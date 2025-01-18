package org.poo.factory;

import org.poo.account.Account;
import org.poo.account.Commerciant;
import org.poo.bank.InfoBank;
import org.poo.main.Constants;

public final class SpendingThreshold implements CashBack {
    /**
     * Calculeaza si adauga cashback-ul pentru spendingThreshold.
     *
     */
    public void calculate(final InfoBank infoBank, final Account account,
                          final double amount,
                          final Commerciant currentCommerciant) {
        double exchangedAmount = 0.0;
        for (Commerciant commerciant : account.getCommerciants()) {
            if (commerciant.getCashBackType().equals("spendingThreshold")) {
                exchangedAmount = exchangedAmount + commerciant.getMoneySpent();
            }
        }
        if (exchangedAmount >= Constants.FIRSTTHRESHOLD.getValue()
                && exchangedAmount < Constants.SECONDTHRESHOLD.getValue()) {
            if (account.getPlan().equals("standard") || account.getPlan().equals("student")) {
                account.setBalance(account.getBalance()
                        + Constants.CASHBACK01.getValue() * amount);
            } else if (account.getPlan().equals("silver")) {
                account.setBalance(account.getBalance()
                        + Constants.CASHBACK03.getValue() * amount);
            } else {
                account.setBalance(account.getBalance()
                        + Constants.CASHBACK05.getValue() * amount);
            }
        } else if (exchangedAmount >= Constants.SECONDTHRESHOLD.getValue()
                && exchangedAmount < Constants.THIRDTHRESHOLD.getValue()) {
            if (account.getPlan().equals("standard") || account.getPlan().equals("student")) {
                account.setBalance(account.getBalance()
                        + Constants.CASHBACK02.getValue() * amount);
            } else if (account.getPlan().equals("silver")) {
                account.setBalance(account.getBalance()
                        + Constants.CASHBACK04.getValue() * amount);
            } else {
                account.setBalance(account.getBalance()
                        + Constants.CASHBACK055.getValue() * amount);
            }
        } else if (exchangedAmount >= Constants.THIRDTHRESHOLD.getValue()) {
            if (account.getPlan().equals("standard") || account.getPlan().equals("student")) {
                account.setBalance(account.getBalance()
                        + Constants.CASHBACK025.getValue() * amount);
            } else if (account.getPlan().equals("silver")) {
                account.setBalance(account.getBalance()
                        + Constants.CASHBACK05.getValue() * amount);
            } else {
                account.setBalance(account.getBalance()
                        + Constants.CASHBACK07.getValue() * amount);
            }
        }
    }
}
