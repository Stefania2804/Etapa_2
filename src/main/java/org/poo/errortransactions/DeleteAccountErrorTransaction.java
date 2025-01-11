package org.poo.errortransactions;

import org.poo.transactions.Transaction;

public final class DeleteAccountErrorTransaction extends Transaction {
    public DeleteAccountErrorTransaction(final int timestamp,
                                         final String description) {
        super(timestamp, description);
    }
}
