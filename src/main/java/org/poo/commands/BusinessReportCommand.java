package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.Business;
import org.poo.bank.InfoBank;
import org.poo.fileio.CommandInput;
import org.poo.main.JsonOutput;

public class BusinessReportCommand implements Command {
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        for (Account account : infoBank.getAccounts()) {
            if (account.getIban().equals(commandInput.getAccount()) && account.getClass() == Business.class) {
                JsonOutput.printBusinessReport(commandInput, infoBank, account, objectMapper, output);
            }
        }
    }
}
