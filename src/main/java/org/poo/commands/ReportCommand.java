package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;
import org.poo.main.JsonOutput;

public final class ReportCommand implements Command {
    /**
     * functia de executare pentru raportul clasic.
     */
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        int failure = 1;
        for (Account account : infoBank.getAccounts()) {
            if (account.getIban().equals(commandInput.getAccount())) {
                failure = 0;
                JsonOutput.printClassicReport(commandInput, account,
                        objectMapper, output);
            }
        }
        if (failure == 1) {
            JsonOutput.errorAccount(commandInput, objectMapper, output);
        }
    }
}
