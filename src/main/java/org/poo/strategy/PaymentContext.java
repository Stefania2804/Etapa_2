package org.poo.strategy;

import org.poo.account.Account;

public class PaymentContext {
    private PayStrategy strategy;

    public PaymentContext(final PayStrategy strategy) {
        this.strategy = strategy;
    }
    /**
     * Se face plata printr-o anumita metoda.
     *
     */
    public void executePayment(final Account account,
                               final double amount) {
        strategy.pay(account, amount);
    }
}
