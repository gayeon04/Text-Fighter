package com.hotmail.kalebmarc.textfighter.main;

import com.hotmail.kalebmarc.textfighter.item.Armour;
import com.hotmail.kalebmarc.textfighter.item.FirstAid;
import com.hotmail.kalebmarc.textfighter.item.InstaHealth;
import com.hotmail.kalebmarc.textfighter.item.Power;
import com.hotmail.kalebmarc.textfighter.player.Coins;
import com.hotmail.kalebmarc.textfighter.player.Potion;
import com.hotmail.kalebmarc.textfighter.player.Stats;
import com.hotmail.kalebmarc.textfighter.player.Xp;

import java.util.ArrayList;

class Shop {
    private Shop() {
    }

    public static void menu() {
        while (true) {
            Ui.cls();
            Ui.println("============================================================");
            Ui.println("  상점에 오신 것을 환영합니다!");
            Ui.println("============================================================");
            Ui.println("  코인:      " + Coins.get());
            Ui.println("  응급 키트: " + FirstAid.get() + "개");
            Ui.println("  포션:  생존 " + Potion.get("survival") + "개 / 회복 " + Potion.get("recovery") + "개");
            Ui.println("============================================================");
            Ui.println("  1) 회복 아이템   2) 무기/탄약");
            Ui.println("  3) 방어구        4) 부동산");
            Ui.println("  5) 경험치        6) 뒤로");
            Ui.println("============================================================");
            switch (Ui.getValidInt()) {
                case 1:
                    health();
                    break;
                case 2:
                    weapons();
                    break;
                case 3:
                    armour();
                    break;
                case 4:
                    property();
                    break;
                case 5:
                    xp();
                    break;
                case 6:
                    return;
                default:
                    break;
            }
        }
    }

    private static void health() {

        while (true) {
            Ui.cls();
            Ui.println("============================================================");
            Ui.println("  [ 회복 아이템 ]");
            NPC.welcome("Health");
            Ui.println("============================================================");
            Ui.println("  코인:      " + Coins.get());
            Ui.println("  응급 키트: " + FirstAid.get() + "개");
            Ui.println("  포션:  생존 " + Potion.get("survival") + "개 / 회복 " + Potion.get("recovery") + "개");
            Ui.println("  인스타 힐: " + InstaHealth.get() + "개");
            Ui.println("------------------------------------------------------------");
            Ui.println("  1) 응급 치료 키트");
            Ui.println("     가격: " + FirstAid.price + " 코인   레벨: " + FirstAid.level);
            Ui.println();
            Ui.println("  2) 생존 포션");
            Ui.println("     가격: " + Potion.spPrice + " 코인   레벨: " + Potion.spLevel);
            Ui.println();
            Ui.println("  3) 회복 포션");
            Ui.println("     가격: " + Potion.rpPrice + " 코인   레벨: " + Potion.rpLevel);
            Ui.println();
            Ui.println("  4) 인스타 힐");
            Ui.println("     가격: " + InstaHealth.price + " 코인   레벨: " + InstaHealth.level);
            Ui.println();
            Ui.println("  5) 뒤로");
            Ui.println("============================================================");
            switch (Ui.getValidInt()) {
                case 1:
                    Ui.cls();
                    FirstAid.buy();
                    NPC.gratitude("Health", "purchase");
                    break;
                case 2:
                    Ui.cls();
                    Potion.buy("survival");
                    NPC.gratitude("Health", "purchase");
                    break;
                case 3:
                    Ui.cls();
                    Potion.buy("recovery");
                    NPC.gratitude("Health", "purchase");
                    break;
                case 4:
                    Ui.cls();
                    InstaHealth.buy();
                    NPC.gratitude("Health", "purchase");
                    break;
                case 5:
                    return;
                default:
                    break;
            }
        }
    }

    private static void weapons() {
        while (true) {
            Ui.cls();
            Ui.println("============================================================");
            Ui.println("  [ 무기 ]");
            NPC.welcome("Weapon");
            Ui.println("============================================================");
            Ui.println("  코인: " + Coins.get() + "   레벨: " + Xp.getLevel());
            Ui.println("------------------------------------------------------------");
            int j = 0;
            int[] offset = new int[Weapon.getWeapons().size()];
            for (int i = 0; i < Weapon.getWeapons().size(); i++) {
                if (Weapon.getWeapons().get(i).isBuyable()) {
                    Ui.println("  " + (j + 1) + ") " + Weapon.getWeapons().get(i).getName());
                    Ui.println("     가격: " + Weapon.getWeapons().get(i).price + " 코인   레벨: " + Weapon.getWeapons().get(i).level);
                    offset[j] = i - j;
                    j++;
                    Ui.println();
                }
            }
            Ui.println("  " + (j + 1) + ") POWER");
            Ui.println("     가격: " + Power.price + " 코인   레벨: " + Power.level);
            Ui.println();
            Ui.println("  " + (j + 2) + ") 탄약 구매");
            Ui.println();
            Ui.println("  " + (j + 3) + ") 뒤로");

            while (true) {//Make it easy to break, without going back to main store menu

                int menuItem = Ui.getValidInt();

                try { //This is probably pretty bad practice. Using exceptions as a functional part of the program.. Use variables!

                    //choices other than options in the array go here:
                    if (menuItem == (j + 1))
                        Power.buy();
                    if (menuItem == (j + 2))
                        buyAmmo();
                    if (menuItem == (j + 3) || menuItem > j)
                        return;

                    //reverts back to Weapon indexing
                    menuItem--;
                    menuItem = menuItem + offset[menuItem];

                    //Results go here:
                    Weapon.getWeapons().get(menuItem).buy();
                    return;

                } catch (Exception e) {
                    Ui.println();
                    Ui.println(menuItem + " 은(는) 유효하지 않은 선택입니다.");
                }
            }
        }
    }

    private static void xp() {

        //Makes sure player has enough money
        boolean valid;

        while (true) {

            //Makes sure player isn't level 10 already
            if (Xp.getLevel() == 100) {
                Ui.msg("이미 최고 레벨(100)입니다! 경험치를 더 구매할 수 없습니다.");
                return;
            }

            Ui.cls();
            Ui.println("============================================================");
            Ui.println("  [ 경험치 ]");
            NPC.welcome("XP");
            Ui.println("============================================================");
            Ui.println("  레벨: " + Xp.getLevel() + "   경험치: " + Xp.getFull());
            Ui.println("  코인: " + Coins.get());
            Ui.println("------------------------------------------------------------");
            Ui.println("  경험치 1당 코인 1개입니다. 얼마나 구매하시겠습니까?");
            Ui.println("  (0을 입력하면 뒤로 돌아갑니다)");
            Ui.println("============================================================");

            int buy = Ui.getValidInt();
            valid = true;

            //Tests
            if (buy > Coins.get()) {
                //Not enough coins
                Ui.msg("코인이 부족합니다.");
                valid = false;
            }
            if (Xp.getLevel() == 100) {
                Ui.msg("이미 최고 레벨(100)입니다.");
                valid = false;
            }
            if (buy < 0) {
                Ui.msg("음수 경험치는 구매할 수 없습니다. 영리한 시도네요 ;)");
                valid = false;
            }
            if (buy == 0) {
                return;
            }

            if (valid) {
                Ui.msg("경험치 " + buy + "을(를) 구매했습니다.");

                //Results
                Xp.set(buy, true);
                Coins.set(-buy, true);
                Stats.xpBought += buy;
                NPC.gratitude("XP", "purchase");
            }

        }
    }

    private static void buyAmmo() {


        while (true) {
            Ui.cls();
            Ui.println("============================================================");
            Ui.println("  [ 탄약 구매 ]");
            Ui.println("============================================================");
            Ui.println("  코인: " + Coins.get() + "   레벨: " + Xp.getLevel());
            Ui.println("------------------------------------------------------------");
            ArrayList<Weapon> validWeapons = new ArrayList<Weapon>();
            for (int i = 0; i < Weapon.getWeapons().size(); i++) {
                if (Weapon.getWeapons().get(i).isBuyable() && !Weapon.getWeapons().get(i).melee && Weapon.getWeapons().get(i).owns()) {
                    Ui.println("  " + (validWeapons.size() + 1) + ") " + Weapon.getWeapons().get(i).getName());
                    Ui.println("     가격: " + Weapon.getWeapons().get(i).getAmmoPrice() + " 코인/발   레벨: " + Weapon.getWeapons().get(i).level);
                    validWeapons.add(Weapon.getWeapons().get(i));
                }
            }
            Ui.println("  " + (validWeapons.size() + 1) + ") 뒤로");

            while (true) {//Make it easy to break, without going back to main store menu

                int menuItem = Ui.getValidInt();

                try { //This is probably pretty bad practice. Using exceptions as a functional part of the program.. Use variables!
                    validWeapons.get(menuItem - 1).buyAmmo();
                    NPC.gratitude("Weapon", "purchase");
                    break;

                } catch (Exception e) {

                    if (menuItem == (validWeapons.size() + 1)) {
                        return;
                    }
                    Ui.println();
                    Ui.println(menuItem + " 은(는) 유효하지 않은 선택입니다.");
                    Ui.pause();
                    Ui.cls();
                }
            }
        }
    }
    private static void property(){
        while (true){

            Ui.cls();
            Ui.println("============================================================");
            Ui.println("  [ 부동산 ]");
            NPC.welcome("property");
            Ui.println("  레벨: " + Xp.getLevel() + "   코인: " + Coins.get());
            Ui.println("============================================================");

            //TODO do stuff to buy property
            Ui.pause();//temp


            return;
        }
    }
    private static void armour() {
        while (true) {
            Ui.cls();
            Ui.println("============================================================");
            Ui.println("  [ 방어구 ]");
            NPC.welcome("Armour");
            Ui.println("============================================================");
            Ui.println("  코인: " + Coins.get() + "   레벨: " + Xp.getLevel());
            Ui.println("------------------------------------------------------------");
            int j = 0;
            int[] armourShopOffset = new int[Armour.getArmours().size()];
            for (int i = 0; i < Armour.getArmours().size(); i++) {
                if (Armour.getArmours().get(i).getPrice() != 0) {
                    Ui.println("  " + (j + 1) + ") " + Armour.getArmours().get(i).getName());
                    Ui.println("     가격: " + Armour.getArmours().get(i).getPrice() + " 코인   레벨: " + Armour.getArmours().get(i).getLevel());
                    armourShopOffset[j] = i - j;
                    j++;
                    Ui.println();
                }
            }
            Ui.println("  " + (j + 1) + ") 뒤로");

            while (true) {

                int menuItem = Ui.getValidInt();

                try {

                    //choices other than options in the array go here:
                    if (menuItem == j + 1 || menuItem > j)
                        return;

                    //reverts back to armour indexing
                    menuItem--;
                    menuItem = menuItem + armourShopOffset[menuItem];

                    //Results go here:
                    Armour.getArmours().get(menuItem).buy();
                    return;

                } catch (Exception e) {
                    Ui.println();
                    Ui.println(menuItem + " 은(는) 유효하지 않은 선택입니다.");
                }
            }
        }
    }
}