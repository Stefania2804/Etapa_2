package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.Savings;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;
import org.poo.main.JsonOutput;

public final class AddInterestCommand implements Command {
    /**
     * functia de executare a comenzii de adaugare de dobanda la un cont
     * de economii.
     */
    public void execute(final CommandInput commandInput, final InfoBank infoBank,
                        final ObjectMapper objectMapper, final ArrayNode output) {
        for (Account account : infoBank.getAccounts()) {
            if (account.getIban().equals(commandInput.getAccount())) {
                if (account.getClass() == Savings.class) {
                    account.setBalance(account.getBalance() + account.getBalance()
                            * ((Savings) account).getInterestRate());
                } else {
                    JsonOutput.interestRateError(commandInput, objectMapper, output);
                }
            }
        }
    }
}
