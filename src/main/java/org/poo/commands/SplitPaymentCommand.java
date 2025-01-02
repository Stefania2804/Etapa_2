package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.errorTransactions.ErrorSplitPaymentTransaction;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;
import org.poo.bank.User;
import org.poo.transactions.SplitPaymentTransaction;
import org.poo.transactions.Transaction;

import java.util.Locale;

public final class SplitPaymentCommand implements Command {
    /**
     * functia de executare a platii distribuite intre mai multe conturi.
     */
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        int cnt = 0;
        double sum = commandInput.getAmount();
        int lenth = 0;
        String cardError = null;

        if (commandInput.getAccounts() != null) {
            lenth = commandInput.getAccounts().size();
        }
        double sumPerMember = sum / lenth;
        for (String ibanAccount : commandInput.getAccounts()) {
            for (Account account : infoBank.getAccounts()) {
                if (account.getIban().equals(ibanAccount)) {
                    double sumPerMemberExchanged = infoBank.exchange(
                            commandInput.getCurrency(),
                            account.getCurrency(), sumPerMember);
                    if (account.getBalance() >= sumPerMemberExchanged) {
                        cnt++;
                    } else {
                        cardError = account.getIban();
                    }
                }
            }
        }
        if (cnt == lenth) {
            for (User user : infoBank.getUsers()) {
                for (Account account : user.getAccounts()) {
                    for (String ibanAccSplit : commandInput.getAccounts()) {
                        if (account.getIban().equals(ibanAccSplit)) {
                            double sumPerMemberExchanged = infoBank.exchange(
                                    commandInput.getCurrency(),
                                    account.getCurrency(), sumPerMember);
                            account.setBalance(account.getBalance() - sumPerMemberExchanged);
                            String formattedValue = String.format(Locale.US, "%.2f", sum);
                            String description = splitPaymentToString(formattedValue,
                                    commandInput.getCurrency());
                            Transaction transactionSplit = new SplitPaymentTransaction(
                                    commandInput.getTimestamp(),
                                    description, commandInput.getCurrency(), sumPerMember,
                                    commandInput.getAccounts());
                            user.addTransaction(transactionSplit);
                            account.addTransaction(transactionSplit);
                        }
                    }
                }
            }
        } else {
            for (User user : infoBank.getUsers()) {
                for (Account account : user.getAccounts()) {
                    for (String ibanAccSplit : commandInput.getAccounts()) {
                        if (account.getIban().equals(ibanAccSplit)) {
                            String formattedValue = String.format(Locale.US,
                                    "%.2f", sum);
                            String description = splitPaymentToString(formattedValue,
                                    commandInput.getCurrency());
                            String error = errorSplitPayment(cardError);
                            Transaction transactionErrorSplit = new ErrorSplitPaymentTransaction(
                                    commandInput.getTimestamp(), description,
                                    commandInput.getCurrency(), sumPerMember,
                                    commandInput.getAccounts(), error);
                            user.addTransaction(transactionErrorSplit);
                            account.addTransaction(transactionErrorSplit);
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
