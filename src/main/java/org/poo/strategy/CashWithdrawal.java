package org.poo.strategy;

import org.poo.account.Account;
import org.poo.transactions.CashWithdrawalTransaction;

public class CashWithdrawal implements PayStrategy {
    private double exchangedToRon;
    public CashWithdrawal(final double exchangedToRon) {
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
    }
}
