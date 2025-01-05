package org.poo.factory;

import org.poo.account.Account;
import org.poo.account.Commerciant;
import org.poo.bank.InfoBank;

public interface CashBack {
    void calculate(InfoBank infoBank, Account account, double amount, Commerciant currentCommerciant);
}
