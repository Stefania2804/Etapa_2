package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"timestamp", "description", "amount", "commerciant"})
public final class OnlinePayTransaction extends Transaction {
    @JsonProperty("amount")
    private double amount;
    @JsonProperty("commerciant")
    private String commerciant;

    public OnlinePayTransaction(final int timestamp, final String description,
                                final double amount, final String commerciant) {
        super(timestamp, description);
        this.amount = amount;
        this.commerciant = commerciant;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public String getCommerciant() {
        return commerciant;
    }

    public void setCommerciant(final String commerciant) {
        this.commerciant = commerciant;
    }
}
