package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.Classic;
import org.poo.account.Savings;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;
import org.poo.bank.User;
import org.poo.transactions.NewAccTransaction;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

public final class AddAccountCommand implements Command {
    /**
     * functie pentru adaugarea unui cont nou.
     */
    public void execute(final CommandInput commandInput, final InfoBank infoBank,
                        final ObjectMapper objectMapper, final ArrayNode output) {
        String email = commandInput.getEmail();
        for (User user : infoBank.getUsers()) {
            if (user.getEmail().equals(email)) {
                Transaction transaction = new NewAccTransaction(
                        commandInput.getTimestamp(),
                        "New account created");
                user.addTransaction(transaction);
                String currency = commandInput.getCurrency();
                String type = commandInput.getAccountType();
                String iban = Utils.generateIBAN();
                switch (type) {
                    case "classic":
                        Account classic = new Classic(iban, 0.0, currency, type);
                        user.addClassic((Classic) classic);
                        user.addAccounts(classic);
                        infoBank.addAccount(classic);
                        classic.addTransaction(transaction);
                        break;
                    case "savings":
                        Account savings = new Savings(iban, 0.0, currency, type,
                                commandInput.getInterestRate());
                        user.addSavings((Savings) savings);
                        user.addAccounts(savings);
                        infoBank.addAccount(savings);
                        savings.addTransaction(transaction);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
