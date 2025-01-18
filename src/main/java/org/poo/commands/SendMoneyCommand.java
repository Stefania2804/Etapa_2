package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.main.JsonOutput;
import org.poo.bank.InfoBank;
import org.poo.visitor.CommandVisitor;
import org.poo.visitor.Employee;
import org.poo.visitor.Manager;
import org.poo.visitor.User;
import org.poo.fileio.CommandInput;

public final class SendMoneyCommand implements Command {
    /**
     * functia de executare pentru transferul bancar intre doua conturi.
     */
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        boolean businessFound = false;
        boolean userFound = false;
        for (User user : infoBank.getUsers()) {
            if (user.getEmail().equals(commandInput.getEmail())) {
                userFound = true;
                for (Account account : infoBank.getAccounts()) {
                    if (account.getIban().equals(commandInput.getAccount())) {
                        if (account.getType().equals("business")
                                && !(((BusinessAccount) account).getOwner().getEmail().
                                equals(commandInput.getEmail()))) {
                            boolean employeeFound = false;
                            boolean managerFound = false;
                            for (Employee employee : ((BusinessAccount) account).getEmployees()) {
                                if (commandInput.getEmail().equals(employee.getEmail())) {
                                    employeeFound = true;
                                    CommandVisitor commandVisitor = new CommandVisitor();
                                    employee.accept(commandVisitor, commandInput,
                                            infoBank, account, objectMapper, output);
                                }
                            }
                            if (!employeeFound) {
                                for (Manager manager : ((BusinessAccount) account).getManagers()) {
                                    if (commandInput.getEmail().equals(manager.getEmail())) {
                                        managerFound = true;
                                        CommandVisitor commandVisitor = new CommandVisitor();
                                        manager.accept(commandVisitor, commandInput,
                                                infoBank, account, objectMapper, output);
                                    }
                                }
                            }
                            if (managerFound || employeeFound) {
                                businessFound = true;
                            }
                        }
                    }
                }
            }
        }
        if (!userFound) {
            JsonOutput.errorUser(commandInput, objectMapper, output);
            return;
        }
        if (!businessFound) {
            ExecuteSendMoney.execute(commandInput, infoBank, objectMapper, output);
        }
    }
}
