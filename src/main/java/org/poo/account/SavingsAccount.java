package org.poo.account;

import com.fasterxml.jackson.annotation.JsonIgnore;


public final class SavingsAccount extends Account {
    @JsonIgnore
    private double interestRate;

    public SavingsAccount(final String iban, final double balance,
                          final String currency,
                          final String type,
                          final double interestRate,
                          final String plan) {
        super(iban, balance, currency, type, plan);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(final double interestRate) {
        this.interestRate = interestRate;
    }

}
