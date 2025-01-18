package org.poo.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.bank.InfoBank;
import org.poo.fileio.CommandInput;

public final class Employee extends User {
    private String name;
    private double spent;
    private double deposited;

    public Employee(final User user) {
        super(user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getBirthDate(), user.getOccupation());
        spent = 0.0;
        deposited = 0.0;
        name = toString(user);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public double getSpent() {
        return spent;
    }

    public void setSpent(final double spent) {
        this.spent = spent;
    }

    public double getDeposited() {
        return deposited;
    }

    public void setDeposited(final double deposited) {
        this.deposited = deposited;
    }


    @Override
    public void accept(final Visitor v,
                       final CommandInput commandInput,
                       final InfoBank infoBank,
                       final Account account,
                       final ObjectMapper objectMapper,
                       final ArrayNode output) {
        v.visitEmployee(this, commandInput, infoBank, account, objectMapper, output);
    }
}
