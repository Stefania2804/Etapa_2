package org.poo.strategy;

import org.poo.account.Account;
import org.poo.main.Constants;

public final class CashWithdrawal implements PayStrategy {
    private double exchangedToRon;
    public CashWithdrawal(final double exchangedToRon) {
        this.exchangedToRon = exchangedToRon;
    }
    @Override
    public void pay(final Account account, final double amount) {
        if (account.getPlan().equals("standard")) {
            account.setBalance(account.getBalance()
                    - Constants.COMISION02.getValue() * amount);
        } else if (account.getPlan().equals("silver")
                && exchangedToRon >= Constants.COMISIONLIMIT.getValue()) {
            account.setBalance(account.getBalance()
                    - Constants.COMISION01.getValue() * amount);
        }
        account.setBalance(account.getBalance() - amount);
    }
}
