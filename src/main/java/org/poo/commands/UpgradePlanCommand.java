package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.bank.InfoBank;
import org.poo.bank.Payment;
import org.poo.errortransactions.ErrorPaymentTransaction;
import org.poo.main.Constants;
import org.poo.main.JsonOutput;
import org.poo.visitor.User;
import org.poo.errortransactions.UpgradePlanTransactionError;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.transactions.UpgradePlanTransaction;

public final class UpgradePlanCommand implements Command {
    /**
     * upgrade la planul utilizatorului.
     */
    public void execute(final CommandInput commandInput, final InfoBank infoBank,
                        final ObjectMapper objectMapper, final ArrayNode output) {
        boolean accFound = false;
        for (User user : infoBank.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(commandInput.getAccount())) {
                    accFound = true;
                    if (infoBank.getMap().get(account.getPlan()) != null && infoBank.getMap().
                            get(commandInput.getNewPlanType()) != null) {
                        if (infoBank.getMap().get(account.getPlan())
                                < infoBank.getMap().get(commandInput.getNewPlanType())) {
                            Transaction transaction = new UpgradePlanTransaction(
                                    commandInput.getTimestamp(),
                                    "Upgrade plan", account.getIban(),
                                    commandInput.getNewPlanType());
                            double exchangedAmount = 0.0;
                            if (account.getPlan().equals("standard") || account.getPlan().
                                    equals("student")) {
                                if (commandInput.getNewPlanType().equals("silver")) {
                                    exchangedAmount = infoBank.exchange("RON",
                                            account.getCurrency(),
                                            Constants.UPGRADESILVER.getValue());
                                } else if (commandInput.getNewPlanType().equals("gold")) {
                                    exchangedAmount = infoBank.exchange("RON",
                                            account.getCurrency(),
                                            Constants.UPGRADEGOLD1.getValue());
                                }
                            } else if (account.getPlan().equals("silver")) {
                                int cnt = 0;
                                if (commandInput.getNewPlanType().equals("gold")) {
                                    for (Payment payment : user.getPayments()) {
                                        if (payment.getAmount()
                                                >= Constants.MINIMSPENT.getValue()) {
                                            cnt++;
                                        }
                                    }
                                    if (cnt >= 5) {
                                        exchangedAmount = 0.0;
                                    } else {
                                        exchangedAmount = infoBank.exchange("RON",
                                                account.getCurrency(),
                                                Constants.UPGRADEGOLD2.getValue());
                                    }
                                }
                            }
                            if (exchangedAmount <= account.getBalance()) {
                                account.setBalance(account.getBalance() - exchangedAmount);
                                user.addTransaction(transaction);
                                for (Account accUser : user.getAccounts()) {
                                    accUser.setPlan(commandInput.getNewPlanType());
                                    accUser.addTransaction(transaction);
                                }
                                account.setPlan(commandInput.getNewPlanType());
                            } else {
                                Transaction transactionError = new ErrorPaymentTransaction(
                                        commandInput.getTimestamp(),
                                        "Insufficient funds");
                                user.addTransaction(transactionError);
                                account.addTransaction(transactionError);
                            }
                        } else if (infoBank.getMap().get(account.getPlan()).equals(
                                infoBank.getMap().get(commandInput.getNewPlanType()))) {
                            Transaction transaction = new UpgradePlanTransactionError(
                                    commandInput.getTimestamp(),
                                    toString(commandInput.getNewPlanType()));
                            user.addTransaction(transaction);
                            account.addTransaction(transaction);
                        }
                    }
                }
            }
        }
        if (!accFound) {
            JsonOutput.errorAccount(commandInput, objectMapper, output);
        }
    }
    /**
     * functie pentru afisarea upgrade-ului.
     */
    public String toString(final String newPlanType) {
        return "The user already has the " + newPlanType + " plan.";
    }
}
