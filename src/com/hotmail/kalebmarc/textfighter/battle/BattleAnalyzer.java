package com.hotmail.kalebmarc.textfighter.battle;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * [Step 4] BattleAnalyzer - Stream / Optional 활용 강화
 *
 * BattleRecord 데이터를 Stream으로 분석해서
 * 플레이 스타일에 맞는 칭호와 총평을 생성.
 *
 * 적용 개념:
 *  - Stream (filter, map, findFirst, mapToInt)
 *  - Optional (orElse, ifPresent)
 *  - 람다 / Predicate
 *  - List.of() (불변 컬렉션)
 */
public class BattleAnalyzer {

    /**
     * 분석 결과 DTO
     */
    public static class AnalysisResult {
        private final PlayerTitle title;
        private final double      efficiency;
        private final BattleRecord record;

        public AnalysisResult(PlayerTitle title, double efficiency, BattleRecord record) {
            this.title      = title;
            this.efficiency = efficiency;
            this.record     = record;
        }

        public PlayerTitle  getTitle()      { return title;      }
        public double       getEfficiency() { return efficiency; }
        public BattleRecord getRecord()     { return record;     }
    }

    /**
     * BattleRecord를 분석해서 칭호 + 총평 결정.
     *
     * 핵심: 판정 규칙을 Map.Entry<Predicate, PlayerTitle> 리스트로 관리.
     * Stream.filter() + findFirst()로 첫 번째 맞는 규칙 적용.
     * → if/else 체인 없이 선언형으로 깔끔하게 처리.
     */
    public AnalysisResult analyze(BattleRecord record) {

        double efficiency = record.getEfficiency();
        long   missCount  = record.countEvents(BattleRecord.EventType.MISS);
        long   critCount  = record.countEvents(BattleRecord.EventType.CRITICAL);
        long   ranAway    = record.countEvents(BattleRecord.EventType.RAN_AWAY);
        long   potions    = record.countEvents(BattleRecord.EventType.POTION_USED);
        int    dealt      = record.getTotalDamageDealt();
        int    taken      = record.getTotalDamageTaken();

        // 판정 규칙: 우선순위 순서대로 배치
        // Predicate<BattleRecord>를 람다로 정의 → Stream으로 첫 번째 매칭 탐색
        List<Map.Entry<Predicate<BattleRecord>, PlayerTitle>> rules = List.of(
                Map.entry(r -> ranAway >= 2,                          PlayerTitle.COWARD),
                Map.entry(r -> efficiency >= 85,                      PlayerTitle.PERFECTIONIST),
                Map.entry(r -> missCount == 0 && dealt > 40,         PlayerTitle.SNIPER_GOD),
                Map.entry(r -> critCount >= 3,                        PlayerTitle.BERSERKER),
                Map.entry(r -> potions >= 3,                          PlayerTitle.POTION_ADDICT),
                Map.entry(r -> dealt > taken * 2 && taken > 30,      PlayerTitle.GLASS_CANNON),
                Map.entry(r -> taken > dealt && taken > 40,          PlayerTitle.TANK),
                Map.entry(r -> true,                                  PlayerTitle.LUCKY) // 기본값
        );

        // Stream으로 첫 번째 매칭 규칙 찾기
        PlayerTitle title = rules.stream()
                .filter(entry -> entry.getKey().test(record))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(PlayerTitle.LUCKY);

        return new AnalysisResult(title, efficiency, record);
    }

    /**
     * 분석 결과를 보기 좋게 출력.
     */
    public void printReport(AnalysisResult result) {
        BattleRecord record = result.getRecord();

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         전투 분석 리포트              ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.printf ("║  플레이어: %-26s║%n", record.getPlayerName());
        System.out.println("╠══════════════════════════════════════╣");
        System.out.printf ("║  칭호: %-29s║%n", result.getTitle().getTitle());
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  총평:                               ║");
        System.out.printf ("║    %-34s║%n", result.getTitle().getComment().split("\n")[0]);
        System.out.println("╠══════════════════════════════════════╣");

        // 통계 (Stream으로 집계한 값들)
        System.out.printf ("║  가한 데미지  : %-21d║%n", record.getTotalDamageDealt());
        System.out.printf ("║  받은 데미지  : %-21d║%n", record.getTotalDamageTaken());
        System.out.printf ("║  전투 효율    : %-19.1f%%║%n", result.getEfficiency());
        System.out.printf ("║  최대 단타    : %-21d║%n", record.getMaxSingleDamage());
        System.out.printf ("║  크리티컬     : %-21d║%n", record.countEvents(BattleRecord.EventType.CRITICAL));
        System.out.printf ("║  빗나감       : %-21d║%n", record.countEvents(BattleRecord.EventType.MISS));
        System.out.printf ("║  포션 사용    : %-21d║%n", record.countEvents(BattleRecord.EventType.POTION_USED));
        System.out.println("╚══════════════════════════════════════╝");
    }
}