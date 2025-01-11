package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"timestamp", "description", "savingsAccountIBAN", "classicAccountIBAN",
        "amount"})
public class WithDrawSavingsTransaction extends Transaction {
    @JsonProperty("savingsAccountIBAN")
    private String savingsAccount;
    @JsonProperty("classicAccountIBAN")
    private String classicAccount;
    @JsonProperty("amount")
    private double amount;

    public WithDrawSavingsTransaction(final int timestamp,
                                      final String description,
                                      final String savingsAccount,
                                      final String classicAccount, final double amount) {
        super(timestamp, description);
        this.savingsAccount = savingsAccount;
        this.classicAccount = classicAccount;
        this.amount = amount;
    }
}
