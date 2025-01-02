package org.poo.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Commerciant {
    @JsonIgnore
    private int payOnlineTimestamp;
    @JsonProperty("commerciant")
    private String name;
    @JsonProperty("total")
    private double amount;

    public Commerciant(final double amount, final String name,
                        final int payOnlineTimestamp) {
        this.amount = amount;
        this.name = name;
        this.payOnlineTimestamp = payOnlineTimestamp;
    }

    public int getPayOnlineTimestamp() {
        return payOnlineTimestamp;
    }

    public void setPayOnlineTimestamp(final int payOnlineTimestamp) {
        this.payOnlineTimestamp = payOnlineTimestamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
