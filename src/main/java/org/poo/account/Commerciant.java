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
    @JsonIgnore
    private double spentBusiness;
    @JsonIgnore
    private List<Client> clients;
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
        spentBusiness = 0.0;
        clients = new ArrayList<>();
    }
    public Commerciant(final Commerciant commerciant) {
        amount = 0.0;
        name = commerciant.getName();
        nrOfTransactions = 0;
        cashBackType = commerciant.getCashBackType();
        type = commerciant.getType();
        id = commerciant.getId();
        account = commerciant.getAccount();
        payOnlineTimestamps = new ArrayList<>();
        moneySpent = 0.0;
        spentBusiness = 0.0;
        clients = new ArrayList<>();
    }
    /**
     * Getter pentru timpii platilor online.
     *
     */
    public List<Integer> getPayOnlineTimestamps() {
        return payOnlineTimestamps;
    }
    /**
     * Setter pentru timpii platilor online.
     *
     */
    public void setPayOnlineTimestamps(final List<Integer> payOnlineTimestamps) {
        this.payOnlineTimestamps = payOnlineTimestamps;
    }
    /**
     * Getter pentru suma incasata pentru raport.
     *
     */
    public double getAmount() {
        return amount;
    }
    /**
     * Setter pentru suma incasata pentru raport.
     *
     */
    public void setAmount(final double amount) {
        this.amount = amount;
    }
    /**
     * Getter pentru nume.
     *
     */
    public String getName() {
        return name;
    }
    /**
     * Setter pentru nume.
     *
     */
    public void setName(final String name) {
        this.name = name;
    }
    /**
     * Getter pentru totalul numarului de tranzactii.
     *
     */
    public int getNrOfTransactions() {
        return nrOfTransactions;
    }
    /**
     * Setter pentru numarul de tranzactii.
     *
     */
    public void setNrOfTransactions(final int nrOfTransactions) {
        this.nrOfTransactions = nrOfTransactions;
    }
    /**
     * Getter pentru tipul comerciantului.
     *
     */
    public String getType() {
        return type;
    }
    /**
     * Setter pentru tipul comerciantului.
     *
     */
    public void setType(final String type) {
        this.type = type;
    }
    /**
     * Getter pentru tipul de cashback.
     *
     */
    public String getCashBackType() {
        return cashBackType;
    }
    /**
     * Setter pentru tipul de cashback.
     *
     */
    public void setCashBackType(final String cashBackType) {
        this.cashBackType = cashBackType;
    }
    /**
     * Getter pentru iban.
     *
     */
    public String getAccount() {
        return account;
    }
    /**
     * Setter pentru iban.
     *
     */
    public void setAccount(final String account) {
        this.account = account;
    }
    /**
     * Getter pentru id.
     *
     */
    public int getId() {
        return id;
    }
    /**
     * Setter pentru id.
     *
     */
    public void setId(final int id) {
        this.id = id;
    }
    /**
     * Getter pentru suma incasata pentru cashback.
     *
     */
    public double getMoneySpent() {
        return moneySpent;
    }
    /**
     * Setter pentru suma incasata pentru cashback.
     *
     */
    public void setMoneySpent(final double moneySpent) {
        this.moneySpent = moneySpent;
    }
    /**
     * Getter pentru suma incasata pentru raportul de afaceri.
     *
     */
    public double getSpentBusiness() {
        return spentBusiness;
    }
    /**
     * Setter pentru suma incasata pentru raportul de afaceri.
     *
     */
    public void setSpentBusiness(final double spentBusiness) {
        this.spentBusiness = spentBusiness;
    }
    /**
     * Getter pentru lista de clienti.
     *
     */
    public List<Client> getClients() {
        return clients;
    }
    /**
     * Setter pentru lista de clienti.
     *
     */
    public void setClients(final List<Client> clients) {
        this.clients = clients;
    }
    /**
     * Adaugarea unui client nou.
     *
     */
    public void addClient(final Client client) {
        clients.add(client);
    }
    /**
     * Adaugarea unui nou timp cd incasare.
     *
     */
    public void addTimestamp(final int timestamp) {
        payOnlineTimestamps.add(timestamp);
    }
}
