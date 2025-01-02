package org.poo.errorTransactions;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.poo.transactions.Transaction;

@JsonPropertyOrder({"timestamp", "description"})
public final class ErrorPaymentTransaction extends Transaction {

    public ErrorPaymentTransaction(final int timestamp,
                                   final String description) {
        super(timestamp, description);
    }
}
