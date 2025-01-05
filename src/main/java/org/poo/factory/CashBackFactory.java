package org.poo.factory;

public class CashBackFactory {
    public static CashBack getCashBack(String cashBackType) {
        if (cashBackType.equals("nrOfTransactions")) {
            return new NrOfTransactions();
        } else if (cashBackType.equals("spendingThreshold")) {
            return new SpendingThreshold();
        }
        throw new IllegalArgumentException("Invalid cashback type");
    }
}
