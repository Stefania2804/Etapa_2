package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"timestamp", "description", "amount"})
public final class CashWithdrawalTransaction extends Transaction {
    @JsonProperty("amount")
    private double amount;

    public CashWithdrawalTransaction(final int timestamp,
                                      final String description, final double amount) {
        super(timestamp, description);
        this.amount = amount;
    }
}
