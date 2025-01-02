package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;
import org.poo.bank.User;

public final class SetAliasCommand implements Command {
    /**
     * functia de executare pentru setarea unui alias.
     */
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        for (User user : infoBank.getUsers()) {
            if (user.getEmail().equals(commandInput.getEmail())) {
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(commandInput.getAccount())) {
                        infoBank.setAlias(commandInput.getAlias(), commandInput.getAccount());
                    }
                }
            }
        }
    }
}
