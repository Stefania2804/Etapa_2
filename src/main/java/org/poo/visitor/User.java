package org.poo.visitor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.Business;
import org.poo.account.Classic;
import org.poo.account.Savings;
import org.poo.bank.InfoBank;
import org.poo.bank.SplitPayment;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.List;

public class User implements Visitable {
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private List<Classic> classics;
    @JsonIgnore
    private List<Savings> savings;
    @JsonIgnore
    private List<Business> businesses;
    private List<Account> accounts;
    @JsonIgnore
    private List<Transaction> transactions;
    @JsonIgnore
    private String birthDate;
    @JsonIgnore
    private String occupation;
    @JsonIgnore
    private List<SplitPayment> splitPayments;

    public User(final String firstName, final String lastName,
                final String email, final String birthDate,
                final String occupation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        classics = new ArrayList<>();
        savings = new ArrayList<>();
        businesses = new ArrayList<>();
        accounts = new ArrayList<>();
        transactions = new ArrayList<>();
        this.birthDate = birthDate;
        this.occupation = occupation;
        splitPayments = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public List<Classic> getClassics() {
        return classics;
    }

    public void setClassics(final List<Classic> classics) {
        this.classics = classics;
    }

    public List<Savings> getSavings() {
        return savings;
    }

    public void setSavings(final List<Savings> savings) {
        this.savings = savings;
    }

    public List<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(final List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(final List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public List<SplitPayment> getSplitPayments() {
        return splitPayments;
    }

    public void setSplitPayments(List<SplitPayment> splitPayments) {
        this.splitPayments = splitPayments;
    }

    /**
     * Adauga un cont clasic nou pentru utilizator.
     *
     */
    public void addClassic(final Classic account) {
        classics.add(account);
    }
    /**
     * Adauga un cont de economii.
     *
     */
    public void addSavings(final Savings account) {
        savings.add(account);
    }
    public void addBusiness(final Business business) {
        businesses.add(business);
    }
    /**
     * Adauga un cont oarecare.
     *
     */
    public void addAccounts(final Account account) {
        accounts.add(account);
    }
    /**
     * Sterge un cont.
     *
     */
    public void deleteFromUser(final Account account) {
        accounts.remove(account);
        if (account.getClass() == Savings.class) {
            savings.remove(account);
            return;
        }
        if (account.getClass() == Classic.class) {
            classics.remove(account);
        }
    }
    /**
     * Adauga o tranzactie facuta de utilizator.
     *
     */
    public void addTransaction(final Transaction transaction) {
        transactions.add(transaction);
    }
    public void addSplitPayment(final SplitPayment splitPayment) {
        splitPayments.add(splitPayment);
    }
    public void accept(Visitor v, CommandInput commandInput, InfoBank infoBank, Account account, ObjectMapper objectMapper, ArrayNode output) {
        return;
    }
}
