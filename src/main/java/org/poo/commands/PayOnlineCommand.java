package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.Business;
import org.poo.account.card.Card;
import org.poo.bank.InfoBank;
import org.poo.visitor.*;
import org.poo.fileio.CommandInput;
import org.poo.main.*;

public final class PayOnlineCommand implements Command {
    /**
     * functia de executare a platii online cu cardul.
     */
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        boolean businessFound = false;
        boolean cardFound = false;
        String payOnlineEmail = commandInput.getEmail();
        for (User user : infoBank.getUsers()) {
            if (user.getEmail().equals(payOnlineEmail)) {
                for (Account account : infoBank.getAccounts()) {
                    if (account.getType().equals("business")
                            && !(((Business) account).getOwner().getEmail().equals(payOnlineEmail))) {
                        for (Card card : account.getCards()) {
                            if (card.getCardNumber().equals(commandInput.getCardNumber())) {
                                cardFound = true;
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
                            }
                        }
                        businessFound = true;
                    }
                }
            }
        }
        if (businessFound == true && cardFound == false) {
            JsonOutput.errorCard(commandInput, objectMapper, output);
            return;
        }
        if (businessFound == false) {
            cardFound = false;
            for (User user : infoBank.getUsers()) {
                if (user.getEmail().equals(payOnlineEmail)) {
                    for (Account account : user.getAccounts()) {
                        for (Card card : account.getCards()) {
                            if (card.getCardNumber().equals(commandInput.getCardNumber())) {
                                cardFound = true;
                            }
                        }
                    }
                }
            }
            if (cardFound == true) {
                ExecuteOnlinePayment.execute(commandInput, infoBank, objectMapper, output);
            } else {
                JsonOutput.errorCard(commandInput, objectMapper, output);
            }
        }
    }
}
