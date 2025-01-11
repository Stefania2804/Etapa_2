package org.poo.factory;

import org.poo.account.Account;
import org.poo.account.Commerciant;
import org.poo.bank.InfoBank;

public class SpendingThreshold implements CashBack {
    public void calculate(InfoBank infoBank, Account account, double amount, Commerciant currentCommerciant) {
        double exchangedAmount = currentCommerciant.getMoneySpent();
        if (account.getIban().equals("RO00POOB5687892910835215")) {
            System.out.println(exchangedAmount + " RON cheltuiti la " + currentCommerciant.getName());
        }
        if (exchangedAmount >= 100 && exchangedAmount < 300) {
            if (account.getPlan().equals("standard") || account.getPlan().equals("student")) {
                account.setBalance(account.getBalance() + 0.001 * amount);
                if (account.getIban().equals("RO00POOB5687892910835215")) {
                    System.out.println("cashback silver pentru " + " " + amount + " " + "rezulta balanta de" + account.getBalance());
                }
            } else if (account.getPlan().equals("silver")) {
                if (account.getIban().equals("RO00POOB5687892910835215")) {
                    System.out.println("cashback silver pentru " + " " + amount + " " + "rezulta balanta de" + account.getBalance());
                }
                account.setBalance(account.getBalance() + 0.003 * amount);
            } else {
                account.setBalance(account.getBalance() + 0.005 * amount);
            }
        } else if (exchangedAmount >= 300 && exchangedAmount < 500) {
            if (account.getPlan().equals("standard") || account.getPlan().equals("student")) {
                account.setBalance(account.getBalance() + 0.002 * amount);
            } else if (account.getPlan().equals("silver")) {
                account.setBalance(account.getBalance() + 0.004 * amount);
                if (account.getIban().equals("RO00POOB5687892910835215")) {
                    System.out.println("cashback silver pentru " + " " + amount + " " + "rezulta balanta de" + account.getBalance());
                }
            } else {
                account.setBalance(account.getBalance() + 0.0055 * amount);
            }
        } else if (exchangedAmount >= 500){
            if (account.getPlan().equals("standard") || account.getPlan().equals("student")) {
                account.setBalance(account.getBalance() + 0.0025 * amount);
            } else if (account.getPlan().equals("silver")) {
                account.setBalance(account.getBalance() + 0.005 * amount);
                if (account.getIban().equals("RO00POOB5687892910835215")) {
                    System.out.println("cashback silver pentru " + " " + amount + " " + "rezulta balanta de" + account.getBalance());
                }
            } else {
                account.setBalance(account.getBalance() + 0.007 * amount);
            }
        }
    }
}
