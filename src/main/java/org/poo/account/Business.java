package org.poo.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.visitor.Employee;
import org.poo.visitor.Manager;
import org.poo.visitor.Owner;

import java.util.ArrayList;
import java.util.List;

public class Business extends Account {
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

    public Business(final String iban, final double balance,
                   final String currency,
                   final String type,
                   final String plan) {
        super(iban, balance, currency, type, plan);
        owner = null;
        managers = new ArrayList<>();
        employees = new ArrayList<>();
        spendingLimit = 500;
        depositLimit = 500;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Manager> getManagers() {
        return managers;
    }

    public void setManagers(List<Manager> managers) {
        this.managers = managers;
    }

    public double getSpendingLimit() {
        return spendingLimit;
    }

    public void setSpendingLimit(double spendingLimit) {
        this.spendingLimit = spendingLimit;
    }

    public double getDepositLimit() {
        return depositLimit;
    }

    public void setDepositLimit(double depositLimit) {
        this.depositLimit = depositLimit;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }
    public void addManager(Manager manager) {
        managers.add(manager);
    }
}
