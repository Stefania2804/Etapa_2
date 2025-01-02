package org.poo.account;

import com.fasterxml.jackson.annotation.JsonIgnore;


public final class Savings extends Account {
    @JsonIgnore
    private double interestRate;

    public Savings(final String iban, final double balance,
                   final String currency,
                   final String type,
                   final double interestRate) {
        super(iban, balance, currency, type);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(final double interestRate) {
        this.interestRate = interestRate;
    }

}
