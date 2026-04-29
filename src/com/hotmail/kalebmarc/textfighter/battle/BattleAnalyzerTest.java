package com.hotmail.kalebmarc.textfighter.battle;

/**
 * [Step 4] BattleAnalyzer 테스트
 * 다양한 플레이 스타일 시나리오로 칭호 판정 확인.
 */
public class BattleAnalyzerTest {

    public static void main(String[] args) {
        System.out.println("=== Step 4: BattleAnalyzer (Stream/Optional) 테스트 ===");

        BattleAnalyzer analyzer = new BattleAnalyzer();

        // ① 시나리오: 도망 전문가
        System.out.println("\n① 시나리오: 도망을 2번 친 플레이어");
        BattleRecord cowardRecord = new BattleRecord("hihi");
        cowardRecord.record(BattleRecord.EventType.ATTACK,   8,  "주먹");
        cowardRecord.record(BattleRecord.EventType.HIT,     12,  "좀비");
        cowardRecord.record(BattleRecord.EventType.RAN_AWAY, 0,  "도망");
        cowardRecord.record(BattleRecord.EventType.ATTACK,   6,  "주먹");
        cowardRecord.record(BattleRecord.EventType.RAN_AWAY, 0,  "도망");
        analyzer.printReport(analyzer.analyze(cowardRecord));

        // ② 시나리오: 무결점 전사
        System.out.println("\n② 시나리오: 압도적 효율의 전사");
        BattleRecord perfectRecord = new BattleRecord("hihi");
        perfectRecord.record(BattleRecord.EventType.ATTACK,   35, "저격총");
        perfectRecord.record(BattleRecord.EventType.HIT,       3, "좀비");
        perfectRecord.record(BattleRecord.EventType.ATTACK,   42, "저격총");
        perfectRecord.record(BattleRecord.EventType.ATTACK,   38, "저격총");
        perfectRecord.record(BattleRecord.EventType.HIT,       2, "좀비");
        analyzer.printReport(analyzer.analyze(perfectRecord));

        // ③ 시나리오: 포션 중독자
        System.out.println("\n③ 시나리오: 포션을 3번 쓴 플레이어");
        BattleRecord potionRecord = new BattleRecord("hihi");
        potionRecord.record(BattleRecord.EventType.ATTACK,      8, "주먹");
        potionRecord.record(BattleRecord.EventType.HIT,        20, "좀비");
        potionRecord.record(BattleRecord.EventType.POTION_USED, 0, "생존포션");
        potionRecord.record(BattleRecord.EventType.HIT,        18, "좀비");
        potionRecord.record(BattleRecord.EventType.POTION_USED, 0, "회복포션");
        potionRecord.record(BattleRecord.EventType.ATTACK,      9, "주먹");
        potionRecord.record(BattleRecord.EventType.POTION_USED, 0, "생존포션");
        analyzer.printReport(analyzer.analyze(potionRecord));

        // ④ Stream 분석 직접 확인
        System.out.println("\n④ Stream 분석 직접 확인:");
        System.out.println("   이벤트 타입별 횟수:");
        potionRecord.getEventCounts()
                .forEach((type, count) ->
                        System.out.printf("   %-15s : %d회%n", type, count));

        System.out.println("\n=== 테스트 완료 ===");
    }
}