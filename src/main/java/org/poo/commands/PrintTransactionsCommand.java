package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;
import org.poo.main.JsonOutput;
import org.poo.visitor.User;

public final class PrintTransactionsCommand implements Command {
    /**
     * functia de executare pentru printare tranzactii.
     */
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        String emailTransaction = commandInput.getEmail();
        for (User user : infoBank.getUsers()) {
            if (user.getEmail().equals(emailTransaction)) {
                JsonOutput.printTransactions(commandInput, user,
                        objectMapper, output);
            }
        }
    }
}
