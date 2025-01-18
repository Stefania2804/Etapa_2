package org.poo.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.main.Constants;
import org.poo.visitor.Employee;
import org.poo.visitor.Manager;
import org.poo.visitor.Owner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class BusinessAccount extends Account {
    @JsonIgnore
    private Owner owner;
    @JsonIgnore
    private List<Employee> employees;
    @JsonIgnore
    private List<Manager> managers;
    @JsonIgnore
    private double spendingLimit;
    @JsonIgnore
    private double depositLimit;
    @JsonIgnore
    private HashMap<Integer, Pair> employeesMap;
    @JsonIgnore
    private HashMap<Integer, Pair> managersMap;
    @JsonIgnore
    private HashMap<Integer, Pair> commerciantsMap;

    public BusinessAccount(final String iban, final double balance,
                           final String currency,
                           final String type,
                           final String plan) {
        super(iban, balance, currency, type, plan);
        owner = null;
        managers = new ArrayList<>();
        employees = new ArrayList<>();
        spendingLimit = Constants.SPENDINGLIMIT.getValue();
        depositLimit = Constants.DEPOSITLIMIT.getValue();
        employeesMap = new HashMap<>();
        managersMap = new HashMap<>();
        commerciantsMap = new HashMap<>();
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(final Owner owner) {
        this.owner = owner;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(final List<Employee> employees) {
        this.employees = employees;
    }

    public List<Manager> getManagers() {
        return managers;
    }

    public void setManagers(final List<Manager> managers) {
        this.managers = managers;
    }

    public double getSpendingLimit() {
        return spendingLimit;
    }

    public void setSpendingLimit(final double spendingLimit) {
        this.spendingLimit = spendingLimit;
    }

    public double getDepositLimit() {
        return depositLimit;
    }

    public void setDepositLimit(final double depositLimit) {
        this.depositLimit = depositLimit;
    }

    public HashMap<Integer, Pair> getEmployeesMap() {
        return employeesMap;
    }

    public void setEmployeesMap(final HashMap<Integer, Pair> employeesMap) {
        this.employeesMap = employeesMap;
    }

    public HashMap<Integer, Pair> getManagersMap() {
        return managersMap;
    }

    public void setManagersMap(final HashMap<Integer, Pair> managersMap) {
        this.managersMap = managersMap;
    }

    public HashMap<Integer, Pair> getCommerciantsMap() {
        return commerciantsMap;
    }

    public void setCommerciantsMap(final HashMap<Integer, Pair> commerciantsMap) {
        this.commerciantsMap = commerciantsMap;
    }
    /**
     * Adauga un angajat la cont.
     *
     */
    public void addEmployee(final Employee employee) {
        employees.add(employee);
    }
    /**
     * Adauga un manager la cont.
     *
     */
    public void addManager(final Manager manager) {
        managers.add(manager);
    }
    /**
     * Adauga o tranzactie facuta de un angajat.
     *
     */
    public void addEmployeeAction(final Pair pair,
                                  final int timestamp) {
        employeesMap.put(timestamp, pair);
    }
    /**
     * Adauga o tranzactie facuta de un manager.
     *
     */
    public void addManagerAction(final Pair pair,
                                 final int timestamp) {
        managersMap.put(timestamp, pair);

    }
    /**
     * Adauga o tranzactie la comercianti.
     *
     */
    public void addCommerciantReceiving(final Pair pair,
                                        final int timestamp) {
        commerciantsMap.put(timestamp, pair);

    }

}
