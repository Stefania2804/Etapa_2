package org.poo.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.Business;
import org.poo.bank.InfoBank;
import org.poo.commands.ExecuteOnlinePayment;
import org.poo.commands.ExecuteSendMoney;
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
        return;
    }
    public void visitEmployee(Employee employee, CommandInput commandInput, InfoBank infoBank, Account account,
                              ObjectMapper objectMapper, ArrayNode output) {
        switch (commandInput.getCommand()) {
            case "payOnline":
                double exhangedToRon = infoBank.exchange(commandInput.getCurrency(), "RON", commandInput.getAmount());
                double exchangedAmount = infoBank.exchange(commandInput.getCurrency(), account.getCurrency(), commandInput.getAmount());
                if (exhangedToRon <= ((Business) account).getSpendingLimit() && ExecuteOnlinePayment.enoughFunds(exchangedAmount,
                        account, exhangedToRon) == true) {
                    employee.setSpent(employee.getSpent() + exchangedAmount);
                    ExecuteOnlinePayment.execute(commandInput, infoBank, objectMapper, output);
                }
                break;
            case "sendMoney":
                double exchangedToRon = infoBank.exchange(account.getCurrency(), "RON", commandInput.getAmount());
                if (ExecuteSendMoney.enoughFunds(commandInput.getAmount(),
                        account, exchangedToRon) == true
                        && commandInput.getAmount() <= ((Business) account).getSpendingLimit()) {
                    employee.setSpent(employee.getSpent() + commandInput.getAmount());
                    ExecuteSendMoney.execute(commandInput, infoBank, objectMapper, output);
                }
                break;
            case "addFunds":
                double exhangedToRonFunds = infoBank.exchange(account.getCurrency(), "RON", commandInput.getAmount());
                if (exhangedToRonFunds <= ((Business) account).getDepositLimit()) {
                    employee.setDeposited(employee.getDeposited() + commandInput.getAmount());
                    account.setBalance(account.getBalance() + commandInput.getAmount());
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
                double exhangedToRon = infoBank.exchange(commandInput.getCurrency(), "RON", commandInput.getAmount());
                double exchangedAmount = infoBank.exchange(commandInput.getCurrency(), account.getCurrency(), commandInput.getAmount());
                if (ExecuteOnlinePayment.enoughFunds(exchangedAmount,
                        account, exhangedToRon) == true) {
                    manager.setSpent(manager.getSpent() + exchangedAmount);
                    ExecuteOnlinePayment.execute(commandInput, infoBank, objectMapper, output);
                }
                break;
            case "sendMoney":
                double exchangedToRon = infoBank.exchange(account.getCurrency(), "RON", commandInput.getAmount());
                if (ExecuteSendMoney.enoughFunds(commandInput.getAmount(),
                        account, exchangedToRon) == true) {
                    manager.setSpent(manager.getSpent() + commandInput.getAmount());
                    ExecuteSendMoney.execute(commandInput, infoBank, objectMapper, output);
                }
                break;
            case "addFunds":
                manager.setDeposited(manager.getDeposited() + commandInput.getAmount());
                account.setBalance(account.getBalance() + commandInput.getAmount());
                break;
            default:
                System.out.println("Invalid command for Manager.");
        }
    }
}
