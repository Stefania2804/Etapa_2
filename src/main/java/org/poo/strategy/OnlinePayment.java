package org.poo.strategy;

import org.poo.account.Account;

public final class OnlinePayment implements PayStrategy {
    private double exchangedAmount;
    public OnlinePayment(final double exchangedAmount) {
        this.exchangedAmount = exchangedAmount;
    }
    @Override
    public void pay(final Account account, final double amount) {
        if (account.getPlan().equals("standard")) {
            account.setBalance(account.getBalance() - 0.002 * amount);
        }else if (account.getPlan().equals("silver") && exchangedAmount >= 500) {
            account.setBalance(account.getBalance() - 0.001 * amount);
        }
        account.setBalance(account.getBalance() - amount);
        account.setBalance(Math.round(account.getBalance() * 100.0) / 100.0);
    }
}
