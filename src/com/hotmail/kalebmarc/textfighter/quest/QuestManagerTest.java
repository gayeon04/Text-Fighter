package com.hotmail.kalebmarc.textfighter.quest;

/**
 * [Step 5] QuestManager (Observer Pattern) 테스트
 */
public class QuestManagerTest {

    public static void main(String[] args) {
        System.out.println("=== Step 5: QuestManager (Observer Pattern) 테스트 ===\n");

        QuestManager qm = QuestManager.getInstance();

        // ① 퀘스트 등록
        System.out.println("① 퀘스트 등록:");
        qm.subscribe(new KillQuest("좀비 사냥꾼",   "Zombie",  3, 50));
        qm.subscribe(new KillQuest("몬스터 헌터",    null,      5, 100));
        qm.subscribe(new CriticalQuest("크리티컬 마스터", 2,       80));

        // ② 이벤트 발행 - 좀비 처치
        System.out.println("\n② 좀비 3번 처치:");
        qm.notify(GameEvent.ENEMY_KILLED, "Zombie");
        qm.notify(GameEvent.ENEMY_KILLED, "Zombie");
        qm.notify(GameEvent.ENEMY_KILLED, "Zombie"); // 퀘스트 완료!

        // ③ 다른 적 처치
        System.out.println("\n③ 고블린 처치:");
        qm.notify(GameEvent.ENEMY_KILLED, "Goblin");
        qm.notify(GameEvent.ENEMY_KILLED, "Goblin");

        // ④ 크리티컬 이벤트
        System.out.println("\n④ 크리티컬 히트 2번:");
        qm.notify(GameEvent.CRITICAL_HIT, null);
        qm.notify(GameEvent.CRITICAL_HIT, null); // 크리티컬 퀘스트 완료!

        // ⑤ 전체 현황
        qm.printStatus();

        // ⑥ Stream 조회 시연
        System.out.println("\n⑥ 완료된 퀘스트 수: " + qm.getCompletedQuests().size());
        System.out.println("   진행 중 퀘스트 수: " + qm.getActiveQuests().size());

        System.out.println("\n=== 테스트 완료 ===");
    }
}