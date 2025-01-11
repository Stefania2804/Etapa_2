package org.poo.strategy;

import org.poo.account.Account;

public final class BankTransfer implements PayStrategy {
    private Account receiver;
    private double exchangedAmount;
    private double exchangedToRon;
    public BankTransfer(final Account receiver, final double exchangedAmount,
                        final double exchangedToRon) {
        this.receiver = receiver;
        this.exchangedAmount = exchangedAmount;
        this.exchangedToRon = exchangedToRon;
    }

    @Override
    public void pay(final Account account, final double amount) {
        if (account.getPlan().equals("standard")) {
            account.setBalance(account.getBalance() - 0.002 * amount);
        } else if (account.getPlan().equals("silver") && exchangedToRon >= 500) {
            account.setBalance(account.getBalance() - 0.001 * amount);
        }
        account.setBalance(account.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + exchangedAmount);
    }
}
