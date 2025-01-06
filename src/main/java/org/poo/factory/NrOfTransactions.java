package org.poo.factory;

import org.poo.account.Account;
import org.poo.account.Commerciant;
import org.poo.bank.InfoBank;

public class NrOfTransactions implements CashBack {
    public void calculate(InfoBank infoBank, Account account, double amount, Commerciant currentCommerciant) {
        for (Commerciant commerciant : account.getCommerciants()) {
            if (commerciant.getCashBackType().equals("nrOfTransactions")) {
                int nrOfTr = commerciant.getNrOfTransactions();
                if (nrOfTr > 2 && account.isReceivedFood() == false && currentCommerciant.getType().equals("Food")) {
                    account.setBalance(account.getBalance() + 0.02 * amount);
                    account.setReceivedFood(true);
                }
                if (nrOfTr > 5 && account.isReceivedClothes() == false && currentCommerciant.getType().equals("Clothes")) {
                    account.setBalance(account.getBalance() + 0.05 * amount);
                    account.setReceivedClothes(true);
                }
                if (nrOfTr > 10 && account.isReceivedTech() == false && currentCommerciant.getType().equals("Tech")) {
                    account.setBalance(account.getBalance() + 0.1 * amount);
                    account.setReceivedTech(true);
                }
                account.setBalance(Math.round(account.getBalance() * 100.000) / 100.00);
            }
        }
    }
}
