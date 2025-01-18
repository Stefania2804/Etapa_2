package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.SavingsAccount;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;
import org.poo.main.JsonOutput;
import org.poo.visitor.User;
import org.poo.transactions.ChangeIrTransaction;
import org.poo.transactions.Transaction;

public final class ChangeInterestRateCommand implements Command {
    /**
     * functia de executare pentru schimbarea dobanzii.
     */
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        for (User user : infoBank.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(commandInput.getAccount())) {
                    if (account.getClass() == SavingsAccount.class) {
                        ((SavingsAccount) account).setInterestRate(commandInput.getInterestRate());
                        String description = changeToString(commandInput.getInterestRate());
                        Transaction transaction = new ChangeIrTransaction(
                                commandInput.getTimestamp(), description);
                        user.addTransaction(transaction);
                        account.addTransaction(transaction);
                    } else {
                        JsonOutput.interestRateError(commandInput, objectMapper, output);
                    }
                }
            }
        }
    }
    /**
     * afisarea schimbarii.
     */
    public static String changeToString(final double rate) {
        return "Interest rate of the account changed to " + rate;
    }
}
