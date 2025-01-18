package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.account.Commerciant;
import org.poo.bank.InfoBank;
import org.poo.fileio.CommandInput;
import org.poo.main.JsonOutput;
import org.poo.visitor.Employee;
import org.poo.visitor.Manager;


public final class BusinessReportCommand implements Command {
    /**
     * functia de executare pentru afisarea raportului pentru contul de afaceri.
     */
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        for (Account account : infoBank.getAccounts()) {
            if (account.getIban().equals(commandInput.getAccount())
                    && account.getClass() == BusinessAccount.class) {
                if (commandInput.getType().equals("transaction")) {
                    for (Integer key : ((BusinessAccount) account).getManagersMap().keySet()) {
                        if (key >= commandInput.getStartTimestamp()
                                && key <= commandInput.getEndTimestamp()) {
                            for (Manager manager : ((BusinessAccount) account).getManagers()) {
                                if (manager.getEmail().equals(
                                        ((BusinessAccount) account).getManagersMap().
                                                get(key).getEmail())) {
                                    if (((BusinessAccount) account).getManagersMap().get(key).
                                            getTransaction().equals("addFunds")) {
                                        manager.setDeposited(manager.getDeposited()
                                                + ((BusinessAccount) account).getManagersMap().
                                                get(key).getAmount());
                                    } else {
                                        manager.setSpent(manager.getSpent()
                                                + ((BusinessAccount) account).getManagersMap().
                                                get(key).getAmount());
                                    }
                                }
                            }
                        }
                    }
                    for (Integer key : ((BusinessAccount) account).getEmployeesMap().keySet()) {
                        if (key >= commandInput.getStartTimestamp()
                                && key <= commandInput.getEndTimestamp()) {
                            for (Employee employee : ((BusinessAccount) account).getEmployees()) {
                                if (employee.getEmail().equals(((BusinessAccount) account).
                                        getEmployeesMap().get(key).getEmail())) {
                                    if (((BusinessAccount) account).getEmployeesMap().get(key).
                                            getTransaction().equals("addFunds")) {
                                        employee.setDeposited(employee.getDeposited()
                                                + ((BusinessAccount) account).getEmployeesMap().
                                                get(key).getAmount());
                                    } else {
                                        employee.setSpent(employee.getSpent()
                                                + ((BusinessAccount) account).getEmployeesMap().
                                                get(key).getAmount());
                                    }
                                }
                            }
                        }
                    }
                    JsonOutput.printBusinessReportTransaction(
                            commandInput, infoBank, account,
                            objectMapper, output);
                    for (Employee employee : ((BusinessAccount) account).getEmployees()) {
                        employee.setDeposited(0.0);
                        employee.setSpent(0.0);
                    }
                    for (Manager manager : ((BusinessAccount) account).getManagers()) {
                        manager.setDeposited(0.0);
                        manager.setSpent(0.0);
                    }
                } else {
                    for (Integer key : ((BusinessAccount) account).getCommerciantsMap().
                            keySet()) {
                        if (key >= commandInput.getStartTimestamp()
                                && key <= commandInput.getEndTimestamp()) {
                            for (Commerciant commerciant : account.getCommerciants()) {
                                if (commerciant.getName().equals(
                                        ((BusinessAccount) account).getCommerciantsMap().
                                                get(key).getCommerciant())) {
                                    commerciant.setSpentBusiness(commerciant.getSpentBusiness()
                                            + ((BusinessAccount) account).getCommerciantsMap().
                                                    get(key).getAmount());
                                }
                            }
                        }
                    }
                    account.getCommerciants().sort((
                            p1, p2) -> p1.
                            getName().compareTo(p2.getName()));
                    for (Commerciant commerciant : account.getCommerciants()) {
                        commerciant.getClients().sort((
                                p1, p2) -> p1.getName().
                                compareTo(p2.getName()));
                    }
                    JsonOutput.printBusinessReportCommerciant(
                            commandInput, infoBank, account, objectMapper, output);
                    for (Commerciant commerciant : account.
                            getCommerciants()) {
                        commerciant.setSpentBusiness(0.0);
                    }
                }
            }
        }
    }
}
