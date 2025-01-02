package org.poo.strategy;

import org.poo.account.Account;

public final class BankTransfer implements PayStrategy {
    private Account receiver;
    private double exchangedAmount;

    public BankTransfer(final Account receiver, final double exchangedAmount) {
        this.receiver = receiver;
        this.exchangedAmount = exchangedAmount;
    }

    @Override
    public void pay(final Account account, final double amount) {
            account.setBalance(account.getBalance() - amount);

            receiver.setBalance(receiver.getBalance() + exchangedAmount);
    }
}
