package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.Commerciant;
import org.poo.factory.CashBack;
import org.poo.factory.CashBackFactory;
import org.poo.main.JsonOutput;
import org.poo.strategy.BankTransfer;
import org.poo.bank.InfoBank;
import org.poo.strategy.PaymentContext;
import org.poo.visitor.User;
import org.poo.errorTransactions.ErrorPaymentTransaction;
import org.poo.fileio.CommandInput;
import org.poo.strategy.PayStrategy;
import org.poo.transactions.SendMoneyTransaction;
import org.poo.transactions.Transaction;

public class SendMoneyCommand implements Command {
    /**
     * functia de executare pentru transferul bancar intre doua conturi.
     */
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        int senderFound = 0;
        int receiverFound = 0;
        String sender = commandInput.getAccount();
        String receiver = commandInput.getReceiver();
        String senderCurrency = new String();
        String receiverCurrency = new String();
        Account senderAccount = new Account(null, 0.0,
                null, null, null);
        Account receiverAccount = new Account(null, 0.0,
                null, null, null);
        User userSender = new User(null,
                null, null, null, null);
        User userReceiver = new User(null,
                null, null, null, null);
        for (User user : infoBank.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(sender)) {
                    senderCurrency = account.getCurrency();
                    senderAccount = account;
                    userSender = user;
                    senderFound = 1;
                }
                if (account.getIban().equals(receiver)
                        || account.getIban().equals(infoBank.getHashMap().
                        get(receiver))) {
                    receiverCurrency = account.getCurrency();
                    receiverAccount = account;
                    userReceiver = user;
                    receiverFound = 1;
                }
            }
        }
        if (senderFound == 0 || receiverFound == 0) {
            JsonOutput.errorUser(commandInput, objectMapper, output);
            return;
        }
        double exchangedAm = infoBank.exchange(senderCurrency, "RON", commandInput.getAmount());
        if (enoughFunds(commandInput.getAmount(), senderAccount, exchangedAm) == false) {
            Transaction transaction = new ErrorPaymentTransaction(
                    commandInput.getTimestamp(),
                    "Insufficient funds");
            userSender.addTransaction(transaction);
            senderAccount.addTransaction(transaction);
            return;
        }
        boolean commerciantFound = false;
        for (Commerciant commerciant : senderAccount.getCommerciants()) {
            if (commandInput.getReceiver().equals(commerciant.getAccount())) {
                commerciantFound = true;
                if (commerciant.getCashBackType().equals("nrOfTransactions")) {
                    commerciant.setNrOfTransactions(commerciant.getNrOfTransactions() + 1);
                } else if (commerciant.getCashBackType().equals("spendingThreshold")) {
                    double exchangedAmount = infoBank.exchange(commandInput.getCurrency(), "RON", commandInput.getAmount());
                    commerciant.setMoneySpent(commerciant.getMoneySpent() + exchangedAmount);
                    commerciant.setMoneySpent(Math.round(commerciant.getMoneySpent() * 100.0) / 100.0);
                    CashBack cashBackNew = CashBackFactory.getCashBack(commerciant.getCashBackType());
                    cashBackNew.calculate(infoBank, senderAccount, commandInput.getAmount(), commerciant);
                }
                commerciant.addTimestamp(commandInput.getTimestamp());
            }
        }
        if (commerciantFound == false) {
            for (Commerciant commerciant : infoBank.getCommerciants()) {
                if (commerciant.getAccount().equals(commandInput.getReceiver())) {
                    if (commerciant.getCashBackType().equals("nrOfTransactions")) {
                        commerciant.setNrOfTransactions(1);
                    } else if (commerciant.getCashBackType().equals("spendingThreshold")) {
                        double exchangedAmount = infoBank.exchange(commandInput.getCurrency(), "RON", commandInput.getAmount());
                        commerciant.setMoneySpent(exchangedAmount);
                        commerciant.setMoneySpent(Math.round(commerciant.getMoneySpent() * 100.0) / 100.0);
                        CashBack cashBack = CashBackFactory.getCashBack(commerciant.getCashBackType());
                        cashBack.calculate(infoBank, senderAccount, commandInput.getAmount(), commerciant);
                    }
                    commerciant.addTimestamp(commandInput.getTimestamp());
                    senderAccount.addCommerciant(commerciant);
                }
            }
        }
        double amount = infoBank.exchange(senderCurrency,
                receiverCurrency, commandInput.getAmount());
        PayStrategy bankTransfer = new BankTransfer(receiverAccount,
                amount, exchangedAm);
        PaymentContext context = new PaymentContext(bankTransfer);
        context.executePayment(senderAccount, commandInput.getAmount());
        Transaction transactionSent = new SendMoneyTransaction(
                commandInput.getTimestamp(),
                commandInput.getDescription(),
                sender, receiver, sendMoneyToString(commandInput.getAmount(),
                senderAccount.getCurrency()), "sent");
        Transaction transactionReceived = new SendMoneyTransaction(
                commandInput.getTimestamp(),
                commandInput.getDescription(),
                sender, receiver, sendMoneyToString(amount,
                receiverAccount.getCurrency()),
                "received");
        userSender.addTransaction(transactionSent);
        senderAccount.addTransaction(transactionSent);
        userReceiver.addTransaction(transactionReceived);
        receiverAccount.addTransaction(transactionReceived);
    }
    /**
     * afisare suma si moneda.
     */
    public static String sendMoneyToString(final double amount,
                                           final String currency) {
        return amount + " " + currency;
    }
    public static boolean enoughFunds(final double amount, final Account account, final double exchangedToRon) {
        double commission = 0.0;
        if (account.getPlan().equals("standard")) {
            commission = 0.002 * amount;
        } else if (account.getPlan().equals("silver") && exchangedToRon >= 500) {
            commission = 0.001 * amount;
        }
        if (account.getBalance() >= amount + commission) {
            return true;
        }
        return false;
    }
}
