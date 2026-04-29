package com.hotmail.kalebmarc.textfighter.main;

import com.hotmail.kalebmarc.textfighter.item.Armour;
import com.hotmail.kalebmarc.textfighter.player.Achievements;
import com.hotmail.kalebmarc.textfighter.player.Coins;
import com.hotmail.kalebmarc.textfighter.player.Stats;
import com.hotmail.kalebmarc.textfighter.player.Xp;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

public class Weapon implements Comparable<Weapon> {

    //Weapon List
    public static final ArrayList<Weapon> arrayWeapon = new ArrayList<>();
    //Properties
    public static int BULLET_DAMAGE;
    public static int BULLET_CRITICAL_MULTIPLIER;
    public static double BULLET_CRITICAL_CHANCE;
    //Variables
    public static Weapon starting;
    private static Weapon current = null;
    public int price;
    public int level;
    public boolean melee;
    public boolean owns;
    private int damageMin;
    private int damageMax;
    private int damageDealt;
    private double chanceOfMissing;
    private double critChanceMultiplier;
    private int critDamMultiplierMin;
    private int critDamMultiplierMax;
    private String name;
    private boolean buyable;
    //Ammo
    private int ammo;
    private int ammoUsed;
    private int ammoPrice;//Per 1
    private int ammoIncludedWithPurchase;

    public Weapon(String name, int ammoUsed, int ammoIncludedWithPurchase, boolean buyable, int price, //For guns
                  int ammoPrice, int level, double chanceOfMissing, double critChanceMultiplier, int critDamMultiplierMin, int critDamMultiplierMax, boolean firstInit, boolean changeDif) {

        this.name = name;
        this.ammoUsed = ammoUsed;
        this.ammoIncludedWithPurchase = ammoIncludedWithPurchase;
        this.buyable = buyable;
        this.price = price;
        this.ammoPrice = ammoPrice;
        this.level = level;
        this.chanceOfMissing = chanceOfMissing;
        this.critChanceMultiplier = critChanceMultiplier;
        this.critDamMultiplierMin = critDamMultiplierMin;
        this.critDamMultiplierMax = critDamMultiplierMax;
        this.melee = false;

        if (!changeDif) {
            arrayWeapon.add(this);
        }

        if (firstInit) {
            this.owns = false;

        }

        Collections.sort(arrayWeapon);

    }

    public Weapon(String name, boolean startingWeapon, boolean buyable, int price, int level,//For Melee
                  int damageMin, int damageMax, boolean firstInit, boolean changeDif) {
        this.name = name;
        this.buyable = buyable;
        this.price = price;
        this.level = level;
        this.damageMin = damageMin;
        this.damageMax = damageMax;
        this.melee = true;

        if (!changeDif) {
            arrayWeapon.add(this);
        }

        if (firstInit) {
            if (startingWeapon) {//If first init, see if player starts with this or not.
                this.owns = true;
                current = this;
                starting = this;
            } else {
                this.owns = false;
            }
        }
    }

    public static ArrayList<Weapon> getWeapons() {
        return arrayWeapon;
    }

    public static Weapon get() {
        return current;
    }

    static int getIndex(Weapon i) {
        return arrayWeapon.indexOf(i);
    }

    public static void set(Weapon x) {
        current = x;
    }

    public static void set(int i) {
        current = arrayWeapon.get(i);
    }

    public static void choose() {
        while (true) {
            Ui.cls();
            Ui.println("============================================================");
            Ui.println("  무기 장착");
            Ui.println();
            Ui.println("  탄약:      " + current.getAmmo() + "발");
            Ui.println("  현재 무기: " + current.getName());
            Ui.println("============================================================");
            int j = 0;
            int[] offset = new int[getWeapons().size()];
            for (int i = 0; i < getWeapons().size(); i++) {
                if (getWeapons().get(i).owns()) {
                    Ui.println((j + 1) + ") " + getWeapons().get(i).getName());
                    offset[j] = i - j;
                    j++;
                }
            }
            Ui.println((j + 1) + ") Back");

            while (true) {

                int menuItem = Ui.getValidInt();

                try {

                    //choices other than options in the array go here:
                    if (menuItem == (j + 1) || menuItem > j)
                        return;

                    //reverts back to Weapon indexing
                    menuItem--;
                    menuItem = menuItem + offset[menuItem];

                    //Testing to make sure the option is valid goes here:
                    if (!getWeapons().get(menuItem).owns) {
                        Ui.msg("해당 무기를 보유하고 있지 않습니다!");
                        return;
                    }

                    current = getWeapons().get(menuItem);
                    Ui.msg(getWeapons().get(menuItem).getName() + "을(를) 장착했습니다!");
                    return;

                } catch (Exception e) {
                    Ui.println();
                    Ui.println(menuItem + " 은(는) 유효하지 않은 선택입니다.");
                }
            }
        }
    }

    private static void noAmmo() {
        Ui.popup("탄약이 떨어졌습니다!", "경고", JOptionPane.WARNING_MESSAGE);
        Weapon.current = Weapon.starting;
    }

    public static void displayAmmo() {
        if (!(Weapon.get().melee)) {
            Ui.println("  탄약:      " + Weapon.get().getAmmo() + "발");
        }
    }

    public String getName() {
        return name;
    }

    public boolean owns() {
        return owns;
    }

    public void setAmmo(int amount, boolean add) {
        if (this.melee) return;
        if (add) {
            this.ammo += amount;
        } else {
            this.ammo = amount;
        }
    }

    public int getAmmo() {
        return this.ammo;
    }

    public int getDamageDealt() {
        return this.damageDealt;
    }

    public int getDamageMin() {
        return this.damageMin;
    }

    public int getDamageMax() {
        return this.damageMax;
    }

    public void dealDam() {

        if (this.melee) {
            /*
             * Melee Attack
             */
            damageDealt = Random.RInt(this.damageMin, this.damageMax);
        } else {

            /*
             * Gun Attack
             */
            if (getAmmo() >= this.ammoUsed) {

                for (int i = 1; i <= this.ammoUsed; i++) {
                    if (Random.RInt(100) > this.chanceOfMissing) {
                        damageDealt += BULLET_DAMAGE;
                        Stats.bulletsThatHit++;

                    }

                    //Results
                    setAmmo(-1, true);
                    Stats.bulletsFired += 1;
                }
                //Run the logic for critical hit
                criticalHit();
                bulletCriticalHit();

            } else {
                noAmmo();
                damageDealt = 0;
            }
        }

        //Display stuff
        com.hotmail.kalebmarc.textfighter.player.Stats.totalDamageDealt += damageDealt;
        com.hotmail.kalebmarc.textfighter.player.Xp.setBattleXp(damageDealt, true);
        if(!Enemy.get().takeDamage(damageDealt)) { // !dead
            Ui.cls();
            Ui.println("------------------------------------------------------------");
            Ui.println("  " + Enemy.get().getName() + "을(를) 공격했습니다!");
            Ui.println("  " + this.name + "으로 " + damageDealt + " 데미지!");
            Ui.println("------------------------------------------------------------");
            Ui.println("  내 체력: " + com.hotmail.kalebmarc.textfighter.player.Health.getStr());
            Ui.println("  적 체력: " + Enemy.get().getHeathStr());
            Ui.println("------------------------------------------------------------");
            Ui.pause();

            if (Enemy.get().getHealth() <= Enemy.get().getHealthMax() / 3){
                Enemy.get().useFirstAidKit();
            }
        }
        damageDealt = 0;
    }

    private void criticalHit() {

        if (wasCriticalHit()) {
            int critMultiplier = Random.RInt(this.critDamMultiplierMin, this.critDamMultiplierMax);

            damageDealt *= critMultiplier;

            Ui.cls();
            Ui.println("------------------------------------------------------------");
            Ui.println("  ★ 크리티컬 히트!");
            Ui.println("  " + critMultiplier + "배 데미지를 입혔습니다!");
            Ui.println("------------------------------------------------------------");
            Ui.pause();

        }
    }

    private void bulletCriticalHit() {

        if (bulletWasCriticalHit()) {

            damageDealt *= Weapon.BULLET_CRITICAL_MULTIPLIER;

            Ui.cls();
            Ui.println("------------------------------------------------------------");
            Ui.println("  ★ 크리티컬 총알 히트!");
            Ui.println("  총알이 " + Weapon.BULLET_CRITICAL_MULTIPLIER + "배 데미지를 입혔습니다!");
            Ui.println("------------------------------------------------------------");
            Ui.pause();

        }
    }

    private boolean wasCriticalHit() {
        return Random.RInt((int) (100 / this.critChanceMultiplier)) == 1;
    }

    private boolean bulletWasCriticalHit() {
        return Random.RInt((int) (100 / Weapon.BULLET_CRITICAL_CHANCE)) == 1;
    }

    public void viewAbout() {

        final int BORDER_LENGTH = 39;

        //Start of weapon Info
        Ui.cls();
        for (int i = 0; i < BORDER_LENGTH; i++) Ui.print("-");//Make line
        Ui.println();
        for (int i = 0; i < ((BORDER_LENGTH / 2) - (this.getName().length() / 2)); i++)
            Ui.print(" ");//Set correct spacing to get name in middle of box
        Ui.println(this.getName());
        Ui.println("가격:          " + this.price + " 코인");
        Ui.println("빗나갈 확률:   " + this.chanceOfMissing + "%");
        Ui.println("탄약 소모:     " + this.ammoUsed);
        Ui.println("데미지:        " + this.getDamage());
        Ui.println("크리티컬 확률: " + this.critChanceMultiplier + "%");
        Ui.println("크리티컬 배율: " + this.critDamMultiplierMin + "~" + this.critDamMultiplierMax + "x");
        if (!this.melee) {
            Ui.println("총알 크리티컬 확률: " + Weapon.BULLET_CRITICAL_CHANCE + "%");
            Ui.println("총알 크리티컬 배율: " + Weapon.BULLET_CRITICAL_MULTIPLIER + "x");
        }
        for (int i = 0; i < BORDER_LENGTH; i++) Ui.print("-");//Make line
        Ui.pause();
        Ui.cls();
        //End of weapon Info
    }

    private String getDamage() {
        if (this.melee) {
            return (this.damageMin + " - " + this.damageMax);
        } else {
            if (this.chanceOfMissing == 0) {
                return String.valueOf((BULLET_DAMAGE * this.ammoUsed));
            } else {
                return ("0 - " + String.valueOf((BULLET_DAMAGE * this.ammoUsed)));
            }
        }
    }

    public boolean isBuyable() {
        return this.buyable;
    }

    public void buy() {
        if (!isBuyable()) {
            Ui.msg("죄송합니다, 이 아이템은 현재 재고가 없습니다.");
            return;
        }
        if (this.owns()) {
            Ui.msg("이미 보유하고 있는 무기입니다.");
            return;
        }
        if (level > Xp.getLevel()) {
            Ui.msg("이 아이템을 구매하기 위한 레벨이 부족합니다.");
            return;
        }
        if (price > Coins.get()) {
            Ui.msg("코인이 부족합니다.");
            return;
        }

        //Buy
        Achievements.boughtItem = true;
        Coins.set(-price, true);
        Stats.coinsSpentOnWeapons += price;
        this.owns = true;
        current = this;
        Ui.println(this.getName() + "을(를) " + this.price + " 코인에 구매했습니다.");
        Ui.println("코인: " + Coins.get());
        Ui.pause();

        //Give ammo
        ammo += this.ammoIncludedWithPurchase;

    }

    public void buyAmmo() {

        Ui.cls();

        //Make sure player is a high enough level
        if (Xp.getLevel() < this.level) {
            Ui.println("레벨이 부족합니다. 최소 " + this.level + " 레벨이 필요합니다.");
            Ui.pause();
            return;
        }

        //Get amount of ammo user wants
        Ui.println("탄약을 몇 개 구매하시겠습니까?");
        Ui.println("탄약 1개당 " + this.ammoPrice + " 코인입니다.");
        Ui.println("현재 코인: " + Coins.get());
        int ammoToBuy = Ui.getValidInt();
        int cost = ammoToBuy * ammoPrice;

        //Make sure player has enough coins
        if (Coins.get() < (cost)) {
            Ui.println("코인이 부족합니다. " + (cost - Coins.get()) + " 코인이 더 필요합니다.");
            Ui.pause();
            return;
        }

        this.ammo += ammoToBuy;
        Coins.set(-cost, true);
        Stats.coinsSpentOnWeapons += cost;

        Ui.println("탄약 " + ammoToBuy + "개를 구매했습니다.");
        Ui.pause();
    }

    public int getAmmoPrice() {
        return this.ammoPrice;
    }

    @Override
    public int compareTo(Weapon w) {
        return Integer.compare(this.level, w.level);
    }
}