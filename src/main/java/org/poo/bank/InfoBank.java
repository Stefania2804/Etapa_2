package org.poo.bank;

import org.poo.account.Account;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

public final class InfoBank {
    private List<User> users;
    private List<Account> accounts;
    private List<Exchange> exchanges;
    private HashMap<String, String> hashMap;

    public InfoBank() {
        users = new ArrayList<>();
        accounts = new ArrayList<>();
        exchanges = new ArrayList<>();
        hashMap = new HashMap<>();
    }
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(final ArrayList<User> users) {
        this.users = users;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(final ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Exchange> getExchanges() {
        return exchanges;
    }

    public void setExchanges(final List<Exchange> exchanges) {
        this.exchanges = exchanges;
    }

    public HashMap<String, String> getHashMap() {
        return hashMap;
    }

    public void setHashMap(final HashMap<String, String> hashMap) {
        this.hashMap = hashMap;
    }
    /**
     * Adauga un client nou al bancii.
     *
     */
    public void addUser(final User user) {
        users.add(user);
    }
    /**
     * Adauga un cont nou deschis la banca.
     *
     */
    public void addAccount(final Account account) {
        accounts.add(account);
    }
    /**
     * Adauga un nou curs valutar disponibil.
     *
     */
    public void addExchange(final Exchange exchange) {
        exchanges.add(exchange);
    }
    /**
     * Sterge un cont deschis la banca.
     *
     */
    public void deleteFromBank(final Account account) {
        accounts.remove(account);
    }

    /**
     * Functie recursiva schimb valutar.
     *
     */
    public double recursiveExchange(final String from, final String to,
                                    final double amount,
                                    final Set<String> visited) {
        if (from.equals(to)) {
            return amount;
        }

        visited.add(from);
        MathContext precision = new MathContext(16);
        for (Exchange exchange : exchanges) {
            if (exchange.getFrom().equals(from) && !visited.contains(exchange.getTo())) {
                BigDecimal rate = new BigDecimal(exchange.getRate(), precision);
                BigDecimal newAmount = new BigDecimal(amount, precision).multiply(rate, precision);

                double result = recursiveExchange(exchange.getTo(),
                        to, newAmount.doubleValue(), visited);
                if (result != -1) {
                    return result;
                }
            }
            if (exchange.getTo().equals(from) && !visited.contains(exchange.getFrom())) {
                BigDecimal rate = new BigDecimal(exchange.getRate(), precision);
                BigDecimal newAmount = new BigDecimal(amount, precision).divide(rate, precision);

                double result = recursiveExchange(exchange.getFrom(), to,
                        newAmount.doubleValue(), visited);
                if (result != -1) {
                    return result;
                }
            }
        }
        return -1;
    }
    /**
     * Schimb valutar intre doua monede.
     *
     */
    public double exchange(final String from, final String to,
                           final double amount) {

        HashSet<String> visited = new HashSet<>();
        double result = recursiveExchange(from, to, amount, visited);
        return result;
    }
    /**
     * Setarea unui alias.
     *
     */
    public void setAlias(final String aliasName,
                         final String iban) {
        hashMap.put(aliasName, iban);
    }
}
