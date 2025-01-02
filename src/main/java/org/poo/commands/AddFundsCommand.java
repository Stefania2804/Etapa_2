package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;

public final class AddFundsCommand implements Command {
    /**
     * functia de executare pentru adaugarea de fonduri.
     */
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        String iban = commandInput.getAccount();
        for (Account acc : infoBank.getAccounts()) {
            if (acc.getIban().equals(iban)) {
                acc.setBalance(commandInput.getAmount() + acc.getBalance());
            }
        }
    }
}
