package org.poo.bank;

public final class Payment {
    private double amount;
    public  Payment(final double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }
}
