package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.bank.InfoBank;
import org.poo.bank.SplitPayment;
import org.poo.main.JsonOutput;
import org.poo.visitor.User;
import org.poo.fileio.CommandInput;

public final class AcceptSplitPaymentCommand implements Command {
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        boolean userFound = false;
        for (User user : infoBank.getUsers()) {
            if (user.getEmail().equals(commandInput.getEmail())) {
                userFound = true;
                if (!user.getSplitPayments().isEmpty()) {
                    for (User user1 : infoBank.getUsers()) {
                        if (!user1.getEmail().equals(commandInput.getEmail())) {
                            for (Account account : user1.getAccounts()) {
                                for (String iban : user.getSplitPayments().get(0).getAccounts()) {
                                    if (account.getIban().equals(iban)) {
                                        for (SplitPayment splitPayment : user1.getSplitPayments()) {
                                            if (splitPayment.equals(user.getSplitPayments().get(0))) {
                                                splitPayment.setAccepts(splitPayment.getAccepts() + 1);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    user.getSplitPayments().get(0).setAccepts(user.getSplitPayments().get(0).getAccepts() + 1);
                    if (user.getSplitPayments().get(0).getAccepts() == user.getSplitPayments().get(0).getAccounts().size()) {
                        ExecuteSplitPayment executeSplitPaymentCommand = new ExecuteSplitPayment();
                        executeSplitPaymentCommand.execute(user.getSplitPayments().get(0), infoBank, objectMapper, output);
                    }
                    user.getSplitPayments().remove(0);
                }
            }
        }
        if((userFound == false)) {
            JsonOutput.errorUser(commandInput, objectMapper, output);
        }
    }
}
