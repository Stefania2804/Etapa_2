package org.poo.strategy;

import org.poo.account.Account;

public final class OnlinePayment implements PayStrategy {

    @Override
    public void pay(final Account account, final double amount) {
            account.setBalance(account.getBalance() - amount);
    }
}
