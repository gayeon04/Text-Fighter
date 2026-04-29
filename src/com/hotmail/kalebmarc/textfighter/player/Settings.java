package com.hotmail.kalebmarc.textfighter.player;

import com.hotmail.kalebmarc.textfighter.item.FirstAid;
import com.hotmail.kalebmarc.textfighter.item.InstaHealth;
import com.hotmail.kalebmarc.textfighter.item.Power;
import com.hotmail.kalebmarc.textfighter.main.*;

public class Settings {

    public static boolean difLocked = false;
    private static String difficulty;
    private static boolean godMode = false;

    private Settings() {
    }

    public static void menu() {
        while (true) {

            Ui.cls();
            Ui.println("============================================================");
            Ui.println("  [ 설정 ]");
            Ui.println("============================================================");
            Ui.println("  1) 난이도 변경 (현재: " + difficulty + ")");
            Ui.println("  2) " + difficulty + " 난이도 고정");
            Ui.println("  3) 치트 잠금 (이후 치트 사용 불가)");
            Ui.println("  4) 팝업 창 토글");
            Ui.println("  5) 뒤로");
            Ui.println("============================================================");

            switch (Ui.getValidInt()) {
                case 1:
                    switchDif();
                    break;
                case 2:
                    lockDif();
                    break;
                case 3:
                    lockCheats();
                    break;
                case 4:
                    if (Ui.guiEnabled) {
                        Ui.guiEnabled = false;
                        Ui.msg("팝업 창이 비활성화되었습니다.");
                    } else {
                        Ui.guiEnabled = true;
                        Ui.msg("팝업 창이 활성화되었습니다.");
                    }
                    break;
                case 5:
                    return;
            }
        }
    }

    public static void setDif(String dif, boolean firstInit, boolean switchDif) {
        difficulty = dif;
        setConstants(dif, firstInit, switchDif);
    }

    private static void switchDif() {
        /*
         * Make sure difficulty isn't locked
		 */
        if (difLocked) {
            Ui.msg("난이도가 잠겨 있습니다. 변경할 수 없습니다.");
            return;
        }

        if (difficulty.equals("Easy")) {
            setDif("Hard", false, true);
        } else {
            setDif("Easy", false, true);
        }
    }

    private static void lockDif() {
        /*
		 * Make sure difficulty isn't already locked
		 */
        if (difLocked) {
            Ui.msg("난이도가 이미 잠겨 있습니다.");
            return;
        }

        while (true) {
            Ui.cls();
            Ui.println(difficulty + " 난이도로 고정하시겠습니까?");
            Ui.println("이후에는 난이도를 변경할 수 없습니다!");
            Ui.println("1) 확인");
            Ui.println("2) 취소");
            switch (Ui.getValidInt()) {
                case 1:
                    Ui.msg("난이도가 " + difficulty + "으로 고정되었습니다.");
                    difLocked = true;
                    return;
                case 2:
                    return;
            }
        }
    }

    private static void lockCheats() {
        /*
		 * Make sure cheats aren't already locked
		 */
        if (Cheats.locked()) {
            Ui.msg("치트가 이미 잠겨 있습니다.");
            return;
        }

		/*
		 * Makes sure cheats aren't already enabled
		 */
        if (Cheats.enabled()) {
            Ui.msg("치트가 이미 활성화되어 있습니다. 비활성화할 수 없습니다.");
            return;
        }

        while (true) {
            Ui.cls();
            Ui.println("정말로 치트를 잠그시겠습니까?");
            Ui.println("이후에는 치트를 사용할 수 없습니다!");
            Ui.println("1) 확인");
            Ui.println("2) 취소");
            switch (Ui.getValidInt()) {
                case 1:
                    Ui.msg("치트가 잠겼습니다.");
                    Cheats.lock();
                    return;
                case 2:
                    return;
            }
        }
    }

    public static String getDif() {
        return difficulty;
    }

    private static void setConstants(String dif, boolean firstInit, boolean changeDif) {
        if (dif.equals("Easy")) {//Sets variables for EASY mode

            //Enemies (Name, health, coindropmin, coindropmax, damagemin, damagemax, xp, levelMin, levelMax, firstinit, changeDif)
            Game.darkElf = new Enemy("Dark Elf", 45, 10, 15, 10, 15, 15, 1, 100, firstInit, changeDif);
            Game.ninja = new Enemy("Ninja", 75, 5, 15, 5, 15, 15, 1, 100, firstInit, changeDif);
            Game.giantSpider = new Enemy("Giant Spider", 35, 5, 10, 5, 10, 10, 1, 100, firstInit, changeDif);
            Game.zombie = new Enemy("Zombie", 20, 5, 15, 5, 15, 15, 1, 100, firstInit, changeDif);
            Game.goblin = new Enemy("Goblin", 60, 10, 20, 10, 20, 20, 1, 100, firstInit, changeDif);
            Game.ghost = new Enemy("Ghost", 85, 15, 25, 15, 25, 25, 1, 100, firstInit, changeDif);
            Game.barbarian = new Enemy("Barbarian", 50, 5, 15, 5, 15, 15, 1, 100, firstInit, changeDif);
            Game.giantAnt = new Enemy("Giant Ant", 30, 5, 10, 5, 10, 10, 1, 100, firstInit, changeDif);
            Game.evilUnicorn = new Enemy("Evil Unicorn", 35, 30, 40, 5, 15, 20, 1, 100, firstInit, changeDif);
            Game.ogre = new Enemy("Ogre", 90, 20, 50, 10, 30, 50, 1, 100, firstInit, changeDif);

            /*Weapons
            * Gun:   (name, ammoUsed, ammoIncludedWithPurchase, buyable, price, ammoPrice, level, chanceOfMissing, critChanceMultiplier,
            *        critDamMultiplierMin, critDamMultiplierMax firstInit, changeDif)
            * Melee: (name, startingWeapon, buyable, price, level, damageMin, damageMax, firstInit, changeDif)
            */

            //Melee:
            Game.fists = new Weapon("Fists", true, false, 0, 0, 5, 10, firstInit, changeDif);
            Game.baseballBat = new Weapon("Baseball Bat", false, true, 120, 1, 10, 15, firstInit, changeDif);
            Game.knife = new Weapon("Knife", false, true, 125, 2, 10, 20, firstInit, changeDif);
            Game.pipe = new Weapon("Pipe", false, false, 0, 0, 5, 20, firstInit, changeDif);
            Game.chainsaw = new Weapon("Chainsaw", false, true, 200, 7, 15, 25, firstInit, changeDif);
            //Guns:
            Game.pistol = new Weapon("Pistol", 1, 18, true, 250, 1, 4, 15, 1.5, 3, 4, firstInit, changeDif);
            Game.smg = new Weapon("Smg", 10, 75, true, 700, 1, 10, 75, 2.5, 4, 6, firstInit, changeDif);
            Game.shotgun = new Weapon("Shotgun", 1, 12, true, 375, 2, 9, 60, 2, 5, 7, firstInit, changeDif);
            Game.rifle = new Weapon("Rifle", 1, 18, true, 275, 1, 5, 10, 1.25, 6, 7, firstInit, changeDif);
            Game.sniper = new Weapon("Sniper", 1, 10, true, 700, 2, 7, 0, 1, 7, 10, firstInit, changeDif);

            //Price
            Power.price = 25;
            Weapon.BULLET_DAMAGE = 10;
            Weapon.BULLET_CRITICAL_CHANCE = 0.01;
            Weapon.BULLET_CRITICAL_MULTIPLIER = 10;
            FirstAid.price = 5;
            Potion.spPrice = 10;
            Potion.rpPrice = 20;
            InstaHealth.price = 30;
            Bank.setInterest(0.05);
            Health.setUpgradePrice(100);

            //Levels needed
            FirstAid.level = 1;
            Potion.spLevel = 2;
            Potion.rpLevel = 2;
            InstaHealth.level = 3;
            Power.level = 4;

        } else {//Sets variables for HARD mode

            //Enemies (Name, health, coindropmin, coindropmax, damagemin, damagemax, xp, levelMin, levelMax, firstinit, changeDif)
            Game.darkElf = new Enemy("Dark Elf", 55, 15, 20, 15, 20, 15, 1, 100, firstInit, changeDif);
            Game.ninja = new Enemy("Ninja", 85, 10, 20, 10, 20, 15, 1, 100, firstInit, changeDif);
            Game.giantSpider = new Enemy("Giant Spider", 45, 10, 15, 10, 15, 10, 1, 100, firstInit, changeDif);
            Game.zombie = new Enemy("Zombie", 30, 10, 20, 10, 20, 15, 1, 100, firstInit, changeDif);
            Game.goblin = new Enemy("Goblin", 70, 15, 25, 15, 25, 20, 1, 100, firstInit, changeDif);
            Game.ghost = new Enemy("Ghost", 95, 20, 30, 20, 30, 25, 1, 100, firstInit, changeDif);
            Game.barbarian = new Enemy("Barbarian", 50, 5, 15, 5, 15, 15, 1, 100, firstInit, changeDif);
            Game.giantAnt = new Enemy("Giant Ant", 30, 5, 10, 5, 10, 10, 1, 100, firstInit, changeDif);
            Game.evilUnicorn = new Enemy("Evil Unicorn", 35, 20, 40, 5, 15, 20, 1, 100, firstInit, changeDif);
            Game.ogre = new Enemy("Ogre", 100, 20, 50, 10, 30, 50, 1, 100, firstInit, changeDif);

            /*Weapons
            * Gun:   (name, ammoUsed, ammoIncludedWithPurchase, buyable, price, ammoPrice, level, chanceOfMissing, critChanceMultiplier,
            *        critDamMultiplierMin, critDamMultiplierMax firstInit, changeDif)
            * Melee: (name, startingWeapon, buyable, price, level, damageMin, damageMax, firstInit, changeDif)
            */

            //Melee:
            Game.fists = new Weapon("Fists", true, false, 0, 0, 5, 10, firstInit, changeDif);
            Game.baseballBat = new Weapon("Baseball Bat", false, true, 170, 1, 10, 15, firstInit, changeDif);
            Game.knife = new Weapon("Knife", false, true, 175, 2, 10, 20, firstInit, changeDif);
            Game.pipe = new Weapon("Pipe", false, false, 0, 0, 5, 20, firstInit, changeDif);
            Game.chainsaw = new Weapon("Chainsaw", false, true, 350, 7, 15, 25, firstInit, changeDif);
            //Guns:
            Game.pistol = new Weapon("Pistol", 1, 18, true, 275, 1, 4, 15, 1.25, 2, 3, firstInit, changeDif);
            Game.smg = new Weapon("Smg", 10, 75, true, 800, 1, 10, 75, 1.75, 3, 5, firstInit, changeDif);
            Game.shotgun = new Weapon("Shotgun", 1, 12, true, 415, 2, 9, 60, 1.5, 4, 6, firstInit, changeDif);
            Game.rifle = new Weapon("Rifle", 1, 18, true, 300, 1, 5, 10, 1, 5, 6, firstInit, changeDif);
            Game.sniper = new Weapon("Sniper", 1, 10, true, 750, 2, 7, 0, .75, 7, 9, firstInit, changeDif);

            //PRICE
            Power.price = 75;
            Weapon.BULLET_DAMAGE = 5;
            Weapon.BULLET_CRITICAL_CHANCE = 0.01;
            Weapon.BULLET_CRITICAL_MULTIPLIER = 10;
            FirstAid.price = 15;
            Potion.spPrice = 25;
            Potion.rpPrice = 35;
            InstaHealth.price = 45;
            Bank.setInterest(0.10);
            Health.setUpgradePrice(100);

            //Levels needed
            FirstAid.level = 1;
            Potion.spLevel = 2;
            Potion.rpLevel = 2;
            InstaHealth.level = 3;
            Power.level = 4;

        }
        
        if (firstInit) newGameSetup();
    }

    private static void newGameSetup() {

        Coins.set(50, false);
        FirstAid.set(3, false);
        Potion.set("survival", 2, false);
        Potion.set("recovery", 2, false);
        Xp.setAll(0, 500, 1);
        Game.none.setOwns(true);
        Game.none.equipSilent();

    }

    //GOD MODE METHODS
    public static boolean getGodMode() {
        return godMode;
    }

    //Returns message only if god mode is enabled
    public static String godModeMsg() {
        if (godMode) {
            return "  [!] 갓모드 활성화됨\n";
        }
        return "";
    }

    public static void toggleGodMode() {
        if (godMode) {
            godMode = false;
            Ui.msg("갓모드가 비활성화되었습니다.");
        } else {
            godMode = true;
            Ui.msg("갓모드가 활성화되었습니다.");
        }
    }
}
