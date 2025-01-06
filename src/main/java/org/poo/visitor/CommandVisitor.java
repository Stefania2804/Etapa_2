package org.poo.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.Business;
import org.poo.bank.InfoBank;
import org.poo.commands.ExecuteOnlinePayment;
import org.poo.fileio.CommandInput;
import org.poo.strategy.OnlinePayment;
import org.poo.strategy.PayStrategy;
import org.poo.strategy.PaymentContext;


public class CommandVisitor implements Visitor {
    public void visitUser(User user, CommandInput commandInput, InfoBank infoBank, Account account,
                          ObjectMapper objectMapper, ArrayNode output) {
        return;
    }

    public void visitOwner(Owner owner, CommandInput commandInput, InfoBank infoBank, Account account,
                           ObjectMapper objectMapper, ArrayNode output) {
        switch (commandInput.getCommand()) {
            case "addAccount":
                System.out.println("Owner " + owner.getEmail() + " added a new account.");
                break;
            case "addNewBusinessAssociate":
                System.out.println("Owner " + owner.getEmail() + " added a new associate.");
                break;
            case "changeSpendingLimit":
                System.out.println("Owner " + owner.getEmail() + " changed spending limit.");
                break;
            case "changeDepositLimit":
                System.out.println("Owner " + owner.getEmail() + " changed deposit limit.");
                break;
            default:
                System.out.println("Invalid command for Owner.");
        }
    }
    public void visitEmployee(Employee employee, CommandInput commandInput, InfoBank infoBank, Account account,
                              ObjectMapper objectMapper, ArrayNode output) {
        switch (commandInput.getCommand()) {
            case "payOnline":
                double exhangedToRon = infoBank.exchange(commandInput.getCurrency(), "RON", commandInput.getAmount());
                double exchangedAmount = infoBank.exchange(commandInput.getCurrency(), account.getCurrency(), commandInput.getAmount());
                if (exhangedToRon <= ((Business) account).getSpendingLimit() && account.getBalance() >= exchangedAmount) {
                    employee.setSpent(employee.getSpent() + exchangedAmount);
                    ExecuteOnlinePayment.execute(commandInput, infoBank, objectMapper, output);
                }
                break;
            case "sendMoney":
                break;
            case "addFunds":
                double exhangedToRonFunds = infoBank.exchange(account.getCurrency(), "RON", commandInput.getAmount());
                if (exhangedToRonFunds <= ((Business) account).getDepositLimit()) {
                    employee.setDeposited(employee.getDeposited() + commandInput.getAmount());
                    account.setBalance(account.getBalance() + commandInput.getAmount());
                    account.setBalance(Math.round(account.getBalance() * 100.000) / 100.00);
                }
                break;
            default:
                System.out.println("Invalid command for Employee.");
        }
    }
    public void visitManager(Manager manager, CommandInput commandInput, InfoBank infoBank, Account account,
                             ObjectMapper objectMapper, ArrayNode output) {
        switch (commandInput.getCommand()) {
            case "payOnline":
                double exchangedAmount = infoBank.exchange(commandInput.getCurrency(), account.getCurrency(), commandInput.getAmount());
                if (exchangedAmount <= account.getBalance()) {
                    manager.setSpent(manager.getSpent() + exchangedAmount);
                    ExecuteOnlinePayment.execute(commandInput, infoBank, objectMapper, output);
                }
                break;
            case "sendMoney":
                break;
            case "addFunds":
                manager.setDeposited(manager.getDeposited() + commandInput.getAmount());
                account.setBalance(account.getBalance() + commandInput.getAmount());
                account.setBalance(Math.round(account.getBalance() * 100.000) / 100.00);
                break;
            default:
                System.out.println("Invalid command for Manager.");
        }
    }
}
