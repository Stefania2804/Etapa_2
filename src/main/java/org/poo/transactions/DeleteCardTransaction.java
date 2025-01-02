package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"timestamp", "description", "card", "cardHolder", "account"})
public final class DeleteCardTransaction extends Transaction {
    @JsonProperty("card")
    private String card;
    @JsonProperty("cardHolder")
    private String cardHolder;
    @JsonProperty("account")
    private String account;

    public DeleteCardTransaction(final int timestamp, final String description,
                                 final String card,
                                 final String cardHolder,
                                 final String account) {
        super(timestamp, description);
        this.card = card;
        this.cardHolder = cardHolder;
        this.account = account;
    }
}
