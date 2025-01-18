package org.poo.visitor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.account.ClassicAccount;
import org.poo.account.SavingsAccount;
import org.poo.bank.InfoBank;
import org.poo.bank.Payment;
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
    private List<ClassicAccount> classics;
    @JsonIgnore
    private List<SavingsAccount> savings;
    @JsonIgnore
    private List<BusinessAccount> businesses;
    private List<Account> accounts;
    @JsonIgnore
    private List<Transaction> transactions;
    @JsonIgnore
    private String birthDate;
    @JsonIgnore
    private String occupation;
    @JsonIgnore
    private List<SplitPayment> splitPayments;
    @JsonIgnore
    private List<Payment> payments;
    @JsonIgnore
    private String fullName;
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
        payments = new ArrayList<>();
        fullName = toString(this);
    }
    /**
     * Getter pentru prenume.
     *
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     * Setter pentru prenume.
     *
     */

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }
    /**
     * Getter pentru nume.
     *
     */
    public String getLastName() {
        return lastName;
    }
    /**
     * Getter pentru email.
     *
     */
    public String getEmail() {
        return email;
    }
    /**
     * Setter pentru email.
     *
     */
    public void setEmail(final String email) {
        this.email = email;
    }
    /**
     * Setter pentru nume.
     *
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
    /**
     * Getter pentru conturile clasice.
     *
     */
    public List<ClassicAccount> getClassics() {
        return classics;
    }
    /**
     * Setter pentru conturile clasice.
     *
     */
    public void setClassics(final List<ClassicAccount> classics) {
        this.classics = classics;
    }
    /**
     * Getter pentru conturile de economii.
     *
     */
    public List<SavingsAccount> getSavings() {
        return savings;
    }
    /**
     * Setter pentru conturile de economii.
     *
     */
    public void setSavings(final List<SavingsAccount> savings) {
        this.savings = savings;
    }
    /**
     * Getter pentru conturile de afaceri.
     *
     */
    public List<BusinessAccount> getBusinesses() {
        return businesses;
    }
    /**
     * Getter pentru conturile de business.
     *
     */
    public void setBusinesses(final List<BusinessAccount> businesses) {
        this.businesses = businesses;
    }
    /**
     * Getter pentru toate conturile.
     *
     */
    public List<Account> getAccounts() {
        return accounts;
    }
    /**
     * Setter pentru toate conturile.
     *
     */
    public void setAccounts(final List<Account> accounts) {
        this.accounts = accounts;
    }
    /**
     * Getter pentru tranzactii.
     *
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }
    /**
     * Setter pentru tranzactii.
     *
     */
    public void setTransactions(final List<Transaction> transactions) {
        this.transactions = transactions;
    }
    /**
     * Getter pentru data de nastere.
     *
     */
    public String getBirthDate() {
        return birthDate;
    }
    /**
     * Setter pentru data de nastere.
     *
     */
    public void setBirthDate(final String birthDate) {
        this.birthDate = birthDate;
    }
    /**
     * Getter pentru ocupatie.
     *
     */
    public String getOccupation() {
        return occupation;
    }
    /**
     * Setter pentru ocupatie.
     *
     */
    public void setOccupation(final String occupation) {
        this.occupation = occupation;
    }
    /**
     * Getter pentru platile distribuite in asteptare.
     *
     */
    public List<SplitPayment> getSplitPayments() {
        return splitPayments;
    }
    /**
     * Setter pentru platile distribuite.
     *
     */
    public void setSplitPayments(final List<SplitPayment> splitPayments) {
        this.splitPayments = splitPayments;
    }
    /**
     * Getter pentru platile.
     *
     */
    public List<Payment> getPayments() {
        return payments;
    }
    /**
     * Setter pentru platile efectuate.
     *
     */
    public void setPayments(final List<Payment> payments) {
        this.payments = payments;
    }
    /**
     * Getter pentru numele complet.
     *
     */
    public String getFullName() {
        return fullName;
    }
    /**
     * Setter pentru numele complet.
     *
     */
    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    /**
     * Adauga un cont clasic nou pentru utilizator.
     *
     */
    public void addClassic(final ClassicAccount account) {
        classics.add(account);
    }
    /**
     * Adauga un cont de economii.
     *
     */
    public void addSavings(final SavingsAccount account) {
        savings.add(account);
    }
    /**
     * Adauga un cont de afaceri.
     *
     */
    public void addBusiness(final BusinessAccount business) {
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
        if (account.getClass() == SavingsAccount.class) {
            savings.remove(account);
            return;
        }
        if (account.getClass() == ClassicAccount.class) {
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
    /**
     * Adauga o plata distribuita in lista de asteptare.
     *
     */
    public void addSplitPayment(final SplitPayment splitPayment) {
        splitPayments.add(splitPayment);
    }
    /**
     * Functia de acceptare a vizitatorului.
     *
     */
    public void accept(final Visitor v, final CommandInput commandInput,
                       final InfoBank infoBank,
                       final Account account,
                       final ObjectMapper objectMapper,
                       final ArrayNode output) {
        v.visitUser(this, commandInput,
                infoBank, account, objectMapper, output);
    }
    /**
     * Adauga o tranzactie facuta de utilizator.
     *
     */
    public void addPayment(final Payment payment) {
        payments.add(payment);
    }
    /**
     * Scrie numele complet al utilizatorului.
     *
     */
    public String toString(final User user) {
        return user.getLastName() + " " + user.getFirstName();
    }
}
