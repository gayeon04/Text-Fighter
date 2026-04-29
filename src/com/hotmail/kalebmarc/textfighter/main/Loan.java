package com.hotmail.kalebmarc.textfighter.main;

import com.hotmail.kalebmarc.textfighter.player.Coins;
import com.hotmail.kalebmarc.textfighter.player.Xp;

public class Loan {

    private static final double INTEREST_RATE = 0.15;
    private static int currentLoan;
    private static int netDue;

    public static void menu() {
        while (true) {
            Ui.cls();
            Ui.println("============================================================");
            Ui.println("  [ 대출 ]");
            Ui.println("============================================================");
            Ui.println("  이자율:    " + (int)(INTEREST_RATE * 100) + "%");
            Ui.println("  대출 한도: " + getMaxLoan() + " 코인");
            Ui.println("  대출 잔액: " + currentLoan + " 코인");
            Ui.println("------------------------------------------------------------");
            Ui.println("  원금:    " + netDue + " 코인");
            Ui.println("  이자:    " + (int)(netDue * INTEREST_RATE) + " 코인");
            Ui.println("  총 납부: " + getGrossDue() + " 코인");
            Ui.println("------------------------------------------------------------");
            Ui.println("  1) 대출 받기   2) 대출 상환   3) 뒤로");
            Ui.println("============================================================");
            switch (Ui.getValidInt()) {
                case 1:
                    createLoan();
                    break;
                case 2:
                    payLoan();
                    break;
                case 3:
                    return;
            }
        }
    }

    private static void createLoan() {

        if (hasLoan()) {
            Ui.msg("기존 대출이 있는 동안에는 새로운 대출을 받을 수 없습니다.");
            return;
        }

        Ui.cls();
        Ui.println("대출받을 금액을 입력하세요.");
        Ui.println("최대 대출 가능 금액: " + getMaxLoan() + " 코인");
        int request = Ui.getValidInt();

        if (request > getMaxLoan()) {
            Ui.msg("최대 대출 가능 금액은 " + getMaxLoan() + " 코인입니다!");
            return;
        }
        if (request <= 0) {
            Ui.msg("1 코인 이상 입력해야 합니다.");
            return;
        }

        //Give loan
        Coins.set(request, true);
        currentLoan = request;
        netDue = request;
        Ui.cls();
        Ui.println(request + " 코인을 대출받았습니다.");
        Ui.println("대출을 완전히 상환하기 전까지는 예금할 수 없습니다.");
        Ui.pause();
    }

    private static void payLoan() {
        if (getGrossDue() == 0) {
            Ui.println("상환할 대출이 없습니다.");
            Ui.pause();
            return;
        }

        Ui.cls();
        Ui.println("총 납부액: " + getGrossDue() + " 코인   보유 코인: " + Coins.get());
        Ui.println("대출을 완전히 상환하기 전까지는 예금할 수 없습니다.");
        Ui.println("얼마를 상환하시겠습니까?");
        int amountToPay = Ui.getValidInt();

        Ui.cls();
        if (Coins.get() < amountToPay) {
            Ui.println("코인이 부족합니다.");
            Ui.pause();
            return;
        }

        if (amountToPay <= 0) {
            Ui.println("1 코인 이상 입력해야 합니다.");
            Ui.pause();
            return;
        }

        if (amountToPay > getGrossDue()) {
            Ui.println("총 납부액은 " + getGrossDue() + " 코인입니다. 더 작은 금액을 입력하세요.");
            Ui.pause();
        }
        netDue = getGrossDue() - amountToPay;
        Coins.set(-amountToPay, true);

        Ui.println(amountToPay + " 코인을 상환했습니다.\n남은 납부액: " + getGrossDue() + " 코인");
        if (getGrossDue() == 0) currentLoan = 0;
        Ui.pause();
    }

    private static int getMaxLoan() {
        return Xp.getLevel() * 100;
    }

    public static int getCurrentLoan() {
        return currentLoan;
    }

    public static void setCurrentLoan(int loan) {
        currentLoan = loan;
    }

    public static boolean hasLoan() {
        return getCurrentLoan() > 0;
    }

    public static int getGrossDue() {
        return (int) (netDue + (netDue * INTEREST_RATE));
    }

    public static int getNetDue() {
        return netDue;
    }

    public static void setNetDue(int due) {
        netDue = due;
    }
}
