package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.Business;
import org.poo.main.JsonOutput;
import org.poo.bank.InfoBank;
import org.poo.visitor.CommandVisitor;
import org.poo.visitor.Employee;
import org.poo.visitor.Manager;
import org.poo.visitor.User;
import org.poo.fileio.CommandInput;

public class SendMoneyCommand implements Command {
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
                for (Account account : infoBank.getAccounts()) {
                    if (account.getIban().equals(commandInput.getAccount())) {
                        userFound = true;
                        if (account.getType().equals("business")
                                && !(((Business) account).getOwner().getEmail().equals(commandInput.getEmail()))) {
                            boolean employeeFound = false;
                            for (Employee employee : ((Business) account).getEmployees()) {
                                if (commandInput.getEmail().equals(employee.getEmail())) {
                                    employeeFound = true;
                                    CommandVisitor commandVisitor = new CommandVisitor();
                                    employee.accept(commandVisitor, commandInput, infoBank, account, objectMapper, output);
                                }
                            }
                            if (employeeFound == false) {
                                for (Manager manager : ((Business) account).getManagers()) {
                                    if (commandInput.getEmail().equals(manager.getEmail())) {
                                        CommandVisitor commandVisitor = new CommandVisitor();
                                        manager.accept(commandVisitor, commandInput, infoBank, account, objectMapper, output);
                                    }
                                }
                            }
                            businessFound = true;
                        }
                    }
                }
            }
        }
        if (userFound == false) {
            JsonOutput.errorUser(commandInput, objectMapper, output);
            return;
        }
        if (businessFound == false) {
            ExecuteSendMoney.execute(commandInput, infoBank, objectMapper, output);
        }
    }
}
