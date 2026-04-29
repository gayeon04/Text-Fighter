package com.hotmail.kalebmarc.textfighter.battle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * [Step 4] BattleRecord - 전투 데이터 수집
 *
 * BattleManager에서 수집한 데이터를 BattleAnalyzer에 전달하는 DTO 역할.
 * Stream/Collectors로 이벤트를 분석.
 *
 * 적용 개념: 컬렉션, Stream, Collectors, enum
 */
public class BattleRecord {

    /** 전투 이벤트 타입 */
    public enum EventType {
        ATTACK, HIT, MISS, CRITICAL, POTION_USED, RAN_AWAY, KILL
    }

    /** 단일 전투 이벤트 */
    public static class BattleEvent {
        private final EventType type;
        private final int value;
        private final String description;

        public BattleEvent(EventType type, int value, String description) {
            this.type        = type;
            this.value       = value;
            this.description = description;
        }

        public EventType getType()        { return type;        }
        public int       getValue()       { return value;       }
        public String    getDescription() { return description; }
    }

    // ── 데이터 ──────────────────────────────────────────

    private final List<BattleEvent> events  = new ArrayList<>();
    private final String            playerName;

    public BattleRecord(String playerName) {
        this.playerName = playerName;
    }

    // ── 이벤트 기록 ──────────────────────────────────────

    public void record(EventType type, int value, String desc) {
        events.add(new BattleEvent(type, value, desc));
    }

    // ── Stream 분석 메서드 ────────────────────────────────

    /** 총 가한 데미지 (Stream mapToInt + sum) */
    public int getTotalDamageDealt() {
        return events.stream()
                .filter(e -> e.getType() == EventType.ATTACK || e.getType() == EventType.CRITICAL)
                .mapToInt(BattleEvent::getValue)
                .sum();
    }

    /** 총 받은 데미지 */
    public int getTotalDamageTaken() {
        return events.stream()
                .filter(e -> e.getType() == EventType.HIT)
                .mapToInt(BattleEvent::getValue)
                .sum();
    }

    /** 전투 효율 (가한 데미지 / 전체 데미지) */
    public double getEfficiency() {
        int dealt = getTotalDamageDealt();
        int taken = getTotalDamageTaken();
        if (dealt + taken == 0) return 0;
        return (double) dealt / (dealt + taken) * 100;
    }

    /** 이벤트 타입별 횟수 (groupingBy + counting) */
    public Map<EventType, Long> getEventCounts() {
        return events.stream()
                .collect(Collectors.groupingBy(
                        BattleEvent::getType,
                        Collectors.counting()
                ));
    }

    /** 특정 타입 이벤트 횟수 */
    public long countEvents(EventType type) {
        return events.stream()
                .filter(e -> e.getType() == type)
                .count();
    }

    /** 최대 단일 데미지 (Stream max + OptionalInt) */
    public int getMaxSingleDamage() {
        return events.stream()
                .filter(e -> e.getType() == EventType.ATTACK || e.getType() == EventType.CRITICAL)
                .mapToInt(BattleEvent::getValue)
                .max()
                .orElse(0);
    }

    // ── Getter ───────────────────────────────────────────

    public String          getPlayerName() { return playerName; }
    public List<BattleEvent> getEvents()   { return Collections.unmodifiableList(events); }
    public int             getTotalTurns() { return (int) events.stream()
            .filter(e -> e.getType() == EventType.ATTACK
                    || e.getType() == EventType.MISS
                    || e.getType() == EventType.CRITICAL)
            .count(); }
}