package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.bank.InfoBank;
import org.poo.fileio.CommandInput;
import org.poo.visitor.Employee;
import org.poo.visitor.Manager;
import org.poo.visitor.User;

public final class AddNewBusinessAssociate implements Command {
    /**
     * functia de executare pentru adaugarea unui nou asociant la contul de afaceri.
     */
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        for (Account account : infoBank.getAccounts()) {
            if (account.getIban().equals(commandInput.getAccount())
                    && account.getType().equals("business")) {
                for (User user : infoBank.getUsers()) {
                    boolean alreadyAssociated = false;
                    if (user.getEmail().equals(commandInput.getEmail())) {
                        for (Employee employee : ((BusinessAccount) account).getEmployees()) {
                            if (employee.getEmail().equals(commandInput.getEmail())) {
                                alreadyAssociated = true;
                            }
                        }
                        for (Manager manager : ((BusinessAccount) account).getManagers()) {
                            if (manager.getEmail().equals(commandInput.getEmail())) {
                                alreadyAssociated = true;
                            }
                        }
                        if (commandInput.getEmail().equals(((BusinessAccount) account).
                                getOwner().getEmail())) {
                            alreadyAssociated = true;
                        }
                        if (!alreadyAssociated) {
                            user.addBusiness((BusinessAccount) account);
                            if (commandInput.getRole().equals("employee")) {
                                Employee employee = new Employee(user);
                                ((BusinessAccount) account).addEmployee(employee);
                            } else if (commandInput.getRole().equals("manager")) {
                                Manager manager = new Manager(user);
                                ((BusinessAccount) account).addManager(manager);
                            }
                        }
                    }
                }
            }
        }
    }
}
