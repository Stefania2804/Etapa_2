package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.bank.InfoBank;
import org.poo.bank.SplitPayment;
import org.poo.bank.User;
import org.poo.errorTransactions.ErrorSplitPaymentTransaction;
import org.poo.fileio.CommandInput;
import org.poo.transactions.SplitPaymentTransaction;
import org.poo.transactions.Transaction;

import java.util.Locale;

public class ExecuteSplitPaymentCommand {
    /**
     * functia de executare a platii distribuite intre mai multe conturi.
     */
    public void execute(final SplitPayment splitPayment,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        int cnt = 0;
        int indexSum = 0;
        double sum = splitPayment.getAmount();
        int length = 0;
        if (splitPayment.getAccounts() != null) {
            length = splitPayment.getAccounts().size();
        }
        String cardError = null;
        if (splitPayment.getAmountForUsers() == null) {
            System.out.println(splitPayment.getTimestamp());
            System.out.println("aici");
            return;
        }
        for (String ibanAccount : splitPayment.getAccounts()) {
            for (Account account : infoBank.getAccounts()) {
                if (account.getIban().equals(ibanAccount)) {
                    double sumPerMemberExchanged = infoBank.exchange(
                            splitPayment.getCurrency(),
                            account.getCurrency(), splitPayment.getAmountForUsers().get(indexSum));
                    if (account.getBalance() >= sumPerMemberExchanged) {
                        cnt++;
                    } else {
                        cardError = account.getIban();
                    }
                    indexSum++;
                }
            }
        }
        if (cnt == length) {
            System.out.println(splitPayment.getAccepts());
            indexSum = 0;
            for (User user : infoBank.getUsers()) {
                for (Account account : user.getAccounts()) {
                    for (String ibanAccSplit : splitPayment.getAccounts()) {
                        if (account.getIban().equals(ibanAccSplit)) {
                            double sumPerMemberExchanged = infoBank.exchange(
                                    splitPayment.getCurrency(),
                                    account.getCurrency(), splitPayment.getAmountForUsers().get(indexSum));
                            account.setBalance(account.getBalance() - sumPerMemberExchanged);
                            String formattedValue = String.format(Locale.US, "%.2f", sum);
                            String description = splitPaymentToString(formattedValue,
                                    splitPayment.getCurrency());
                            Transaction transactionSplit = new SplitPaymentTransaction(
                                    splitPayment.getTimestamp(),
                                    description, splitPayment.getSplitPaymentType(),
                                    splitPayment.getCurrency(), splitPayment.getAmount(),
                                    splitPayment.getAmountForUsers(),
                                    splitPayment.getAccounts());
                            user.addTransaction(transactionSplit);
                            account.addTransaction(transactionSplit);
                            indexSum++;
                        }
                    }
                }
            }
        } else {
            System.out.println("aici intra");
            indexSum = 0;
            for (User user : infoBank.getUsers()) {
                for (Account account : user.getAccounts()) {
                    for (String ibanAccSplit : splitPayment.getAccounts()) {
                        if (account.getIban().equals(ibanAccSplit)) {
                            String formattedValue = String.format(Locale.US,
                                    "%.2f", sum);
                            String description = splitPaymentToString(formattedValue,
                                    splitPayment.getCurrency());
                            String error = errorSplitPayment(cardError);
                            Transaction transactionErrorSplit = new ErrorSplitPaymentTransaction(
                                    splitPayment.getTimestamp(), description,
                                    splitPayment.getCurrency(), splitPayment.getAmountForUsers().get(indexSum),
                                    splitPayment.getAccounts(), error);
                            user.addTransaction(transactionErrorSplit);
                            account.addTransaction(transactionErrorSplit);
                            indexSum++;
                        }
                    }
                }
            }
        }
    }
    /**
     * Descriere plata distribuita cu suma platita si moneda folosita.
     */
    public static String splitPaymentToString(final String amount,
                                              final String currency) {
        return "Split payment of " + amount + " " + currency;
    }
    /**
     * Eroare afisata pentru plata distribuita.
     */
    public static String errorSplitPayment(final String cardNumber) {
        return "Account " + cardNumber + " has insufficient funds for a split payment.";
    }

}
