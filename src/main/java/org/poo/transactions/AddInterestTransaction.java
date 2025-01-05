package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"timestamp", "description", "amount", "currency"})
public final class AddInterestTransaction extends Transaction {
    @JsonProperty("amount")
    private double amount;
    @JsonProperty("currency")
    private String currency;
    public AddInterestTransaction(final int timestamp,
                                  final String description, final String currency,
                                  final double amount) {
        super(timestamp, description);
        this.amount = amount;
        this.currency = currency;
    }
}
