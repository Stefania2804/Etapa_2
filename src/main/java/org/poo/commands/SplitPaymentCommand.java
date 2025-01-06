package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.bank.SplitPayment;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;
import org.poo.visitor.User;

public final class SplitPaymentCommand implements Command {
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        for (User user : infoBank.getUsers()) {
            for (Account account : user.getAccounts()) {
                for (String acc : commandInput.getAccounts()) {
                    if (account.getIban().equals(acc)) {
                        SplitPayment splitPayment = new SplitPayment(commandInput);
                        user.addSplitPayment(splitPayment);
                    }
                }
            }
        }
    }
}
