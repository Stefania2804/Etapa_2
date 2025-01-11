package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.Commerciant;
import org.poo.account.card.Card;
import org.poo.account.card.OneTimeCard;
import org.poo.bank.InfoBank;
import org.poo.bank.Payment;
import org.poo.errortransactions.ErrorPaymentTransaction;
import org.poo.factory.CashBack;
import org.poo.factory.CashBackFactory;
import org.poo.fileio.CommandInput;
import org.poo.main.JsonOutput;
import org.poo.strategy.OnlinePayment;
import org.poo.strategy.PayStrategy;
import org.poo.strategy.PaymentContext;
import org.poo.transactions.*;
import org.poo.utils.Utils;
import org.poo.visitor.User;

public abstract class ExecuteOnlinePayment {
    public static void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        int enoughFunds = 0;
        int successPay = 0;
        String payOnlineEmail = commandInput.getEmail();
        String payOnlineCard = commandInput.getCardNumber();
        for (User user : infoBank.getUsers()) {
            if (user.getEmail().equals(payOnlineEmail)) {
                for (Account account : infoBank.getAccounts()) {
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
                                double exchangedAm = infoBank.exchange(account.getCurrency(), "RON", amount);
                                PayStrategy onlinePayment = new OnlinePayment(exchangedAm);
                                PaymentContext context = new PaymentContext(onlinePayment);
                                if (enoughFunds(amount, account, exchangedAm) == true && card.getStatus().
                                        equals("active")) {
                                    enoughFunds = 1;
                                }
                                if (enoughFunds == 1 && card.getStatus().equals("active")) {
                                    if (commandInput.getAmount() != 0) {
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
                                            if (commandInput.getCommerciant().equals(commerciant.getName())
                                                    || commandInput.getCommerciant().equals(commerciant.getId())) {
                                                comFound = 1;
                                                if (commerciant.getCashBackType().equals("nrOfTransactions")) {
                                                    commerciant.setNrOfTransactions(commerciant.getNrOfTransactions() + 1);
                                                    CashBack cashBack = CashBackFactory.getCashBack(commerciant.getCashBackType());
                                                    cashBack.calculate(infoBank, account, amount, commerciant);
                                                } else if (commerciant.getCashBackType().equals("spendingThreshold")) {
                                                    double exchangedAmount = infoBank.exchange(commandInput.getCurrency(), "RON", commandInput.getAmount());
                                                    commerciant.setMoneySpent(commerciant.getMoneySpent() + exchangedAmount);
                                                    CashBack cashBackNew = CashBackFactory.getCashBack(commerciant.getCashBackType());
                                                    cashBackNew.calculate(infoBank, account, amount, commerciant);
                                                    CashBack cashBackNrOfTransactions = CashBackFactory.getCashBack("nrOfTransactions");
                                                    cashBackNrOfTransactions.calculate(infoBank, account, amount, commerciant);
                                                }
                                                commerciant.addTimestamp(commandInput.getTimestamp());
                                            }
                                        }
                                        if (comFound == 0) {
                                            for (Commerciant commerciant : infoBank.getCommerciants()) {
                                                if (commandInput.getCommerciant().equals(commerciant.getName())
                                                        || commandInput.getCommerciant().equals(commerciant.getId())) {
                                                    Commerciant copy = new Commerciant(commerciant);
                                                    if (commerciant.getCashBackType().equals("nrOfTransactions")) {
                                                        copy.setNrOfTransactions(1);
                                                        copy.addTimestamp(commandInput.getTimestamp());
                                                        account.addCommerciant(copy);
                                                        CashBack cashBack = CashBackFactory.getCashBack(commerciant.getCashBackType());
                                                        cashBack.calculate(infoBank, account, amount, copy);
                                                    } else if (commerciant.getCashBackType().equals("spendingThreshold")) {
                                                        double exchangedAmount = infoBank.exchange(commandInput.getCurrency(), "RON", commandInput.getAmount());
                                                        copy.setMoneySpent(exchangedAmount);
                                                        copy.addTimestamp(commandInput.getTimestamp());
                                                        account.addCommerciant(copy);
                                                        CashBack cashBack = CashBackFactory.getCashBack(commerciant.getCashBackType());
                                                        cashBack.calculate(infoBank, account, amount, copy);
                                                        CashBack cashBackNrOfTransactions = CashBackFactory.getCashBack("nrOfTransactions");
                                                        cashBackNrOfTransactions.calculate(infoBank, account, amount, copy);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (account.getPlan().equals("silver")) {
                                        double exchangedToRon = infoBank.exchange(commandInput.getCurrency(), "RON", commandInput.getAmount());
                                        Payment payment = new Payment(exchangedToRon);
                                        user.addPayment(payment);
                                        int cnt = 0;
                                        for (Payment payment1 : user.getPayments()) {
                                            if (payment1.getAmount() >= 300) {
                                                cnt++;
                                            }
                                        }
                                        if (cnt >= 5) {
                                            if (account.getIban().equals("RO00POOB5687892910835215")) {
                                                System.out.println("Upgrade la " + account.getPlan() + " automat");
                                            }
                                            account.setPlan("gold");
                                            for (Account acc : user.getAccounts()) {
                                                acc.setPlan("gold");
                                            }
                                            Transaction transactionUpgrade = new UpgradePlanTransaction(commandInput.getTimestamp(),
                                                    "Upgrade plan", account.getIban(), "gold");
                                            user.addTransaction(transactionUpgrade);
                                        }
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
