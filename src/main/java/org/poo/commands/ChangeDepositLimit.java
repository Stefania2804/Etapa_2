package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.bank.InfoBank;
import org.poo.fileio.CommandInput;
import org.poo.main.JsonOutput;
import org.poo.visitor.User;

public final class ChangeDepositLimit implements Command {
    /**
     * functia de executare pentru schimbarea limitei de depunere pentru angajati.
     */
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        boolean ownerSucces = false;
        for (User user : infoBank.getUsers()) {
            if (user.getEmail().equals(commandInput.getEmail())) {
                for (Account account : infoBank.getAccounts()) {
                    if (account.getType().equals("business")
                            && account.getIban().equals(commandInput.getAccount())
                            && ((BusinessAccount) account).getOwner().getEmail().
                            equals(commandInput.getEmail())) {
                        ownerSucces = true;
                        ((BusinessAccount) account).setDepositLimit(commandInput.getAmount());
                    }
                }
            }
        }
        if (!ownerSucces) {
            JsonOutput.depositLimitError(commandInput, objectMapper, output);
        }
    }
}
