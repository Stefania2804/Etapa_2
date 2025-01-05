package org.poo.bank;

import lombok.Data;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data

public final class SplitPayment {
    private int timestamp;
    private String currency;
    private double amount;
    private List<Double> amountForUsers;
    private List<String> accounts;
    private String splitPaymentType;
    private int accepts;
    private int rejects;

    public SplitPayment(final CommandInput commandInput) {
        this.timestamp = commandInput.getTimestamp();
        this.currency = commandInput.getCurrency();
        this.amount = commandInput.getAmount();
        this.amountForUsers = commandInput.getAmountForUsers();
        this.accounts = commandInput.getAccounts();
        this.splitPaymentType = commandInput.getSplitPaymentType();
        accepts = 0;
        rejects = 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        SplitPayment that = (SplitPayment) obj;
        return timestamp == that.timestamp
                && Double.compare(amount, that.amount) == 0
                && accepts == that.accepts
                && rejects == that.rejects
                && Objects.equals(currency, that.currency)
                && (amountForUsers == null ? that.amountForUsers == null : amountForUsers.equals(that.amountForUsers))
                && Objects.equals(accounts, that.accounts)
                && Objects.equals(splitPaymentType, that.splitPaymentType);
    }
}