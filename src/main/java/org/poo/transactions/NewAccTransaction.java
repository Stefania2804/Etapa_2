package org.poo.transactions;

public final class NewAccTransaction extends Transaction {

    public NewAccTransaction(final int timestamp,
                             final String description) {
        super(timestamp, description);
    }
}
