package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"timestamp", "description", "senderIBAN",
        "receiverIBAN", "amount", "transferTye"})
public final class SendMoneyTransaction extends Transaction {
    @JsonProperty("senderIBAN")
    private String senderIban;
    @JsonProperty("receiverIBAN")
    private String receiverIban;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("transferType")
    private String transferType;

    public SendMoneyTransaction(final int timestamp, final String description,
                                final String senderIban,
                                final String receiverIban,
                                final String amount,
                                final String transferType) {
        super(timestamp, description);
        this.senderIban = senderIban;
        this.receiverIban = receiverIban;
        this.amount = amount;
        this.transferType = transferType;
    }
}
