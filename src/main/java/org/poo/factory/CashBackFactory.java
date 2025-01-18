package org.poo.factory;

public final class CashBackFactory {
    /**
     * Instantiere cashback.
     *
     */
    public static CashBack getCashBack(final String cashBackType) {
        if (cashBackType.equals("nrOfTransactions")) {
            return new NrOfTransactions();
        } else if (cashBackType.equals("spendingThreshold")) {
            return new SpendingThreshold();
        }
        throw new IllegalArgumentException("Invalid cashback type");
    }
}
