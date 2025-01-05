package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
@JsonPropertyOrder({"timestamp", "description", "splitPaymentType", "currency",
        "amountForUsers", "involvedAccounts"})
public class SplitPaymentTransaction extends Transaction {
    @JsonProperty("splitPaymentType")
    private String splitPaymentType;
    @JsonProperty("currency")
    private String currency;
    @JsonIgnore
    private double amount;
    @JsonProperty("amountForUsers")
    private List<Double> amountForUsers;
    @JsonProperty("involvedAccounts")
    private List<String> involvedAccounts;

    public SplitPaymentTransaction(final int timestamp, final String description,
                                   final String splitPaymentType,
                                   final String currency, final double amount,
                                   final List<Double> amountForUsers,
                                   final List<String> involvedAccounts) {
        super(timestamp, description);
        this.currency = currency;
        this.amount = amount;
        this.involvedAccounts = involvedAccounts;
        this.splitPaymentType = splitPaymentType;
        this.amountForUsers = amountForUsers;
    }
}
