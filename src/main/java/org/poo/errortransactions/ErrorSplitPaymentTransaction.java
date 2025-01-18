package org.poo.errortransactions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.poo.transactions.SplitPaymentTransaction;

import java.util.List;
@JsonPropertyOrder({"timestamp", "description", "currency", "amount", "involvedAccounts", "error"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ErrorSplitPaymentTransaction extends SplitPaymentTransaction {
    @JsonProperty("error")
    private String error;
    public ErrorSplitPaymentTransaction(final int timestamp, final String description,
                                        final String currency,
                                        final Double amount,
                                        final List<String> involvedAccounts,
                                        final String error,
                                        final String splitPaymentType,
                                        final List<Double> amountForUsers) {
        super(timestamp, description, splitPaymentType,
                currency, amount, amountForUsers, involvedAccounts);
        this.error = error;
    }
}
