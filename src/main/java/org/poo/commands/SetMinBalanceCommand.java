package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;

public final class SetMinBalanceCommand implements Command {
    /**
     * functia de executare pentru setarea unei balante minime.
     */
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        double minBalance = commandInput.getMinBalance();
        String minBalanceIban = commandInput.getAccount();
        for (Account account : infoBank.getAccounts()) {
            if (account.getIban().equals(minBalanceIban)) {
                account.setMinBalance(minBalance);
                break;
            }
        }
    }
}
