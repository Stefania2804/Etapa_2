package org.poo.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Data;
import org.poo.account.Account;
import org.poo.bank.InfoBank;
import org.poo.fileio.CommandInput;

public class Employee extends User {
    private String name;
    private double spent;
    private double deposited;

    public Employee(final User user) {
        super(user.getFirstName(), user.getLastName(), user.getEmail(), user.getBirthDate(), user.getOccupation());
        spent = 0.0;
        deposited = 0.0;
        name = toString(user);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSpent() {
        return spent;
    }

    public void setSpent(double spent) {
        this.spent = spent;
    }

    public double getDeposited() {
        return deposited;
    }

    public void setDeposited(double deposited) {
        this.deposited = deposited;
    }

    @Override
    public void accept(Visitor v, CommandInput commandInput, InfoBank infoBank, Account account,
                       ObjectMapper objectMapper, ArrayNode output) {
        v.visitEmployee(this, commandInput, infoBank, account, objectMapper, output);
    }
    public String toString(User user) {
        return user.getLastName() + " " + user.getFirstName();
    }
}
