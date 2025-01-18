package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.bank.InfoBank;
import org.poo.bank.SplitPayment;
import org.poo.fileio.CommandInput;
import org.poo.main.JsonOutput;
import org.poo.visitor.User;

public final class RejectSplitPaymentCommand implements Command {
    /**
     * functia de executare a respingerea unei plati distribuite.
     */
    public void execute(final CommandInput commandInput, final InfoBank infoBank,
                        final ObjectMapper objectMapper, final ArrayNode output) {
        boolean userFound = false;
        SplitPayment splitPayment = null;
        for (User user : infoBank.getUsers()) {
            if (user.getEmail().equals(commandInput.getEmail())) {
                userFound = true;
                if (!user.getSplitPayments().isEmpty()) {
                    for (User user1 : infoBank.getUsers()) {
                        if (!user1.getEmail().equals(commandInput.getEmail())) {
                            for (Account account : user1.getAccounts()) {
                                for (String iban : user.getSplitPayments().get(0).getAccounts()) {
                                    if (account.getIban().equals(iban)) {
                                        user.getSplitPayments().get(0).setRejects(1);
                                        splitPayment = user.getSplitPayments().get(0);
                                        user1.getSplitPayments().remove(splitPayment);
                                    }
                                }
                            }
                        }
                    }
                }
                user.getSplitPayments().remove(0);
            }
        }
        if (!userFound) {
            JsonOutput.errorUser(commandInput, objectMapper, output);
            return;
        }
        ExecuteSplitPayment executeSplitPaymentCommand = new ExecuteSplitPayment();
        executeSplitPaymentCommand.execute(splitPayment, infoBank, objectMapper, output);
    }
}
