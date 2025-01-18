package org.poo.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.poo.account.card.Card;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"IBAN", "balance", "currency", "type", "cards"})
public class Account {
    @JsonProperty("IBAN")
    private String iban;
    private double balance;
    private String currency;
    private String type;
    private ArrayList<Card> cards;
    @JsonIgnore
    private double minBalance;
    @JsonIgnore
    private ArrayList<Transaction> transactions;
    @JsonIgnore
    private List<Commerciant> commerciants;
    @JsonIgnore
    private String plan;
    @JsonIgnore
    private boolean receivedFood;
    @JsonIgnore
    private boolean receivedClothes;
    @JsonIgnore
    private boolean receivedTech;

    public Account(final String iban, final double balance, final String currency,
                   final String type, final String plan) {
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
        this.type = type;
        this.cards = new ArrayList<>();
        minBalance = 0.0;
        transactions = new ArrayList<>();
        commerciants = new ArrayList<>();
        this.plan = plan;
        receivedFood = false;
        receivedClothes = false;
        receivedTech = false;
    }
    /**
     * Getter pentru iban.
     *
     */
    public String getIban() {
        return iban;
    }
    /**
     * Setter pentru iban.
     *
     */

    public void setIban(final String iban) {
        this.iban = iban;
    }
    /**
     * Getter pentru balance.
     *
     */

    public double getBalance() {
        return balance;
    }
    /**
     * Setter pentru balance.
     *
     */

    public void setBalance(final double balance) {
        this.balance = balance;
    }
    /**
     * Getter pentru currency.
     *
     */

    public String getCurrency() {
        return currency;
    }
    /**
     * Setter pentru currency.
     *
     */

    public void setCurrency(final String currency) {
        this.currency = currency;
    }
    /**
     * Getter pentru type.
     *
     */

    public String getType() {
        return type;
    }
    /**
     * Setter pentru type.
     *
     */

    public void setType(final String type) {
        this.type = type;
    }
    /**
     * Getter pentru tranzactiile contului.
     *
     */
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
    /**
     * Setter pentru tranzactiile contului.
     *
     */
    public void setTransactions(final ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }
    /**
     * Getter pentru comerciantii contului.
     *
     */
    public List<Commerciant> getCommerciants() {
        return commerciants;
    }
    /**
     * Setter pentru comerciantii contului.
     *
     */
    public void setCommerciants(final List<Commerciant> commerciants) {
        this.commerciants = commerciants;
    }

    /**
     * Getter pentru cardurile asociate contului.
     *
     */
    public ArrayList<Card> getCards() {
        return cards;
    }
    /**
     * Setter pentru cardurile asociate contului.
     *
     */
    public void setCards(final ArrayList<Card> card) {
        this.cards = card;
    }
    /**
     * Getter pentru balanta minima.
     *
     */
    public double getMinBalance() {
        return minBalance;
    }
    /**
     * Seteaza o balanta minima pentru cont.
     *
     */
    public void setMinBalance(final double minBalance) {
        this.minBalance = minBalance;
    }
    /**
     * Getter pentru planul contului.
     *
     */
    public String getPlan() {
        return plan;
    }
    /**
     * Setter pentru planul contului.
     *
     */
    public void setPlan(final String plan) {
        this.plan = plan;
    }
    /**
     * Getter pentru statusul casback-ului Food(primit/neprimit).
     *
     */
    public boolean isReceivedFood() {
        return receivedFood;
    }
    /**
     * Setter pentru statusul cashback-ului Food.
     *
     */
    public void setReceivedFood(final boolean receivedFood) {
        this.receivedFood = receivedFood;
    }
    /**
     * Getter pentru statusul casback-ului Clothes(primit/neprimit).
     *
     */
    public boolean isReceivedClothes() {
        return receivedClothes;
    }
    /**
     * Setter pentru statusul cashback-ului Clothes.
     *
     */
    public void setReceivedClothes(final boolean receivedClothes) {
        this.receivedClothes = receivedClothes;
    }
    /**
     * Getter pentru statusul casback-ului Tech(primit/neprimit).
     *
     */
    public boolean isReceivedTech() {
        return receivedTech;
    }
    /**
     * Setter pentru statusul cashback-ului Tech.
     *
     */
    public void setReceivedTech(final boolean receivedTech) {
        this.receivedTech = receivedTech;
    }
    /**
     * Adauga un card in lista de carduri.
     *
     */
    public void addCard(final Card card) {
        cards.add(card);
    }
    /**
     * Sterge un card al contului.
     *
     */
    public void deleteCard(final Card card) {
        cards.remove(card);
    }
    /**
     * Adauga o tranzactie in cont.
     *
     */
    public void addTransaction(final Transaction transaction) {
        transactions.add(transaction);
    }
    /**
     * Adauga un comerciant al contului.
     *
     */
    public void addCommerciant(final Commerciant commerciant) {
        commerciants.add(commerciant);
    }
}
