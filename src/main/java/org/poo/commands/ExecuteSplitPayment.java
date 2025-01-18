package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.bank.InfoBank;
import org.poo.bank.SplitPayment;
import org.poo.visitor.User;
import org.poo.errortransactions.ErrorSplitPaymentTransaction;
import org.poo.transactions.SplitPaymentTransaction;
import org.poo.transactions.Transaction;

import java.util.Locale;

public final class ExecuteSplitPayment {
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
        double sumPerMember = 0.0;
        double sumPerMemberExchanged = 0.0;
        if (splitPayment.getSplitPaymentType().equals("equal")) {
            sumPerMember = splitPayment.getAmount() / length;
        }
        if (splitPayment.getRejects() == 0) {
            for (String ibanAccount : splitPayment.getAccounts()) {
                for (Account account : infoBank.getAccounts()) {
                    if (account.getIban().equals(ibanAccount)) {
                        if (splitPayment.getSplitPaymentType().equals("equal")) {
                            sumPerMemberExchanged = infoBank.exchange(
                                    splitPayment.getCurrency(),
                                    account.getCurrency(), sumPerMember);
                        } else {
                            sumPerMemberExchanged = infoBank.exchange(
                                    splitPayment.getCurrency(),
                                    account.getCurrency(),
                                    splitPayment.getAmountForUsers().get(indexSum));
                        }
                        if (account.getBalance() >= sumPerMemberExchanged) {
                            cnt++;
                        } else {
                            cardError = account.getIban();
                            break;
                        }
                        indexSum++;
                    }
                }
                if (cardError != null) {
                    break;
                }
            }
        }
        if (cnt == length) {
            indexSum = 0;
            for (User user : infoBank.getUsers()) {
                for (Account account : user.getAccounts()) {
                    for (String ibanAccSplit : splitPayment.getAccounts()) {
                        if (account.getIban().equals(ibanAccSplit)) {
                            if (splitPayment.getSplitPaymentType().equals("equal")) {
                                sumPerMemberExchanged = infoBank.exchange(
                                        splitPayment.getCurrency(),
                                        account.getCurrency(), sumPerMember);
                            } else {
                                sumPerMemberExchanged = infoBank.exchange(
                                        splitPayment.getCurrency(),
                                        account.getCurrency(),
                                        splitPayment.getAmountForUsers().get(indexSum));
                            }
                            account.setBalance(account.getBalance() - sumPerMemberExchanged);
                            String formattedValue = String.format(
                                    Locale.US, "%.2f", sum);
                            String description = splitPaymentToString(formattedValue,
                                    splitPayment.getCurrency());
                            Double amount = null;

                            if (splitPayment.getSplitPaymentType().equals("equal")) {
                                amount = sumPerMember;
                            } else {
                                amount = null;
                            }
                            Transaction transactionSplit = new SplitPaymentTransaction(
                                    splitPayment.getTimestamp(),
                                    description, splitPayment.getSplitPaymentType(),
                                    splitPayment.getCurrency(), amount,
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
            for (User user : infoBank.getUsers()) {
                for (Account account : user.getAccounts()) {
                    for (String ibanAccSplit : splitPayment.getAccounts()) {
                        if (account.getIban().equals(ibanAccSplit)) {
                            String formattedValue = String.format(Locale.US,
                                    "%.2f", sum);
                            String description = splitPaymentToString(formattedValue,
                                    splitPayment.getCurrency());
                            String error = errorSplitPayment(cardError);

                            Double amount = null;
                            if (splitPayment.getSplitPaymentType().equals("equal")) {
                                amount = sumPerMember;
                            } else {
                                amount = null;
                            }
                            Transaction transactionErrorSplit = null;
                            if (splitPayment.getRejects() == 0) {
                                transactionErrorSplit = new ErrorSplitPaymentTransaction(
                                        splitPayment.getTimestamp(), description,
                                        splitPayment.getCurrency(), amount,
                                        splitPayment.getAccounts(), error,
                                        splitPayment.getSplitPaymentType(),
                                        splitPayment.getAmountForUsers());
                            } else {
                                transactionErrorSplit = new ErrorSplitPaymentTransaction(
                                        splitPayment.getTimestamp(), description,
                                        splitPayment.getCurrency(), amount,
                                        splitPayment.getAccounts(),
                                        "One user rejected the payment.",
                                        splitPayment.getSplitPaymentType(),
                                        splitPayment.getAmountForUsers());
                            }
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
