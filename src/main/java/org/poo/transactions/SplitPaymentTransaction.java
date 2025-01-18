package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
@JsonPropertyOrder({"timestamp", "description", "splitPaymentType", "currency",
        "amountForUsers", "involvedAccounts"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SplitPaymentTransaction extends Transaction {
    @JsonProperty("splitPaymentType")
    private String splitPaymentType;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("amountForUsers")
    private List<Double> amountForUsers;
    @JsonProperty("involvedAccounts")
    private List<String> involvedAccounts;

    public SplitPaymentTransaction(final int timestamp, final String description,
                                   final String splitPaymentType,
                                   final String currency, final Double amount,
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
