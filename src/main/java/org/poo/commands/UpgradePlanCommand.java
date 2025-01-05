package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.bank.InfoBank;
import org.poo.bank.User;
import org.poo.errorTransactions.UpgradePlanTransactionError;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.transactions.UpgradePlanTransaction;

import java.sql.SQLOutput;

public class UpgradePlanCommand implements Command {
    public void execute(final CommandInput commandInput, final InfoBank infoBank,
                        final ObjectMapper objectMapper, final ArrayNode output) {
        boolean accFound = false;
        for (User user : infoBank.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(commandInput.getAccount())) {
                    accFound = true;
                    if (infoBank.getMap().get(account.getPlan()) != null && infoBank.getMap().get(commandInput.getNewPlanType()) != null) {
                        if (infoBank.getMap().get(account.getPlan()) <
                                infoBank.getMap().get(commandInput.getNewPlanType())) {
                            Transaction transaction = new UpgradePlanTransaction(commandInput.getTimestamp(),
                                    "Upgrade plan", account.getIban(), commandInput.getNewPlanType());
                            double exchangedAmount = 0.0;
                            if (account.getPlan().equals("standard") || account.getPlan().equals("student")) {
                                if (commandInput.getNewPlanType().equals("silver")) {
                                    exchangedAmount = infoBank.exchange("RON", account.getCurrency(), 100);
                                } else if (commandInput.getNewPlanType().equals("gold")){
                                    exchangedAmount = infoBank.exchange("RON", account.getCurrency(), 350);
                                }
                            } else if (account.getPlan().equals("silver")) {
                                if (commandInput.getNewPlanType().equals("gold")) {
                                    exchangedAmount = infoBank.exchange("RON", account.getCurrency(), 250);
                                }
                            }
                            if (exchangedAmount <= account.getBalance()) {
                                account.setBalance(account.getBalance() - exchangedAmount);
                                account.setBalance(Math.round(account.getBalance() * 100.0) / 100.0);
                                user.addTransaction(transaction);
                                for (Account acc_user : user.getAccounts()) {
                                    acc_user.setPlan(commandInput.getNewPlanType());
                                }
                            }
                        } else if (infoBank.getMap().get(account.getPlan()) == infoBank.getMap().get(commandInput.getNewPlanType())) {
                            Transaction transaction = new UpgradePlanTransactionError(commandInput.getTimestamp(),
                                    toString(commandInput.getNewPlanType()));
                            user.addTransaction(transaction);
                            account.addTransaction(transaction);
                        }
                    }
                }
            }
        }
    }
    public String toString(String newPlanType) {
        return "The user already has the " + newPlanType + " plan.";
    }
}
