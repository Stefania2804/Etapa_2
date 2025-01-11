package org.poo.errortransactions;

import org.poo.transactions.Transaction;

public class WithDrawSavingsErrorTransaction extends Transaction {
    public WithDrawSavingsErrorTransaction(final int timestamp,
                                   final String description) {
        super(timestamp, description);
    }
}
