package org.poo.factory;

import org.poo.account.Account;
import org.poo.account.Commerciant;
import org.poo.bank.InfoBank;

public class SpendingThreshold implements CashBack {
    public void calculate(InfoBank infoBank, Account account, double amount, Commerciant currentCommerciant) {
        double exchangedAmount = currentCommerciant.getMoneySpent();
        if (exchangedAmount >= 100 && exchangedAmount < 300) {
            if (account.getPlan().equals("standard") || account.getPlan().equals("student")) {
                account.setBalance(account.getBalance() + 0.001 * amount);
                account.setBalance(Math.round(account.getBalance() * 100.0) / 100.0);
                currentCommerciant.setMoneySpent(0.0);
            } else if (account.getPlan().equals("silver")) {
                account.setBalance(account.getBalance() + 0.003 * amount);
                account.setBalance(Math.round(account.getBalance() * 100.0) / 100.0);
                currentCommerciant.setMoneySpent(0.0);
            } else {
                account.setBalance(account.getBalance() + 0.005 * amount);
                account.setBalance(Math.round(account.getBalance() * 100.0) / 100.0);
                currentCommerciant.setMoneySpent(0.0);
            }
        } else if (exchangedAmount >= 300 && exchangedAmount < 500) {
            if (account.getPlan().equals("standard") || account.getPlan().equals("student")) {
                account.setBalance(account.getBalance() + 0.002 * amount);
                account.setBalance(Math.round(account.getBalance() * 100.0) / 100.0);
                currentCommerciant.setMoneySpent(0.0);
            } else if (account.getPlan().equals("silver")) {
                account.setBalance(account.getBalance() + 0.004 * amount);
                account.setBalance(Math.round(account.getBalance() * 100.0) / 100.0);
                currentCommerciant.setMoneySpent(0.0);
            } else {
                account.setBalance(account.getBalance() + 0.0055 * amount);
                account.setBalance(Math.round(account.getBalance() * 100.0) / 100.0);
                currentCommerciant.setMoneySpent(0.0);
            }
        } else if (exchangedAmount >= 500){
            if (account.getPlan().equals("standard") || account.getPlan().equals("student")) {
                account.setBalance(account.getBalance() + 0.0025 * amount);
                account.setBalance(Math.round(account.getBalance() * 100.0) / 100.0);
                currentCommerciant.setMoneySpent(0.0);
            } else if (account.getPlan().equals("silver")) {
                account.setBalance(account.getBalance() + 0.005 * amount);
                account.setBalance(Math.round(account.getBalance() * 100.0) / 100.0);
                currentCommerciant.setMoneySpent(0.0);
            } else {
                account.setBalance(account.getBalance() + 0.007 * amount);
                account.setBalance(Math.round(account.getBalance() * 100.0) / 100.0);
                currentCommerciant.setMoneySpent(0.0);
            }
        }
    }
}
