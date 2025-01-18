package org.poo.factory;

import org.poo.account.Account;
import org.poo.account.Commerciant;
import org.poo.bank.InfoBank;
import org.poo.main.Constants;

public final class NrOfTransactions implements CashBack {
    /**
     * calculare si adaugare cashback pentru nrOfTransactions.
     *
     */
    public void calculate(final InfoBank infoBank, final Account account,
                          final double amount, final Commerciant currentCommerciant) {
        int nrOfTr = 0;
        for (Commerciant commerciant : account.getCommerciants()) {
            if (commerciant.getCashBackType().equals("nrOfTransactions")) {
                nrOfTr = commerciant.getNrOfTransactions();
                if (currentCommerciant.getCashBackType().equals("spendingThreshold")) {
                    if (nrOfTr >= Constants.FOODLIMIT.getValue()
                            && !account.isReceivedFood()
                            && currentCommerciant.getType().equals("Food")) {
                        account.setBalance(account.getBalance()
                                + Constants.CASHBACK2.getValue() * amount);
                        account.setReceivedFood(true);
                    }
                    if (nrOfTr >= Constants.CLOTHESLIMIT.getValue()
                            && !account.isReceivedClothes()
                            && currentCommerciant.getType().equals("Clothes")) {
                        account.setBalance(account.getBalance()
                                + Constants.CASHBACK5.getValue() * amount);
                        account.setReceivedClothes(true);
                    }
                    if (nrOfTr >= Constants.TECHLIMIT.getValue()
                            && !account.isReceivedTech()
                            && currentCommerciant.getType().equals("Tech")) {
                        account.setBalance(account.getBalance()
                                + Constants.CASHBACK10.getValue() * amount);
                        account.setReceivedTech(true);
                    }
                } else {
                    if (nrOfTr > Constants.FOODLIMIT.getValue()
                            && !account.isReceivedFood()
                            && currentCommerciant.getType().equals("Food")) {
                        account.setBalance(account.getBalance()
                                + Constants.CASHBACK2.getValue() * amount);
                        account.setReceivedFood(true);
                    }
                    if (nrOfTr > Constants.CLOTHESLIMIT.getValue()
                            && !account.isReceivedClothes()
                            && currentCommerciant.getType().equals("Clothes")) {
                        account.setBalance(account.getBalance()
                                + Constants.CASHBACK5.getValue() * amount);
                        account.setReceivedClothes(true);
                    }
                    if (nrOfTr > Constants.TECHLIMIT.getValue()
                            && !account.isReceivedTech()
                            && currentCommerciant.getType().equals("Tech")) {
                        account.setBalance(account.getBalance()
                                + Constants.CASHBACK10.getValue() * amount);
                        account.setReceivedTech(true);
                    }
                }
            }
        }
    }
}
