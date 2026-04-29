package com.hotmail.kalebmarc.textfighter.player;

import com.hotmail.kalebmarc.textfighter.item.FirstAid;
import com.hotmail.kalebmarc.textfighter.item.InstaHealth;
import com.hotmail.kalebmarc.textfighter.item.Power;
import com.hotmail.kalebmarc.textfighter.main.*;

public class Stats {
    //Battle Stats
    public static int highScore;
    public static int kills;
    public static int totalDamageDealt;
    public static int totalKills;
    public static int bulletsFired;
    public static int bulletsThatHit;
    //Coins
    public static int totalCoinsSpent;
    public static int coinsSpentOnWeapons;
    public static int coinsSpentOnHealth;
    public static int coinsSpentOnBankInterest;
    public static int xpBought;
    //Other
    public static int timesCheated;
    public static int timesQuit;
    public static int itemsCrafted;
    public static int diceGamesPlayed;
    public static int slotGamesPlayed;
    public static int lotteryTicketsBought;
    public static int lotteryWon;
    public static int blackjackGamesPlayed;
    private static String killDeathRatio;

    private Stats() {
    }

    public static void view() {

        updateKillDeathRatio();

        Ui.cls();
        Ui.println("============================================================");
        Ui.println("  [ 플레이어 통계 ]");
        Ui.println("============================================================");
        Ui.println("  [ 전투 ]");
        Ui.println("  최고 기록:        " + highScore);
        Ui.println("  현재 연속 처치:    " + kills);
        Ui.println("  POWER 사용 횟수:  " + Power.used);
        Ui.println("  현재 무기:        " + Weapon.get().getName());
        Ui.println("  현재 적:          " + com.hotmail.kalebmarc.textfighter.main.Enemy.get().getName());
        Ui.println("  총 데미지:        " + totalDamageDealt);
        Ui.println("  총 처치 수:       " + totalKills);
        Ui.println("  총알 발사 수:     " + bulletsFired);
        Ui.println("  총알 명중 수:     " + bulletsThatHit);
        Ui.println("  K/D:              " + killDeathRatio);
        Ui.println("------------------------------------------------------------");
        Ui.println("  [ 코인 ]");
        Ui.println("  보유 코인:        " + Coins.get());
        Ui.println("  은행 잔액:        " + Bank.get());
        Ui.println("  카지노 획득:      " + Casino.totalCoinsWon);
        Ui.println("  카지노 게임 수:   " + Casino.gamesPlayed);
        Ui.println("  총 사용 코인:     " + totalCoinsSpent);
        Ui.println("  은행 수수료:      " + coinsSpentOnBankInterest);
        Ui.println("  무기에 사용:      " + coinsSpentOnWeapons);
        Ui.println("  회복에 사용:      " + coinsSpentOnHealth);
        Ui.println("  구매한 경험치:    " + xpBought);
        Ui.println("------------------------------------------------------------");
        Ui.println("  [ 회복 ]");
        Ui.println("  현재 체력:        " + Health.getStr());
        Ui.println("  인스타 힐 사용:   " + InstaHealth.used);
        Ui.println("  응급 키트 사용:   " + FirstAid.used);
        Ui.println("  생존 포션 사용:   " + (Potion.spUsed));
        Ui.println("  회복 포션 사용:   " + (Potion.rpUsed));
        Ui.println("  총 포션 사용:     " + (Potion.spUsed + Potion.rpUsed));
        Ui.println("  사망 횟수:        " + Health.timesDied);
        Ui.println("  음식 먹은 수:     " + Food.totalEaten);
        Ui.println("------------------------------------------------------------");
        Ui.println("  [ 기타 ]");
        Ui.println("  치트 활성화:      " + Cheats.enabled());
        Ui.println("  레벨:             " + Xp.getLevel());
        Ui.println("  경험치:           " + Xp.getFull());
        Ui.println("  총 획득 경험치:   " + Xp.total);
        Ui.println("  치트 사용 횟수:   " + timesCheated);
        Ui.println("  종료 횟수:        " + timesQuit);
        Ui.println("  주사위 게임:      " + diceGamesPlayed);
        Ui.println("  슬롯 게임:        " + slotGamesPlayed);
        Ui.println("  블랙잭 게임:      " + blackjackGamesPlayed);
        Ui.println("  복권 구매:        " + lotteryTicketsBought);
        Ui.println("  복권 당첨:        " + lotteryWon);
        Ui.println("============================================================");
        Ui.pause();
    }

    private static void updateKillDeathRatio() {
        int i, gcm = 1, first = totalKills, second = Health.timesDied;

        i = (first >= second) ? first : second;

        while (i != 0) {
            if (first % i == 0 && second % i == 0) {
                gcm = i;
                break;
            }
            i--;
        }

        killDeathRatio = first / gcm + ":" + second / gcm;
    }
}
