package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;
import org.poo.visitor.CommandVisitor;
import org.poo.visitor.Employee;
import org.poo.visitor.Manager;

public final class AddFundsCommand implements Command {
    /**
     * functia de executare pentru adaugarea de fonduri.
     */
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        String iban = commandInput.getAccount();
        for (Account acc : infoBank.getAccounts()) {
            if (acc.getIban().equals(iban)) {
                if (acc.getType().equals("business")) {
                    boolean employeeFound = false;
                    boolean managerFound = false;
                    for (Employee employee : ((BusinessAccount) acc).getEmployees()) {
                        if (commandInput.getEmail().equals(employee.getEmail())) {
                            employeeFound = true;
                            CommandVisitor commandVisitor = new CommandVisitor();
                            employee.accept(commandVisitor, commandInput, infoBank,
                                    acc, objectMapper, output);
                        }
                    }
                    if (!employeeFound) {
                        for (Manager manager : ((BusinessAccount) acc).getManagers()) {
                            if (commandInput.getEmail().equals(manager.getEmail())) {
                                managerFound = true;
                                CommandVisitor commandVisitor = new CommandVisitor();
                                manager.accept(commandVisitor, commandInput, infoBank,
                                        acc, objectMapper, output);
                            }
                        }
                        if (!managerFound) {
                            if (commandInput.getEmail().equals(
                                    ((BusinessAccount) acc).getOwner().getEmail())) {
                                acc.setBalance(commandInput.getAmount() + acc.getBalance());
                            }
                        }
                    }
                } else {
                    acc.setBalance(commandInput.getAmount() + acc.getBalance());
                }
            }
        }
    }
}
