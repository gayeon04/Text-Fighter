package com.hotmail.kalebmarc.textfighter.main;

import com.hotmail.kalebmarc.textfighter.player.Coins;
import com.hotmail.kalebmarc.textfighter.player.Stats;
import com.hotmail.kalebmarc.textfighter.player.Xp;

public class Bank {

    private static double interest;
    private static int balance;

    public static void menu() {

        int amount;

        //Makes sure user level 2
        if (Xp.getLevel() < 2) {
            Ui.msg("은행을 이용하려면 최소 레벨 2가 필요합니다.");
            return;
        }

        while (true) {

            Ui.cls();
            Ui.println("============================================================");
            Ui.println("  [ 은행 ]");
            Ui.println("============================================================");
            Ui.println("  코인을 예금하면 사망 시에도 안전하게 보관됩니다.");
            Ui.println("  단, 예금액의 " + (int)(interest * 100) + "% 를 수수료로 납부해야 합니다.");
            Ui.println("------------------------------------------------------------");
            Ui.println("  잔액:  " + get() + " 코인");
            Ui.println("  보유:  " + Coins.get() + " 코인");
            Ui.println("------------------------------------------------------------");
            Ui.println("  1) 예금   2) 인출");
            Ui.println("  3) 대출   4) 뒤로");
            Ui.println("============================================================");

            switch (Ui.getValidInt()) {
                case 1:
                    //-----------------------------------------------------------------------------------
                    if (Loan.hasLoan()) {
                        Ui.msg("대출을 모두 상환하기 전까지는 예금할 수 없습니다!");
                        break;
                    }
                    Ui.println("얼마를 예금하시겠습니까? (수수료 " + (int)(interest * 100) + "% 차감)");
                    Ui.println("현재 보유 코인: " + Coins.get());
                    do {
                        amount = Ui.getValidInt();
                        if (amount > Coins.get()) {
                            Ui.println("코인이 부족합니다. 현재 " + Coins.get() + " 코인을 보유하고 있습니다.");
                            amount = -1;
                        }
                    } while (amount < 0);
                    if (amount == 0) return;

                    //Deposit
                    deposit(amount, interest);
                    //-----------------------------------------------------------------------------------
                    break;
                case 2:
                    //-----------------------------------------------------------------------------------
                    Ui.cls();

                    //Input
                    Ui.println("얼마를 인출하시겠습니까?");
                    Ui.println("현재 잔액: " + get() + " 코인");
                    do {
                        amount = Ui.getValidInt();
                        if (amount > get()) {
                            Ui.println("잔액이 부족합니다. 현재 잔액: " + get() + " 코인");
                            amount = -1;
                        }
                    } while (amount < 0);

                    //Withdraw
                    withdraw(amount);
                    //-----------------------------------------------------------------------------------
                    break;
                case 3:
                    Loan.menu();
                    break;
                case 4:
                    return;
            }
        }
    }

    public static int get() {
        return balance;
    }

    public static void set(int amount, boolean add) {
        if (!add) {
            balance = amount;
        } else {
            balance += amount;
            if (balance < 0) balance = 0;
        }
    }

    public static void setInterest(double price) {
        interest = price;
    }

    private static void withdraw(int amount) {
        //Calculation
        Coins.set(amount, true);
        set(-amount, true);

        //Result
        Ui.cls();
        Ui.println("인출 금액: " + amount + " 코인");
        Ui.println("현재 잔액: " + get() + " 코인");
        Ui.pause();
    }

    private static void deposit(int amount, double interest) {

        //Get interest
        interest = interest * amount;
        if (amount < 10) interest = 1;

        //Take coins from player
        Coins.set(-amount, true);

        //Take away interest amount
        amount -= Math.round(interest);
        Stats.totalCoinsSpent += Math.round(interest);
        Stats.coinsSpentOnBankInterest += Math.round(interest);

        //Add remaining coins to bank account
        set(amount, true);

        //Display
        Ui.cls();
        Ui.println("예금 금액:    " + amount + " 코인");
        Ui.println("납부 수수료:  " + Math.round(interest) + " 코인");
        Ui.println("현재 잔액:   " + get() + " 코인");
        Ui.pause();
    }
}
