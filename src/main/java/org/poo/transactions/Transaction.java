package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"timestamp", "description"})
public class Transaction {
    @JsonProperty("timestamp")
    private int timestamp;
    @JsonProperty("description")
    private String description;

    public Transaction(final int timestamp, final String description) {
        this.timestamp = timestamp;
        this.description = description;
    }
    /**
     * Getter pentru timestamp-ul tranzactiei.
     *
     */
    public int getTimestamp() {
        return timestamp;
    }
    /**
     * Setter pentru timestamp.
     *
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }
    /**
     * Getter pentru descrierea tranzactiei.
     *
     */
    public String getDescription() {
        return description;
    }
    /**
     * Setter pentru descriere.
     *
     */
    public void setDescription(final String description) {
        this.description = description;
    }
}
