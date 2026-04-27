package com.hotmail.kalebmarc.textfighter.player;

public class Coins {

    private static int coins;
    private static int bank;

    public static final int LOTTERY_THRESHOLD = 1000; // 적절한 값으로

    private Coins() {
    }

    public static int get() {
        return coins;
    }

    public static void set(int amount, boolean add) {
        if (!add) {
            coins = amount;
        } else {
            coins += amount;
            if (amount < 0) Stats.totalCoinsSpent += -amount;
            if (coins < 0) coins = 0;
        }
    }

}