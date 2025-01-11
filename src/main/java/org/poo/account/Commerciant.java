package org.poo.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Commerciant {
    @JsonIgnore
    private List<Integer> payOnlineTimestamps;
    @JsonProperty("commerciant")
    private String name;
    @JsonProperty("total")
    private double amount;
    @JsonIgnore
    private int nrOfTransactions;
    @JsonIgnore
    private String cashBackType;
    @JsonIgnore
    private String account;
    @JsonIgnore
    private int id;
    @JsonIgnore
    private String type;
    @JsonIgnore
    private double moneySpent;

    public Commerciant(final double amount, final String name,
                       final String cashBackType,
                       final String type, final int id,
                       final String account) {
        this.amount = amount;
        this.name = name;
        nrOfTransactions = 0;
        this.cashBackType = cashBackType;
        this.type = type;
        this.id = id;
        this.account = account;
        payOnlineTimestamps = new ArrayList<>();
        moneySpent = 0.0;
    }
    public Commerciant (Commerciant commerciant) {
        this.amount = 0.0;
        this.name = commerciant.getName();
        nrOfTransactions = 0;
        this.cashBackType = commerciant.getCashBackType();
        this.type = commerciant.getType();
        this.id = commerciant.getId();
        this.account = commerciant.getAccount();
        payOnlineTimestamps = new ArrayList<>();
        moneySpent = 0.0;
    }

    public List<Integer> getPayOnlineTimestamps() {
        return payOnlineTimestamps;
    }

    public void setPayOnlineTimestamps(List<Integer> payOnlineTimestamps) {
        this.payOnlineTimestamps = payOnlineTimestamps;
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

    public int getNrOfTransactions() {
        return nrOfTransactions;
    }

    public void setNrOfTransactions(int nrOfTransactions) {
        this.nrOfTransactions = nrOfTransactions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCashBackType() {
        return cashBackType;
    }

    public void setCashBackType(String cashBackType) {
        this.cashBackType = cashBackType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent(double moneySpent) {
        this.moneySpent = moneySpent;
    }

    public void addTimestamp(int timestamp) {
        payOnlineTimestamps.add(timestamp);
    }
}
