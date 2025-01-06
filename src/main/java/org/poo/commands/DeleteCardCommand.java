package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.card.Card;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;
import org.poo.visitor.User;
import org.poo.transactions.DeleteCardTransaction;
import org.poo.transactions.Transaction;

public final class DeleteCardCommand implements Command {
    /**
     * functia de executare a stergerii unui card.
     */
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        String cardNumber = commandInput.getCardNumber();
        for (User user : infoBank.getUsers()) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(cardNumber)) {
                        account.deleteCard(card);
                        Transaction transaction = new DeleteCardTransaction(
                                commandInput.getTimestamp(),
                                "The card has been destroyed",
                                cardNumber, user.getEmail(), account.getIban());
                        user.addTransaction(transaction);
                        account.addTransaction(transaction);
                        break;
                    }
                }
            }
        }
    }
}
