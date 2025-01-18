package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.card.Card;
import org.poo.bank.InfoBank;
import org.poo.main.Constants;
import org.poo.visitor.User;
import org.poo.errortransactions.ErrorPaymentTransaction;
import org.poo.fileio.CommandInput;
import org.poo.main.JsonOutput;
import org.poo.strategy.CashWithdrawal;
import org.poo.strategy.PayStrategy;
import org.poo.strategy.PaymentContext;
import org.poo.transactions.CashWithdrawalTransaction;
import org.poo.transactions.Transaction;

public final class CashWithdrawalCommand implements Command {
    /**
     * functia de executare pentru retrageri de la bancomat.
     */
    public void execute(final CommandInput commandInput, final InfoBank infoBank,
                        final ObjectMapper objectMapper, final ArrayNode output) {
        boolean userFound = false;
        boolean accFound = false;
        for (User user : infoBank.getUsers()) {
            if (user.getEmail().equals(commandInput.getEmail())) {
                userFound = true;
                for (Account account : user.getAccounts()) {
                    for (Card card : account.getCards()) {
                        if (card.getCardNumber().equals(commandInput.getCardNumber())) {
                            accFound = true;
                            double exchangedAmount = infoBank.exchange("RON", account.getCurrency(),
                                    commandInput.getAmount());
                            if (enoughFunds(exchangedAmount, account,
                                    commandInput.getAmount())) {
                                PayStrategy cashWithdrawalPay = new CashWithdrawal(
                                        commandInput.getAmount());
                                PaymentContext context = new PaymentContext(cashWithdrawalPay);
                                context.executePayment(account, exchangedAmount);
                                Transaction transaction = new CashWithdrawalTransaction(
                                        commandInput.getTimestamp(),
                                        toString(commandInput.getAmount()),
                                        commandInput.getAmount());
                                user.addTransaction(transaction);
                                account.addTransaction(transaction);
                            } else {
                                Transaction transaction = new ErrorPaymentTransaction(
                                        commandInput.getTimestamp(),
                                        "Insufficient funds");
                                user.addTransaction(transaction);
                                account.addTransaction(transaction);
                            }
                        }
                    }
                }
            }
        }
        if (!userFound) {
            JsonOutput.errorUser(commandInput, objectMapper, output);
        } else if (!accFound) {
            JsonOutput.errorCard(commandInput, objectMapper, output);
        }
    }
    /**
     * Scrie suma retrasa.
     */
    public String toString(final double amount) {
        return "Cash withdrawal of " + amount;
    }
    /**
     * Verifica daca sunt suficiente fonsuri inainte de retragere.
     */
    public static boolean enoughFunds(final double amount,
                                      final Account account, final double exchangedToRon) {
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
