package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.Commerciant;
import org.poo.account.Savings;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;
import org.poo.main.JsonOutput;
import org.poo.transactions.OnlinePayTransaction;
import org.poo.transactions.Transaction;

public final class SpendingsReportCommand implements Command {
    /**
     * functia de executare pentru raportul de cheltuieli
     */
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        int found = 0;
        int savings = 0;
        Account accountFound = new Account(null, 0.0,
                null, null);
        for (Account account : infoBank.getAccounts()) {
            if (account.getIban().equals(commandInput.getAccount())) {
                found = 1;
                accountFound = account;
                if (account.getClass() == Savings.class) {
                    JsonOutput.errorSpendings(commandInput, objectMapper, output);
                    savings = 1;
                    break;
                }
            }
        }
        if (found == 0) {
            JsonOutput.errorAccount(commandInput, objectMapper, output);
            return;
        }
        if (savings == 0) {
            for (Transaction transaction : accountFound.getTransactions()) {
                if (transaction.getDescription().equals("Card payment")) {
                    if (transaction.getTimestamp() >= commandInput.getStartTimestamp()
                            && transaction.getTimestamp() <= commandInput.getEndTimestamp()) {
                        for (Commerciant commerciant : accountFound.getCommerciants()) {
                            if (commerciant.getName().equals(((OnlinePayTransaction)
                                    transaction).getCommerciant())) {
                                commerciant.setAmount(commerciant.getAmount()
                                        + ((OnlinePayTransaction) transaction).getAmount());
                            }
                        }
                    }
                }
            }
            for (Account account : infoBank.getAccounts()) {
                if (account.getIban().equals(commandInput.getAccount())) {
                    account.getCommerciants().sort((
                            p1, p2) -> p1.getName().compareTo(p2.getName()));
                    JsonOutput.printSpendingsReport(commandInput,
                            account, objectMapper, output);
                }
            }
        }
    }
}
