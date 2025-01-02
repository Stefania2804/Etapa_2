package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.Commerciant;
import org.poo.account.card.Card;
import org.poo.account.card.OneTimeCard;
import org.poo.bank.InfoBank;
import org.poo.strategy.OnlinePayment;
import org.poo.strategy.PaymentContext;
import org.poo.bank.User;
import org.poo.errorTransactions.ErrorPaymentTransaction;
import org.poo.fileio.CommandInput;
import org.poo.main.*;
import org.poo.strategy.PayStrategy;
import org.poo.transactions.DeleteCardTransaction;
import org.poo.transactions.NewCardTransaction;
import org.poo.transactions.OnlinePayTransaction;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

public final class PayOnlineCommand implements Command {
    /**
     * functia de executare a platii online cu cardul.
     */
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        int enoughFunds = 0;
        int successPay = 0;
        String payOnlineEmail = commandInput.getEmail();
        String payOnlineCard = commandInput.getCardNumber();
        for (User user : infoBank.getUsers()) {
            if (user.getEmail().equals(payOnlineEmail)) {
                for (Account account : user.getAccounts()) {
                    int comFound = 0;
                    for (Card card : account.getCards()) {
                        if (card.getCardNumber().equals(payOnlineCard)) {
                            if (card.getStatus().equals("frozen")) {
                                Transaction transaction = new ErrorPaymentTransaction(
                                        commandInput.getTimestamp(),
                                        "The card is frozen");
                                user.addTransaction(transaction);
                                account.addTransaction(transaction);
                            }
                            double amount = infoBank.exchange(commandInput.getCurrency(),
                                    account.getCurrency(), commandInput.getAmount());
                            PayStrategy onlinePayment = new OnlinePayment();
                            PaymentContext context = new PaymentContext(onlinePayment);
                            if (account.getBalance() >= amount && card.getStatus().
                                    equals("active")) {
                                enoughFunds = 1;
                            }
                            if (enoughFunds == 1 && card.getStatus().equals("active")) {
                                context.executePayment(account, amount);
                                Transaction transaction = new OnlinePayTransaction(
                                        commandInput.getTimestamp(),
                                        "Card payment", amount, commandInput.
                                        getCommerciant());
                                user.addTransaction(transaction);
                                account.addTransaction(transaction);
                                if (card.getClass() == OneTimeCard.class) {
                                    Transaction deleteTransaction = new DeleteCardTransaction(
                                            commandInput.getTimestamp(),
                                            "The card has been destroyed",
                                            card.getCardNumber(), user.getEmail(),
                                            account.getIban());
                                    user.addTransaction(deleteTransaction);
                                    account.addTransaction(deleteTransaction);
                                    card.setCardNumber(Utils.generateCardNumber());
                                    Transaction createTransaction = new NewCardTransaction(
                                            commandInput.getTimestamp(),
                                            "New card created",
                                            card.getCardNumber(), user.getEmail(),
                                            account.getIban());
                                    user.addTransaction(createTransaction);
                                    account.addTransaction(createTransaction);
                                }
                                for (Commerciant commerciant : account.getCommerciants()) {
                                    if (commerciant.getName().equals(
                                            commandInput.getCommerciant())) {
                                        comFound = 1;
                                    }
                                }
                                if (comFound == 0) {
                                    Commerciant commerciant = new Commerciant(0.0,
                                            commandInput.getCommerciant(),
                                            commandInput.getTimestamp());
                                    account.addCommerciant(commerciant);
                                }
                            }
                            if (enoughFunds == 0 && card.getStatus().equals("active")) {
                                Transaction transaction = new ErrorPaymentTransaction(
                                        commandInput.getTimestamp(),
                                        "Insufficient funds");
                                user.addTransaction(transaction);
                                account.addTransaction(transaction);
                            }
                            successPay = 1;
                        }
                    }
                }
            }
        }
        if (successPay == 0) {
            JsonOutput.errorCard(commandInput, objectMapper, output);
        }
    }
}
