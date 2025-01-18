package org.poo.account;

public final class Pair {
    private double amount;
    private String email;
    private String transaction;
    private String commerciant;

    public Pair(final double amount,
                final String email,
                final String transaction) {
        this.amount = amount;
        this.email = email;
        this.transaction = transaction;
        commerciant = null;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(final String transaction) {
        this.transaction = transaction;
    }

    public String getCommerciant() {
        return commerciant;
    }

    public void setCommerciant(final String commerciant) {
        this.commerciant = commerciant;
    }
}
