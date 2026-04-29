package com.hotmail.kalebmarc.textfighter.player;

import com.hotmail.kalebmarc.textfighter.main.Casino;
import com.hotmail.kalebmarc.textfighter.main.Cheats;
import com.hotmail.kalebmarc.textfighter.main.Enemy;
import com.hotmail.kalebmarc.textfighter.main.Ui;

import javax.swing.*;
import java.util.ArrayList;

public class Achievements {
    //Arrays for achievements
    public static final ArrayList<Boolean> arrayKilled = new ArrayList<>();
    private static final ArrayList<Enemy> arrayEnemy = new ArrayList<>();
    /* Achievements
     * 22 Total
     *
     * Booleans to check if the
     * achievement has been unlocked
     */
    public static boolean moneyMaker = false;
    public static boolean enemySlayer = false;
    public static boolean firstKill = false;
    public static boolean timeForAnUpgrade = false;
    public static boolean textFighterMaster = false;
    public static boolean YAYPOWER = false;
    public static boolean awwYouCareAboutMe = false;
    public static boolean slayer = false;
    public static boolean nobodysPerfect = false;
    public static boolean makingMoney = false;
    public static boolean gamblingAddiction = false;
    public static boolean unnaturalLuck = false;
    public static boolean level2Fighter = false;
    public static boolean level3Fighter = false;
    public static boolean level4Fighter = false;
    public static boolean level5Fighter = false;
    public static boolean level6Fighter = false;
    public static boolean level7Fighter = false;
    public static boolean level8Fighter = false;
    public static boolean level9Fighter = false;
    public static boolean level10Fighter = false;
    public static boolean honestPlayer = false;
    //Variables for testing the achievements
    //Time for an upgrade
    public static boolean boughtItem = false;
    //Aww, You Care About Me
    public static boolean viewedAbout = false;

    private Achievements() {
    }

    public static void setUpEnemyAch(String name, Enemy type) {

        arrayKilled.add(false);
        arrayEnemy.add(type);

    }

    public static void view() {

        //Displays which achievements the user has gotten
        Ui.cls();

        boolean[] ach = new boolean[22];
        String[] strAch = new String[22];
        ach[0] = moneyMaker;
        strAch[0] = "돈 버는 달인";

        ach[1] = enemySlayer;
        strAch[1] = "적 학살자";

        ach[2] = firstKill;
        strAch[2] = "첫 번째 처치";

        ach[3] = timeForAnUpgrade;
        strAch[3] = "업그레이드 시간";

        ach[4] = textFighterMaster;
        strAch[4] = "텍스트 파이터 마스터";

        ach[5] = YAYPOWER;
        strAch[5] = "POWER 만세!";

        ach[6] = awwYouCareAboutMe;
        strAch[6] = "관심을 가져주셔서 감사합니다 :')";

        ach[7] = slayer;
        strAch[7] = "학살자";

        ach[8] = nobodysPerfect;
        strAch[8] = "완벽한 사람은 없어";

        ach[9] = makingMoney;
        strAch[9] = "돈 모으기";

        ach[10] = gamblingAddiction;
        strAch[10] = "도박 중독";

        ach[11] = level2Fighter;
        strAch[11] = "2레벨 전사!";

        ach[12] = level3Fighter;
        strAch[12] = "3레벨 전사!";

        ach[13] = level4Fighter;
        strAch[13] = "4레벨 전사!";

        ach[14] = level5Fighter;
        strAch[14] = "5레벨 전사!";

        ach[15] = level6Fighter;
        strAch[15] = "6레벨 전사!";

        ach[16] = level7Fighter;
        strAch[16] = "7레벨 전사!";

        ach[17] = level8Fighter;
        strAch[17] = "8레벨 전사!";

        ach[18] = level9Fighter;
        strAch[18] = "9레벨 전사!";

        ach[19] = level10Fighter;
        strAch[19] = "10레벨 전사!";

        ach[20] = honestPlayer;
        strAch[20] = "정직한 플레이어";

        ach[21] = unnaturalLuck;
        strAch[21] = "타고난 행운";

        Ui.println("============================================================");
        Ui.println("  [ 업적 ]");
        Ui.println("============================================================");
        Ui.println("  ★ 획득한 업적:");
        Ui.println();
        for (int i = 0; i < ach.length; i++) {
            if (ach[i]) {
                Ui.println("  " + strAch[i]);
            }
        }
        //Enemy
        for (int i = 0; i < arrayEnemy.size(); i++) {
            if (arrayKilled.get(i)) {
                Ui.println("  처치: " + arrayEnemy.get(i).getName() + "!");
            }
        }
        Ui.println();
        Ui.println("  ☆ 미획득 업적:");
        Ui.println();
        for (int i = 0; i < ach.length; i++) {
            if (!ach[i]) {
                Ui.println("  " + strAch[i]);
            }
        }
        //Enemy
        for (int i = 0; i < arrayEnemy.size(); i++) {
            if (!arrayKilled.get(i)) {
                Ui.println("  처치: " + arrayEnemy.get(i).getName() + "!");
            }
        }
        Ui.println();
        Ui.println("============================================================");

        Ui.pause();
    }

    public static void check() {

        //Tests achievements if not already unlocked

        if (!Cheats.enabled()) {
            if (!moneyMaker) checkMoneyMaker();
            if (!enemySlayer) checkEnemySlayer();
            if (!firstKill) checkFirstKill();
            if (!timeForAnUpgrade) checkTimeForAnUpgrade();
            if (!textFighterMaster) checkTextFighterMaster();
            if (!YAYPOWER) checkYAYPOWER();
            if (!awwYouCareAboutMe) checkAwwYouCareAboutMe();
            if (!slayer) checkSlayer();
            if (!nobodysPerfect) checkNobodysPerfect();
            if (!makingMoney) checkMakingMoney();
            if (!gamblingAddiction) checkGamblingAddiction();
            if (!unnaturalLuck) checkUnnaturalLuck();
            if (!level2Fighter) checkLevel2Fighter();
            if (!level3Fighter) checkLevel3Fighter();
            if (!level4Fighter) checkLevel4Fighter();
            if (!level5Fighter) checkLevel5Fighter();
            if (!level6Fighter) checkLevel6Fighter();
            if (!level7Fighter) checkLevel7Fighter();
            if (!level8Fighter) checkLevel8Fighter();
            if (!level9Fighter) checkLevel9Fighter();
            if (!level10Fighter) checkLevel10Fighter();
            if (!honestPlayer) checkHonestPlayer();
            //Enemy achs get checked from textfighter.Enemy class
        }
    }

    public static void getEnemyAch(Enemy x) {
        if (!arrayKilled.get(arrayEnemy.indexOf(x))) {
            get("처치: " + x.getName() + "!");
            arrayKilled.set(arrayEnemy.indexOf(x), true);
        }
    }

    private static void get(String ach) {
        //Displays that you've gotten an achievement
        Ui.popup("업적을 달성했습니다!\n\n" + ach, "업적 달성!", JOptionPane.INFORMATION_MESSAGE);
        Xp.set(100, true);
    }

    private static void checkUnnaturalLuck() {
        if(Casino.LOTTERY.getJackpotWon()){
            unnaturalLuck = true;
            get("타고난 행운");
        }
    }

    private static void checkMoneyMaker() {
        if (Coins.get() >= 1500) {
            moneyMaker = true;
            get("돈 버는 달인");
        }
    }

    private static void checkEnemySlayer() {
        if (Stats.totalKills >= 100) {
            enemySlayer = true;
            get("적 학살자");
        }
    }

    private static void checkFirstKill() {
        if (Stats.totalKills >= 1) {
            firstKill = true;
            get("첫 번째 처치");
        }
    }

    private static void checkTimeForAnUpgrade() {
        if (boughtItem) {
            timeForAnUpgrade = true;
            get("업그레이드 시간");
        }
    }

    private static void checkTextFighterMaster() {
        if (
                moneyMaker &&
                        enemySlayer &&
                        firstKill &&
                        timeForAnUpgrade &&
                        YAYPOWER &&
                        awwYouCareAboutMe &&
                        slayer &&
                        nobodysPerfect &&
                        makingMoney &&
                        gamblingAddiction &&
                        level2Fighter &&
                        level3Fighter &&
                        level4Fighter &&
                        level5Fighter &&
                        level6Fighter &&
                        level7Fighter &&
                        level8Fighter &&
                        level9Fighter &&
                        level10Fighter &&
                        honestPlayer
                ) {

            //Check Enemy Achs
            int temp = 0;
            for (int i = 0; i <= arrayEnemy.size(); i++) {
                if (arrayKilled.get(i)) {
                    temp++;
                }
            }
            if (temp < arrayEnemy.size()) {
                textFighterMaster = true;
                get("텍스트 파이터 마스터\n축하합니다! 모든 업적을 달성했습니다! 2500 코인을 획득했습니다!");
                Coins.set(2500, true);
            }
        }
    }

    private static void checkYAYPOWER() {
        if (com.hotmail.kalebmarc.textfighter.item.Power.used >= 5) {
            YAYPOWER = true;
            get("POWER 만세!");
        }
    }

    private static void checkAwwYouCareAboutMe() {
        if (viewedAbout) {
            awwYouCareAboutMe = true;
            get("관심을 가져주셔서 감사합니다 :')");

        }
    }

    private static void checkSlayer() {
        if (Stats.totalDamageDealt >= 1000) {
            slayer = true;
            get("학살자");
        }
    }

    private static void checkNobodysPerfect() {
        if (Health.timesDied > 0) {
            nobodysPerfect = true;
            get("완벽한 사람은 없어");
        }
    }

    private static void checkMakingMoney() {
        if (Casino.totalCoinsWon >= 1000) {
            makingMoney = true;
            get("돈 모으기");
        }
    }

    private static void checkGamblingAddiction() {
        if (Casino.gamesPlayed >= 75) {
            gamblingAddiction = true;
            get("도박 중독");
        }
    }

    private static void checkLevel2Fighter() {
        if (Xp.getLevel() == 2) {
            level2Fighter = true;
            get("2레벨 전사!");
        }
    }

    private static void checkLevel3Fighter() {
        if (Xp.getLevel() == 3) {
            level3Fighter = true;
            get("3레벨 전사!");
        }
    }

    private static void checkLevel4Fighter() {
        if (Xp.getLevel() == 4) {
            level4Fighter = true;
            get("4레벨 전사!");
        }
    }

    private static void checkLevel5Fighter() {
        if (Xp.getLevel() == 5) {
            level5Fighter = true;
            get("5레벨 전사!");
        }
    }

    private static void checkLevel6Fighter() {
        if (Xp.getLevel() == 6) {
            level6Fighter = true;
            get("6레벨 전사!");
        }
    }

    private static void checkLevel7Fighter() {
        if (Xp.getLevel() == 7) {
            level7Fighter = true;
            get("7레벨 전사!");
        }
    }

    private static void checkLevel8Fighter() {
        if (Xp.getLevel() == 8) {
            level8Fighter = true;
            get("8레벨 전사!");
        }
    }

    private static void checkLevel9Fighter() {
        if (Xp.getLevel() == 9) {
            level9Fighter = true;
            get("9레벨 전사!");
        }
    }

    private static void checkLevel10Fighter() {
        if (Xp.getLevel() == 10) {
            level10Fighter = true;
            get("10레벨 전사!");
        }
    }

    private static void checkHonestPlayer() {
        if (Cheats.locked()) {
            honestPlayer = true;
            get("정직한 플레이어");
        }
    }
}