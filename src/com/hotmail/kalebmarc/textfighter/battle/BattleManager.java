package com.hotmail.kalebmarc.textfighter.battle;

import java.util.ArrayList;
import java.util.List;

/**
 * [Step 3] BattleManager - Strategy Pattern 핵심
 *
 * 전투 전략(AttackStrategy)을 런타임에 교체할 수 있는 컨텍스트 클래스.
 * 무기가 바뀌면 setStrategy()로 전략만 교체 → 전투 로직 자체는 변경 없음.
 *
 * 적용 개념:
 *  - Strategy Pattern (AttackStrategy 교체)
 *  - 컬렉션 (List<String> 전투 로그)
 *  - 람다 활용 (withBonus 고차 함수)
 */
public class BattleManager {

    private AttackStrategy strategy;
    private final List<String> battleLog = new ArrayList<>();

    // 전투 통계 (Step 4 BattleAnalyzer와 연동)
    private int totalDamageDealt = 0;
    private int totalDamageTaken = 0;
    private int turnsPlayed      = 0;
    private int criticalHits     = 0;
    private int missCount        = 0;

    public BattleManager(AttackStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * 전략 교체 - 무기 변경 시 호출.
     * Strategy Pattern의 핵심: 런타임에 알고리즘 교체.
     */
    public void setStrategy(AttackStrategy strategy) {
        this.strategy = strategy;
        log("전략 변경 → " + strategy.getClass().getSimpleName());
    }

    /**
     * 공격 실행 - 현재 전략으로 데미지 계산.
     *
     * @param dmgMin 최소 데미지
     * @param dmgMax 최대 데미지
     * @param weaponName 무기 이름 (로그용)
     * @return 실제 데미지
     */
    public int attack(int dmgMin, int dmgMax, String weaponName) {
        int damage = strategy.execute(dmgMin, dmgMax);
        turnsPlayed++;

        if (damage == 0) {
            missCount++;
            log("공격 빗나감! (" + weaponName + ")");
        } else if (damage >= dmgMax * 1.8) {
            criticalHits++;
            totalDamageDealt += damage;
            log("크리티컬! " + weaponName + " → " + damage + " 데미지");
        } else {
            totalDamageDealt += damage;
            log(weaponName + " → " + damage + " 데미지");
        }

        return damage;
    }

    /**
     * 적에게 피해 입음 - 통계 기록.
     */
    public void takeDamage(int damage, String enemyName) {
        totalDamageTaken += damage;
        log(enemyName + "의 반격 → " + damage + " 피해");
    }

    // ── 로그 ────────────────────────────────────────────

    private void log(String message) {
        battleLog.add(message);
    }

    public void printLog() {
        System.out.println("\n[전투 로그]");
        battleLog.forEach(entry -> System.out.println("  - " + entry));
    }

    // ── 통계 getter (BattleAnalyzer 연동용) ─────────────

    public int getTotalDamageDealt() { return totalDamageDealt; }
    public int getTotalDamageTaken() { return totalDamageTaken; }
    public int getTurnsPlayed()      { return turnsPlayed;      }
    public int getCriticalHits()     { return criticalHits;     }
    public int getMissCount()        { return missCount;        }

    public List<String> getBattleLog() {
        return List.copyOf(battleLog);
    }

    public void reset() {
        battleLog.clear();
        totalDamageDealt = 0;
        totalDamageTaken = 0;
        turnsPlayed      = 0;
        criticalHits     = 0;
        missCount        = 0;
    }
}