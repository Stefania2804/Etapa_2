package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.*;
import org.poo.bank.InfoBank;
import org.poo.bank.Payment;
import org.poo.errortransactions.ErrorPaymentTransaction;
import org.poo.factory.CashBack;
import org.poo.factory.CashBackFactory;
import org.poo.fileio.CommandInput;
import org.poo.main.Constants;
import org.poo.main.JsonOutput;
import org.poo.strategy.BankTransfer;
import org.poo.strategy.PayStrategy;
import org.poo.strategy.PaymentContext;
import org.poo.transactions.SendMoneyTransaction;
import org.poo.transactions.Transaction;
import org.poo.transactions.UpgradePlanTransaction;
import org.poo.visitor.User;

public abstract class ExecuteSendMoney {
    /**
     * functia de executare pentru transferuri.
     */
    public static void execute(final CommandInput commandInput,
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
                if (account.getIban().equals(sender)
                        || account.getIban().equals(infoBank.getHashMap().
                        get(sender))) {
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
        boolean commerciantReceiver = false;
        if (receiverFound == 0) {
            for (Commerciant commerciant : infoBank.getCommerciants()) {
                if (commerciant.getAccount().equals(receiver)
                        || commerciant.getAccount().equals(infoBank.getHashMap().
                        get(receiver))) {
                    commerciantReceiver = true;
                    receiverFound = 1;
                }
            }
        }
        if (senderFound == 0 || receiverFound == 0) {
            JsonOutput.errorUser(commandInput, objectMapper, output);
            return;
        }
        double exchangedAm = infoBank.exchange(senderCurrency,
                "RON", commandInput.getAmount());
        if (!enoughFunds(commandInput.getAmount(),
                senderAccount, exchangedAm)) {
            Transaction transaction = new ErrorPaymentTransaction(
                    commandInput.getTimestamp(),
                    "Insufficient funds");
            userSender.addTransaction(transaction);
            senderAccount.addTransaction(transaction);
        } else {
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
            userSender.addTransaction(transactionSent);
            senderAccount.addTransaction(transactionSent);
            boolean commerciantFound = false;
            if (commerciantReceiver) {
                String commerciantName = null;
                Commerciant payCommerciant = null;
                for (Commerciant commerciant : senderAccount.getCommerciants()) {
                    if (commandInput.getReceiver().equals(
                            commerciant.getAccount())) {
                        commerciantFound = true;
                        commerciantName = commerciant.getName();
                        payCommerciant = commerciant;
                        if (commerciant.getCashBackType().equals("nrOfTransactions")) {
                            commerciant.setNrOfTransactions(
                                    commerciant.getNrOfTransactions() + 1);
                            commerciant.addTimestamp(commandInput.getTimestamp());
                            CashBack cashBackNrOfTransactions = CashBackFactory.getCashBack(
                                    "nrOfTransactions");
                            cashBackNrOfTransactions.calculate(infoBank, senderAccount,
                                    commandInput.getAmount(), commerciant);
                        } else if (commerciant.getCashBackType().equals("spendingThreshold")) {
                            double exchangedAmount = infoBank.exchange(senderAccount.getCurrency(),
                                    "RON", commandInput.getAmount());
                            commerciant.setMoneySpent(commerciant.getMoneySpent()
                                    + exchangedAmount);
                            commerciant.addTimestamp(commandInput.getTimestamp());
                            CashBack cashBackNew = CashBackFactory.getCashBack(
                                    commerciant.getCashBackType());
                            cashBackNew.calculate(infoBank, senderAccount,
                                    commandInput.getAmount(), commerciant);
                            CashBack cashBackNrOfTransactions = CashBackFactory.getCashBack(
                                    "nrOfTransactions");
                            cashBackNrOfTransactions.calculate(infoBank,
                                    senderAccount, commandInput.getAmount(), commerciant);
                        }
                    }
                }
                if (!commerciantFound) {
                    for (Commerciant commerciant : infoBank.getCommerciants()) {
                        if (commerciant.getAccount().equals(commandInput.getReceiver())) {
                            commerciantName = commerciant.getName();
                            Commerciant copy = new Commerciant(commerciant);
                            if (commerciant.getCashBackType().equals("nrOfTransactions")) {
                                double exchangedAmount = infoBank.exchange(
                                        senderAccount.getCurrency(),
                                        "RON", commandInput.getAmount());
                                copy.setMoneySpent(exchangedAmount);
                                copy.setNrOfTransactions(1);
                                copy.addTimestamp(commandInput.getTimestamp());
                                senderAccount.addCommerciant(copy);
                                CashBack cashBack = CashBackFactory.getCashBack(
                                        commerciant.getCashBackType());
                                cashBack.calculate(infoBank, senderAccount,
                                        commandInput.getAmount(), commerciant);
                            } else if (commerciant.getCashBackType().equals(
                                    "spendingThreshold")) {
                                double exchangedAmount = infoBank.exchange(
                                        senderAccount.getCurrency(), "RON",
                                        commandInput.getAmount());
                                copy.setMoneySpent(exchangedAmount);
                                copy.addTimestamp(commandInput.getTimestamp());
                                senderAccount.addCommerciant(copy);
                                CashBack cashBack = CashBackFactory.getCashBack(
                                        commerciant.getCashBackType());
                                cashBack.calculate(infoBank, senderAccount,
                                        commandInput.getAmount(), commerciant);
                                CashBack cashBackNrOfTransactions = CashBackFactory.getCashBack(
                                        "nrOfTransactions");
                                cashBackNrOfTransactions.calculate(infoBank, senderAccount,
                                        commandInput.getAmount(), commerciant);
                            }
                            payCommerciant = copy;
                        }
                    }
                }
                if (senderAccount.getType().equals("business")
                        && !userSender.getEmail().equals(((BusinessAccount) senderAccount).
                        getOwner().getEmail())) {
                    Pair pair = new Pair(commandInput.getAmount(),
                            commandInput.getEmail(), "sendMoney");
                    pair.setCommerciant(commerciantName);
                    Client client = new Client(userSender.getFullName(),
                            commandInput.getTimestamp(), userSender.getEmail());
                    payCommerciant.addClient(client);
                    ((BusinessAccount) senderAccount).addCommerciantReceiving(pair,
                            commandInput.getTimestamp());
                }
            }

            if (senderAccount.getPlan().equals("silver")) {
                double exchangedToRon = infoBank.exchange(senderCurrency,
                        "RON", commandInput.getAmount());
                Payment payment = new Payment(exchangedToRon);
                userSender.addPayment(payment);
                int cnt = 0;
                for (Payment payment1 : userSender.getPayments()) {
                    if (payment1.getAmount()
                            >= Constants.MINIMSPENT.getValue()) {
                        cnt++;
                    }
                }
                if (cnt >= 5) {
                    senderAccount.setPlan("gold");
                    for (Account account : userSender.getAccounts()) {
                        account.setPlan("gold");
                    }
                    Transaction transaction = new UpgradePlanTransaction(
                            commandInput.getTimestamp(),
                            "Upgrade plan", senderAccount.getIban(),
                            "gold");
                    userSender.addTransaction(transaction);
                    senderAccount.addTransaction(transaction);
                }
            }
            if (!commerciantReceiver) {
                Transaction transactionReceived = new SendMoneyTransaction(
                        commandInput.getTimestamp(),
                        commandInput.getDescription(),
                        sender, receiver, sendMoneyToString(amount,
                        receiverAccount.getCurrency()),
                        "received");
                userReceiver.addTransaction(transactionReceived);
                receiverAccount.addTransaction(transactionReceived);
            }
        }
    }
    /**
     * afisare suma si moneda.
     */
    public static String sendMoneyToString(final double amount,
                                           final String currency) {
        return amount + " " + currency;
    }
    /**
     * Verificare pentru fonduri suficiente.
     *
     */
    public static boolean enoughFunds(final double amount,
                                      final Account account,
                                      final double exchangedToRon) {
        double commission = 0.0;
        if (account.getPlan().equals("standard")) {
            commission = Constants.COMISION02.getValue() * amount;
        } else if (account.getPlan().equals("silver")
                && exchangedToRon >= Constants.COMISIONLIMIT.getValue()) {
            commission = Constants.COMISION01.getValue() * amount;
        }
        if (account.getBalance() >= amount + commission) {
            return true;
        }
        return false;
    }
}
