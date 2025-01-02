package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.card.Card;
import org.poo.account.card.OneTimeCard;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;
import org.poo.bank.User;
import org.poo.transactions.NewCardTransaction;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

public final class CreateOneTimeCardCommand implements Command {
    /**
     * functia de executare pentru crearea unui card de unica folosinta.
     */
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        String ibanAccOnetime = commandInput.getAccount();
        String userEmailOneTime = commandInput.getEmail();
        for (User user : infoBank.getUsers()) {
            if (user.getEmail().equals(userEmailOneTime)) {
                for (Account accs : user.getAccounts()) {
                    if (accs.getIban().equals(ibanAccOnetime)) {
                        String cardNumber = Utils.generateCardNumber();
                        Card card = new OneTimeCard(cardNumber, "active");
                        accs.addCard(card);
                        Transaction transaction = new
                                NewCardTransaction(commandInput.getTimestamp(),
                                "New card created",
                                cardNumber, userEmailOneTime, ibanAccOnetime);
                        user.addTransaction(transaction);
                        accs.addTransaction(transaction);
                        break;
                    }
                }
                break;
            }
        }
    }
}
