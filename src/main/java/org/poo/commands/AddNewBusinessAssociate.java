package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.Business;
import org.poo.bank.InfoBank;
import org.poo.fileio.CommandInput;
import org.poo.visitor.Employee;
import org.poo.visitor.Manager;
import org.poo.visitor.User;

public class AddNewBusinessAssociate implements Command {
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        for (Account account : infoBank.getAccounts()) {
            if (account.getIban().equals(commandInput.getAccount()) && account.getType().equals("business")) {
                for (User user : infoBank.getUsers()) {
                    if (user.getEmail().equals(commandInput.getEmail())) {
                        user.addBusiness((Business) account);
                        user.addAccounts(account);
                        if (commandInput.getRole().equals("employee")) {
                            Employee employee = new Employee(user);
                            ((Business) account).addEmployee(employee);
                        } else if (commandInput.getRole().equals("manager")) {
                            Manager manager = new Manager(user);
                            ((Business) account).addManager(manager);
                        }
                    }
                }
            }
        }
    }
}
