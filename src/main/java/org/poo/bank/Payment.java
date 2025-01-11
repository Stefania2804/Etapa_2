package org.poo.bank;

public class Payment {
    private double amount;
    public  Payment (final double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
