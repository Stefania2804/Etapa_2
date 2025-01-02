package org.poo.bank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.account.Account;
import org.poo.account.Classic;
import org.poo.account.Savings;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.List;

public final class User {
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private List<Classic> classics;
    @JsonIgnore
    private List<Savings> savings;
    private List<Account> accounts;
    @JsonIgnore
    private List<Transaction> transactions;

    public User(final String firstName, final String lastName, final String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        classics = new ArrayList<>();
        savings = new ArrayList<>();
        accounts = new ArrayList<>();
        transactions = new ArrayList<>();
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
}
