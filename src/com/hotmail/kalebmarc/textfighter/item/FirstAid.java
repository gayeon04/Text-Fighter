package com.hotmail.kalebmarc.textfighter.item;

import com.hotmail.kalebmarc.textfighter.main.Ui;
import com.hotmail.kalebmarc.textfighter.player.Coins;
import com.hotmail.kalebmarc.textfighter.player.Health;
import com.hotmail.kalebmarc.textfighter.player.Stats;
import com.hotmail.kalebmarc.textfighter.player.Xp;

public class FirstAid {
    public static int used = 0;
    public static int price;
    public static int level;
    private static int firstAid;

    private FirstAid() {
    }

    public static int get() {
        return firstAid;
    }

    public static void set(int amount, boolean add) {
        if (!add) {
            firstAid = amount;
        } else {
            firstAid += amount;
            if (firstAid < 0) firstAid = 0;
        }
    }

    public static void use() {

        Ui.cls();

        if (get() <= 0) {

            Ui.println("------------------------------------------------------------");
            Ui.println("  응급 치료 키트가 없습니다!");
            Ui.println("  상점에서 구매하세요.");
            Ui.println("------------------------------------------------------------");
            Ui.pause();

        } else if (Health.get() == 100) {

            Ui.println("------------------------------------------------------------");
            Ui.println("  이미 체력이 가득 찼습니다!");
            Ui.println("  응급 치료 키트가 필요하지 않습니다!");
            Ui.println("------------------------------------------------------------");
            Ui.println("  내 체력: " + Health.getStr());
            Ui.println("  응급 키트: " + get() + "개");
            Ui.println("------------------------------------------------------------");
            Ui.pause();

        } else {

            set(-1, true);
            Health.gain(20);
            used++;

            Ui.println("------------------------------------------------------------");
            Ui.println("  응급 치료 키트를 사용했습니다.");
            Ui.println("  체력 20 회복!");
            Ui.println("------------------------------------------------------------");
            Ui.println("  내 체력: " + Health.getStr());
            Ui.println("  응급 키트: " + get() + "개");
            Ui.println("------------------------------------------------------------");
            Ui.pause();

        }

    }

    public static void buy() {
        if (Xp.getLevel() < level) {
            Ui.println("구매하려면 최소 레벨 " + level + " 이 필요합니다!");
            Ui.pause();
        } else if (price <= Coins.get()) {
            Coins.set(-price, true);
            Stats.coinsSpentOnHealth += price;
            set(1, true);
            Ui.println("구매해 주셔서 감사합니다!");
            Ui.pause();
        } else {
            Ui.println("코인이 부족합니다.");
            Ui.pause();
        }
    }
}
