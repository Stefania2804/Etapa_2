package org.poo.errorTransactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.poo.transactions.SplitPaymentTransaction;

import java.util.List;
@JsonPropertyOrder({"timestamp", "description", "currency", "amount", "involvedAccounts", "error"})
public final class ErrorSplitPaymentTransaction extends SplitPaymentTransaction {
    @JsonProperty("error")
    private String error;
    public ErrorSplitPaymentTransaction(final int timestamp, final String description,
                                        final String currency,
                                        final double amount,
                                        final List<String> involvedAccounts,
                                        final String error) {
        super(timestamp, description, currency, amount, involvedAccounts);
        this.error = error;
    }
}
