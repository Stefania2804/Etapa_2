package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
@JsonPropertyOrder({"timestamp", "description", "currency",
        "amount", "involvedAccounts"})
public class SplitPaymentTransaction extends Transaction {
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("amount")
    private double amount;
    @JsonProperty("involvedAccounts")
    private List<String> involvedAccounts;

    public SplitPaymentTransaction(final int timestamp, final String description,
                                   final String currency, final double amount,
                                   final List<String> involvedAccounts) {
        super(timestamp, description);
        this.currency = currency;
        this.amount = amount;
        this.involvedAccounts = involvedAccounts;
    }
}
