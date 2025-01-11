package org.poo.strategy;

import org.poo.account.Account;

public final class OnlinePayment implements PayStrategy {
    private double exchangedAmount;
    public OnlinePayment(final double exchangedAmount) {
        this.exchangedAmount = exchangedAmount;
    }
    @Override
    public void pay(final Account account, final double amount) {
        if (account.getIban().equals("RO00POOB5687892910835215")) {
            System.out.println("plata de " + amount);
        }
        if (account.getPlan().equals("standard")) {
            account.setBalance(account.getBalance() - 0.002 * amount);
            if (account.getIban().equals("RO00POOB5687892910835215")) {
                System.out.println("comision de " + amount * 0.002);
            }
        }else if (account.getPlan().equals("silver") && exchangedAmount >= 500) {
            account.setBalance(account.getBalance() - 0.001 * amount);
            if (account.getIban().equals("RO00POOB5687892910835215")) {
                System.out.println("comision de " + amount * 0.001);
            }
        }
        account.setBalance(account.getBalance() - amount);
        if (account.getIban().equals("RO00POOB5687892910835215")) {
            System.out.println("balanta dupa plata de " + amount + " " + account.getCurrency() +  " este " + account.getBalance() + " " + account.getCurrency());
        }
    }
}
