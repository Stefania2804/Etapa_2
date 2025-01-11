package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.Commerciant;
import org.poo.bank.InfoBank;
import org.poo.bank.Payment;
import org.poo.errortransactions.ErrorPaymentTransaction;
import org.poo.factory.CashBack;
import org.poo.factory.CashBackFactory;
import org.poo.fileio.CommandInput;
import org.poo.main.JsonOutput;
import org.poo.strategy.BankTransfer;
import org.poo.strategy.PayStrategy;
import org.poo.strategy.PaymentContext;
import org.poo.transactions.SendMoneyTransaction;
import org.poo.transactions.Transaction;
import org.poo.transactions.UpgradePlanTransaction;
import org.poo.visitor.User;

public abstract class ExecuteSendMoney {
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
        boolean commerciantReceiver = false;
        if (receiverFound == 0) {
            for (Commerciant commerciant : infoBank.getCommerciants()) {
                if (commerciant.getAccount().equals(receiver)) {
                    commerciantReceiver = true;
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
                    double exchangedAmount = infoBank.exchange(senderAccount.getCurrency(), "RON", commandInput.getAmount());
                    commerciant.setMoneySpent(commerciant.getMoneySpent() + exchangedAmount);
                    CashBack cashBackNew = CashBackFactory.getCashBack(commerciant.getCashBackType());
                    cashBackNew.calculate(infoBank, senderAccount, commandInput.getAmount(), commerciant);
                    CashBack cashBackNrOfTransactions = CashBackFactory.getCashBack("nrOfTransactions");
                    cashBackNrOfTransactions.calculate(infoBank, senderAccount, commandInput.getAmount(), commerciant);
                }
                commerciant.addTimestamp(commandInput.getTimestamp());
            }
        }
        if (commerciantFound == false) {
            for (Commerciant commerciant : infoBank.getCommerciants()) {
                if (commerciant.getAccount().equals(commandInput.getReceiver())) {
                    Commerciant copy = new Commerciant(commerciant);
                    if (commerciant.getCashBackType().equals("nrOfTransactions")) {
                        copy.setNrOfTransactions(1);

                    } else if (commerciant.getCashBackType().equals("spendingThreshold")) {
                        double exchangedAmount = infoBank.exchange(senderAccount.getCurrency(), "RON", commandInput.getAmount());
                        copy.setMoneySpent(exchangedAmount);
                        CashBack cashBack = CashBackFactory.getCashBack(commerciant.getCashBackType());
                        cashBack.calculate(infoBank, senderAccount, commandInput.getAmount(), copy);
                        CashBack cashBackNrOfTransactions = CashBackFactory.getCashBack("nrOfTransactions");
                        cashBackNrOfTransactions.calculate(infoBank, senderAccount, commandInput.getAmount(), copy);
                    }
                    commerciant.addTimestamp(commandInput.getTimestamp());
                    senderAccount.addCommerciant(copy);
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
        if (senderAccount.getPlan().equals("silver")) {
            double exchangedToRon = infoBank.exchange(senderCurrency, "RON", commandInput.getAmount());
            Payment payment = new Payment(exchangedToRon);
            userSender.addPayment(payment);
            int cnt = 0;
            for (Payment payment1 : userSender.getPayments()) {
                if (payment1.getAmount() >= 300) {
                    cnt++;
                }
            }
            if (cnt >= 5) {
                senderAccount.setPlan("gold");
                for (Account account : userSender.getAccounts()) {
                    account.setPlan("gold");
                }
                Transaction transaction = new UpgradePlanTransaction(commandInput.getTimestamp(),
                        "Upgrade plan", senderAccount.getIban(), "gold");
                userSender.addTransaction(transaction);
            }
        }
        if (commerciantReceiver == false) {
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
