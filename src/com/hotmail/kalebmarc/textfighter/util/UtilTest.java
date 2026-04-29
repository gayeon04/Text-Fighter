package com.hotmail.kalebmarc.textfighter.util;

/**
 * [Step 6+7] AutoSaveTask + GameLogger 통합 테스트
 *
 * AutoSaveTask: 별도 스레드에서 3초마다 자동저장
 * GameLogger:   Singleton으로 모든 로그 수집 + Stream 분석
 */
public class UtilTest {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Step 6+7: AutoSaveTask + GameLogger 테스트 ===\n");

        GameLogger logger = GameLogger.getInstance();

        // ① Singleton 확인 - 두 번 호출해도 같은 인스턴스
        System.out.println("① Singleton 확인:");
        GameLogger logger2 = GameLogger.getInstance();
        System.out.println("   같은 인스턴스? " + (logger == logger2)); // true

        // ② AutoSaveTask 시작 - 람다로 저장 로직 주입
        System.out.println("\n② AutoSaveTask 시작 (3초 간격):");
        AutoSaveTask autoSave = new AutoSaveTask(
                () -> {
                    // 실제 게임에서는 Saves.save(true) 호출
                    // 테스트에서는 로거에 기록
                    logger.save("자동저장 실행");
                },
                3  // 3초마다
        );
        autoSave.start();

        // ③ 게임 중 다양한 로그 기록 (메인 스레드)
        System.out.println("\n③ 게임 로그 기록 중...");
        logger.info("게임 시작");
        logger.battle("좀비와 전투 시작");
        logger.battle("주먹 → 8 데미지");
        logger.battle("좀비 처치!");
        logger.info("레벨업: 1 → 2");
        logger.warn("체력 낮음: 15/100");

        // 자동저장이 2번 실행될 시간 대기
        System.out.println("   (7초 대기 - 자동저장 2번 실행)");
        Thread.sleep(7000);

        // ④ 즉시 저장 (수동 트리거)
        System.out.println("\n④ 수동 즉시 저장:");
        autoSave.saveNow();
        Thread.sleep(500);

        // ⑤ 자동저장 중지
        System.out.println("\n⑤ 자동저장 중지:");
        autoSave.stop();

        // ⑥ GameLogger Stream 분석
        System.out.println("\n⑥ GameLogger Stream 분석:");
        System.out.println("   BATTLE 로그:");
        logger.filterByLevel(GameLogger.LogLevel.BATTLE)
                .forEach(l -> System.out.println("   " + l));

        System.out.println("\n   '저장' 키워드 검색:");
        logger.search("저장")
                .forEach(l -> System.out.println("   " + l));

        // ⑦ 로그 요약
        logger.printSummary();

        System.out.println("\n   총 저장 횟수: " + autoSave.getSaveCount());
        System.out.println("\n=== 테스트 완료 ===");
    }
}