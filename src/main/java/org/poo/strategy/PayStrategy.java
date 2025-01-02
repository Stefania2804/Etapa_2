package org.poo.strategy;

import org.poo.account.Account;

public interface PayStrategy {
    /**
     * Functia specifica fiecarei metode de plata.
     */
    void pay(Account account, double amount);
}
