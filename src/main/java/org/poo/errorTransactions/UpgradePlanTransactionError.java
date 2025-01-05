package org.poo.errorTransactions;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.poo.transactions.Transaction;
@JsonPropertyOrder({"timestamp", "description"})

public class UpgradePlanTransactionError extends Transaction {
    public UpgradePlanTransactionError(final int timestamp,
                                   final String description) {
        super(timestamp, description);
    }
}
