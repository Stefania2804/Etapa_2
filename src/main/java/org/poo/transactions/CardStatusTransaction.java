package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"timestamp", "description"})
public final class CardStatusTransaction extends Transaction {
    public CardStatusTransaction(final int timestamp,
                                 final String description) {
        super(timestamp, description);
    }
}
