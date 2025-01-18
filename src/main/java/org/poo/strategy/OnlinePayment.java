package org.poo.strategy;

import org.poo.account.Account;
import org.poo.main.Constants;

public final class OnlinePayment implements PayStrategy {
    private double exchangedAmount;
    public OnlinePayment(final double exchangedAmount) {
        this.exchangedAmount = exchangedAmount;
    }
    @Override
    public void pay(final Account account, final double amount) {
        if (account.getPlan().equals("standard")) {
            account.setBalance(account.getBalance()
                    - Constants.COMISION02.getValue() * amount);
        } else if (account.getPlan().equals("silver")
                && exchangedAmount >= Constants.COMISIONLIMIT.getValue()) {
            account.setBalance(account.getBalance()
                    - Constants.COMISION01.getValue() * amount);
        }
        account.setBalance(account.getBalance() - amount);
    }
}
