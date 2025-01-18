package org.poo.account;

public final class ClassicAccount extends Account {

    public ClassicAccount(final String iban, final double balance,
                          final String currency,
                          final String type,
                          final String plan) {
        super(iban, balance, currency, type, plan);
    }
}
