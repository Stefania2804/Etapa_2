package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.card.Card;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;
import org.poo.main.JsonOutput;
import org.poo.bank.User;
import org.poo.transactions.CardStatusTransaction;
import org.poo.transactions.Transaction;

public final class CheckCardStatusCommand implements Command {
    /**
     * functia de executare pentru verificarea si inghetarea unui card.
     */
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        int cardFound = 0;
        for (User user : infoBank.getUsers()) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(commandInput.getCardNumber())) {
                        if (account.getBalance() <= account.getMinBalance()) {
                            Transaction transaction = new CardStatusTransaction(
                                    commandInput.getTimestamp(),
                                    "You have reached the minimum amount of "
                                            + "funds, the card will be frozen");
                            user.addTransaction(transaction);
                            card.setStatus("frozen");
                            account.addTransaction(transaction);
                        }
                        cardFound = 1;
                    }
                }
            }
        }
        if (cardFound == 0) {
            JsonOutput.errorCard(commandInput, objectMapper, output);
        }
    }
}
