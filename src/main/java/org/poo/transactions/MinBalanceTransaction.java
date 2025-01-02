package org.poo.transactions;

public final class MinBalanceTransaction extends Transaction {

    public MinBalanceTransaction(final int timestamp,
                                 final String description) {
        super(timestamp, description);
    }
}
