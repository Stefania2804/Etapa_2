package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"timestamp", "description"})

public final class ChangeIrTransaction extends Transaction {
    public ChangeIrTransaction(final int timestamp,
                               final String description) {
        super(timestamp, description);
    }
}
