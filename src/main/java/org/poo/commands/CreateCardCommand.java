package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.card.Card;
import org.poo.account.card.NormalCard;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;
import org.poo.bank.User;
import org.poo.transactions.NewCardTransaction;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

public final class CreateCardCommand implements Command {
    /**
     * functia de executare a comenzii de creare a unui card
     */
    public void execute(final CommandInput commandInput, final InfoBank infoBank,
                        final ObjectMapper objectMapper, final ArrayNode output) {
        int success = 0;
        String ibanAcc = commandInput.getAccount();
        String userEmail = commandInput.getEmail();
        User userFound = new User(null, null, userEmail, null, null);
        for (User user : infoBank.getUsers()) {
            if (user.getEmail().equals(userEmail)) {
                userFound = user;
                for (Account accs : user.getAccounts()) {
                    if (accs.getIban().equals(ibanAcc)) {
                        String cardNumber = Utils.generateCardNumber();
                        Card card = new NormalCard(cardNumber, "active");
                        accs.addCard(card);
                        Transaction transaction = new
                                NewCardTransaction(commandInput.getTimestamp(),
                                "New card created",
                                cardNumber, userEmail, ibanAcc);
                        user.addTransaction(transaction);
                        accs.addTransaction(transaction);
                        success = 1;
                        break;
                    }
                }
                break;
            }
        }
        if (success == 0) {
            Transaction transaction = new Transaction(commandInput.getTimestamp(),
                    "The account doesn't belong to the user");
            userFound.addTransaction(transaction);
        }
    }
}
