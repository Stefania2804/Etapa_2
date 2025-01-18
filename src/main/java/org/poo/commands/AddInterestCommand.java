package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.SavingsAccount;
import org.poo.visitor.User;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;
import org.poo.main.JsonOutput;
import org.poo.transactions.AddInterestTransaction;
import org.poo.transactions.Transaction;

public final class AddInterestCommand implements Command {
    /**
     * functia de executare a comenzii de adaugare de dobanda la un cont
     * de economii.
     */
    public void execute(final CommandInput commandInput, final InfoBank infoBank,
                        final ObjectMapper objectMapper, final ArrayNode output) {
        for (User user : infoBank.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(commandInput.getAccount())) {
                    if (account.getClass() == SavingsAccount.class) {
                        double amount = account.getBalance()
                                * ((SavingsAccount) account).getInterestRate();
                        account.setBalance(account.getBalance() + account.getBalance()
                                * ((SavingsAccount) account).getInterestRate());
                        Transaction transaction = new AddInterestTransaction(
                                commandInput.getTimestamp(), "Interest rate income",
                                account.getCurrency(), amount);
                        user.addTransaction(transaction);
                        account.addTransaction(transaction);
                    } else {
                        JsonOutput.interestRateError(commandInput, objectMapper, output);
                    }
                }
            }
        }
    }
}
