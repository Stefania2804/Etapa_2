package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"timestamp", "description", "accountIBAN",
        "newPlanType",})
public class UpgradePlanTransaction extends Transaction {
    @JsonProperty("accountIBAN")
    private String iban;
    @JsonProperty("newPlanType")
    private String plan;

    public UpgradePlanTransaction(final int timestamp,
                                  final String description,
                                  final String iban,
                                  final String plan) {
        super(timestamp, description);
        this.iban = iban;
        this.plan = plan;
    }
}
