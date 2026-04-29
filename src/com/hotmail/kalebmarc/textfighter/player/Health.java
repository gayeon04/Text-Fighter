package com.hotmail.kalebmarc.textfighter.player;

import com.hotmail.kalebmarc.textfighter.item.Armour;
import com.hotmail.kalebmarc.textfighter.main.Enemy;
import com.hotmail.kalebmarc.textfighter.main.Handle;
import com.hotmail.kalebmarc.textfighter.main.Ui;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.*;

public class Health {

    public static int timesDied;
    private static int health;
    private static int outOf;
    //Constants
    private static int UPGRADE_PRICE;

    private Health() {
    }

    public static String getStr() {
        return health + "/" + outOf;
    }

    public static int get() {
        return health;
    }

    public static int getOutOf() {
        return outOf;
    }

    public static void set(int h) {
        health = h;
    }

    public static void set(int h, int hOutOf) {
        health = h;
        outOf = hOutOf;
    }

    public static void setUpgradePrice(int price) {
        UPGRADE_PRICE = price;
    }

    public static void gain(int h) {
        health += h;
        if (health > outOf) {
            health = outOf;
        }
    }

    private static void lose(int h) {
        health -= h;
        if (health <= 0) {
            die();
        }
    }

    public static void die() {
        float randomCoinLoss = ThreadLocalRandom.current().nextInt(25, 51); //random between 25% and 50%
        int coinsLost = Math.round(Coins.get() * (randomCoinLoss / 100));
        Ui.popup("사망했습니다!\n" + coinsLost + " 코인을 잃었습니다.", "사망!", JOptionPane.WARNING_MESSAGE);
        Coins.set(-(coinsLost), true);
        Stats.kills = 0;
        Health.set(Health.getOutOf());
        timesDied++;
    }

    public static void takeDamage(int damage) {

        if (Settings.getGodMode()) {
            damage = 0;
        }

        double resist = Armour.getEquipped().getDamResist() / 100.0;
        damage = (int) (damage - (damage * resist));

        Ui.cls();
        Ui.println("------------------------------------------------------------");
        Ui.println("  " + Enemy.get().getName() + "에게 공격받았습니다!");
        Ui.println("  체력 " + damage + " 감소!");
        Ui.println("------------------------------------------------------------");
        Ui.println("  내 체력: " + (health - damage));
        Ui.println("  적 체력: " + Enemy.get().getHeathStr());
        Ui.println("------------------------------------------------------------");
        Ui.pause();
        Health.lose(damage);

    }

    private static int getLevel() {

        //TODO Possibly find a better way to calculate and execute this whole 'upgrade health' section later
        switch (getOutOf()) {
            case 100:
                return 0;
            case 110:
                return 1;
            case 120:
                return 2;
            case 130:
                return 3;
            case 140:
                return 4;
            case 150:
                return 5;
            case 160:
                return 6;
            case 170:
                return 7;
            case 180:
                return 8;
            case 190:
                return 9;
            case 200:
                return 10;
            default:
                Handle.error("Unable to get health level");
                return 0;
        }
    }

    public static void upgrade() {
        while (true) {

            //Make sure player didn't already upgrade fully
            if (Health.getOutOf() == 200) {
                Ui.msg("체력이 최대 레벨까지 업그레이드되었습니다.");
                return;
            }

            // Calculate how much to upgrade to.
            int health = getOutOf() + 10;

            //Make sure health doesn't go over 200
            if (health > 200) {
                health = 200;
            }

            Ui.cls();
            Ui.println("============================================================");
            Ui.println("  [ 체력 업그레이드 ]");
            Ui.println("============================================================");
            Ui.println("  최대 체력을 200까지 올릴 수 있습니다.");
            Ui.println("  1회 업그레이드당 10 HP 증가, " + UPGRADE_PRICE + " 코인 소모.");
            Ui.println("------------------------------------------------------------");
            Ui.println("  현재 체력: " + getStr());
            Ui.println("------------------------------------------------------------");
            Ui.println("  1) " + health + " HP로 업그레이드");
            Ui.println("  2) 뒤로");
            Ui.println("============================================================");

            if (Ui.getValidInt() == 1) {

                //Level that player is trying to upgrade to,
                //and level needed to upgrade.
                int level = getLevel() + 1;

                if ((Xp.getLevel() >= level) && (Coins.get() >= UPGRADE_PRICE)) {

                    //Make sure user doesn't already have full health
                    if (getLevel() == 10) {
                        Ui.msg("이미 최대 체력입니다!");
                    }

                    //Upgrade health
                    Health.set(health, health);
                    Coins.set(-UPGRADE_PRICE, true);

                    Ui.msg("체력이 업그레이드되었습니다!");

                } else {
                    Ui.cls();
                    Ui.println("업그레이드 불가! 최소 " + level + " 레벨, " + UPGRADE_PRICE + " 코인이 필요합니다.");
                    Ui.println();
                    Ui.println("현재 레벨: " + Xp.getLevel());
                    Ui.println("현재 코인: " + Coins.get());
                    Ui.pause();
                }//if
            } else {
                return;
            }
        }//While(true)
    }//upgrade
}//Health
