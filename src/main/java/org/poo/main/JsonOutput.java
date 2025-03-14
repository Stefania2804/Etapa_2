package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.*;
import org.poo.bank.InfoBank;
import org.poo.visitor.Employee;
import org.poo.visitor.Manager;
import org.poo.visitor.User;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class JsonOutput {
    /**
     * Printeaza utilizatorii.
     *
     */
    public static void printUsers(final InfoBank infoBank, final CommandInput commandInput,
                                  final ObjectMapper objectMapper, final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", "printUsers");

        ArrayNode usersArray = objectMapper.createArrayNode();
        for (User user : infoBank.getUsers()) {
            usersArray.add(objectMapper.valueToTree(user));
        }

        commandNode.set("output", usersArray);
        commandNode.put("timestamp", commandInput.getTimestamp());
        output.add(commandNode);
    }
    /**
     * Sterge contul unui utilizator.
     *
     */
    public static void deleteAccount(final CommandInput commandInput,
                                     final ObjectMapper objectMapper,
                                     final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", "deleteAccount");
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("success", "Account deleted");
        outputNode.put("timestamp", commandInput.getTimestamp());

        commandNode.set("output", outputNode);
        commandNode.put("timestamp", commandInput.getTimestamp());
        output.add(commandNode);
    }
    /**
     * Afiseaza eroare la plata online cu cardul.
     *
     */
    public static void errorCard(final CommandInput commandInput,
                                          final ObjectMapper objectMapper, final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", commandInput.getCommand());
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("timestamp", commandInput.getTimestamp());
        outputNode.put("description", "Card not found");

        commandNode.set("output", outputNode);
        commandNode.put("timestamp", commandInput.getTimestamp());
        output.add(commandNode);
    }
    /**
     * Printeaza tranzactiile unui utilizator.
     *
     */
    public static void printTransactions(final CommandInput commandInput, final User user,
                                         final ObjectMapper objectMapper, final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", "printTransactions");

        ArrayNode transactionsArray = objectMapper.createArrayNode();
        List<Transaction> sortedTransactions = user.getTransactions().stream()
                .sorted(Comparator.comparing(Transaction::getTimestamp))
                .collect(Collectors.toList());

        for (Transaction transaction : sortedTransactions) {
            transactionsArray.add(objectMapper.valueToTree(transaction));
        }

        commandNode.set("output", transactionsArray);
        commandNode.put("timestamp", commandInput.getTimestamp());
        output.add(commandNode);
    }
    /**
     * Afiseaza eroare la stergerea unui cont cu balanta diferita de 0.
     *
     */
    public static void deleteAccountError(final CommandInput commandInput,
                                          final ObjectMapper objectMapper,
                                          final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", "deleteAccount");
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("error", "Account couldn't be deleted "
                + "- see org.poo.transactions for details");
        outputNode.put("timestamp", commandInput.getTimestamp());

        commandNode.set("output", outputNode);
        commandNode.put("timestamp", commandInput.getTimestamp());
        output.add(commandNode);
    }
    /**
     * Printeaza raportul clasic.
     *
     */
    public static void printClassicReport(final CommandInput commandInput,
                                          final Account account,
                                          final ObjectMapper objectMapper,
                                          final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", "report");

        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("IBAN", account.getIban());
        outputNode.put("balance", account.getBalance());
        outputNode.put("currency", account.getCurrency());

        ArrayNode transactionsArray = objectMapper.createArrayNode();
        for (Transaction transaction : account.getTransactions()) {
            if (transaction.getTimestamp() >= commandInput.getStartTimestamp()
                    && transaction.getTimestamp() <= commandInput.getEndTimestamp()) {
                transactionsArray.add(objectMapper.valueToTree(transaction));
            }
        }

        outputNode.set("transactions", transactionsArray);

        commandNode.set("output", outputNode);
        commandNode.put("timestamp", commandInput.getTimestamp());

        output.add(commandNode);
    }
    /**
     * Doar contul de economii poate avea dobanda.
     *
     */
    public static void interestRateError(final CommandInput commandInput,
                                         final ObjectMapper objectMapper,
                                         final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", commandInput.getCommand());
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("timestamp", commandInput.getTimestamp());
        outputNode.put("description", "This is not a savings account");

        commandNode.set("output", outputNode);
        commandNode.put("timestamp", commandInput.getTimestamp());
        output.add(commandNode);
    }
    /**
     * Printeaza raportul de cheltuieli al unui cont.
     *
     */
    public static void printSpendingsReport(final CommandInput commandInput,
                                            final Account account,
                                            final ObjectMapper objectMapper,
                                            final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", "spendingsReport");

        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("IBAN", account.getIban());
        outputNode.put("balance", account.getBalance());
        outputNode.put("currency", account.getCurrency());

        ArrayNode transactionsArray = objectMapper.createArrayNode();
        ArrayNode commerciantsArray = objectMapper.createArrayNode();
        for (Transaction transaction : account.getTransactions()) {
            if (transaction.getDescription().equals("Card payment")
                    && transaction.getTimestamp() >= commandInput.getStartTimestamp()
                    && transaction.getTimestamp() <= commandInput.getEndTimestamp()) {
                transactionsArray.add(objectMapper.valueToTree(transaction));
            }
        }
        for (Commerciant commerciant : account.getCommerciants()) {
            for (Integer time : commerciant.getPayOnlineTimestamps()) {
                if (time >= commandInput.getStartTimestamp() && time
                        <= commandInput.getEndTimestamp()) {
                    commerciantsArray.add(objectMapper.valueToTree(commerciant));
                    break;
                }
            }
        }
        outputNode.set("transactions", transactionsArray);
        outputNode.set("commerciants", commerciantsArray);

        commandNode.set("output", outputNode);
        commandNode.put("timestamp", commandInput.getTimestamp());

        output.add(commandNode);
        for (Commerciant commerciant : account.getCommerciants()) {
            for (Integer time : commerciant.getPayOnlineTimestamps()) {
                if (time >= commandInput.getStartTimestamp() && time
                        <= commandInput.getEndTimestamp()) {
                    commerciant.setAmount(0.0);
                    break;
                }
            }
        }
    }
    /**
     * Eroare pentru cont inexistent.
     *
     */
    public static void errorAccount(final CommandInput commandInput,
                                    final ObjectMapper objectMapper,
                                    final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", commandInput.getCommand());
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("timestamp", commandInput.getTimestamp());
        outputNode.put("description", "Account not found");

        commandNode.set("output", outputNode);
        commandNode.put("timestamp", commandInput.getTimestamp());
        output.add(commandNode);
    }
    /**
     * Eroare pentru raport de cheltuieli cerut pentru un cont de
     * economii.
     *
     */
    public static void errorSpendings(final CommandInput commandInput,
                                      final ObjectMapper objectMapper,
                                      final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", commandInput.getCommand());

        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("error", "This kind of report is not supported for a saving account");

        commandNode.set("output", outputNode);
        commandNode.put("timestamp", commandInput.getTimestamp());
        output.add(commandNode);
    }
    /**
     * Eroare pentru retragere numerar
     */
    public static void errorCashWithdrawal(final CommandInput commandInput,
                                           final ObjectMapper objectMapper,
                                           final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", commandInput.getCommand());
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("timestamp", commandInput.getTimestamp());
        outputNode.put("description", "Insufficient funds");

        commandNode.set("output", outputNode);
        commandNode.put("timestamp", commandInput.getTimestamp());
        output.add(commandNode);
    }
    /**
     * Eroare pentru utilizator negasit.
     */
    public static void errorUser(final CommandInput commandInput,
                                    final ObjectMapper objectMapper,
                                    final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", commandInput.getCommand());
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("timestamp", commandInput.getTimestamp());
        outputNode.put("description", "User not found");

        commandNode.set("output", outputNode);
        commandNode.put("timestamp", commandInput.getTimestamp());
        output.add(commandNode);
    }
    /**
     * Printare raport de afaceri.
     */
    public static void printBusinessReportTransaction(final CommandInput commandInput,
                                            final InfoBank infoBank,
                                            final Account account,
                                            final ObjectMapper objectMapper,
                                            final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", "businessReport");
        double exchanged = 0.0;
        double totalSpent = 0.0;
        double totalDeposited = 0.0;
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("IBAN", account.getIban());
        outputNode.put("balance", account.getBalance());
        outputNode.put("currency", account.getCurrency());
        outputNode.put("spending limit", ((BusinessAccount) account).getSpendingLimit());
        outputNode.put("deposit limit", ((BusinessAccount) account).getDepositLimit());
        outputNode.put("statistics type", commandInput.getType());
        ArrayNode managersArray = objectMapper.createArrayNode();
        ArrayNode employeesArray = objectMapper.createArrayNode();
        for (Manager manager : ((BusinessAccount) account).getManagers()) {
            ObjectNode managerNode = objectMapper.createObjectNode();
            managerNode.put("username", manager.getName());
            managerNode.put("spent", manager.getSpent());
            managerNode.put("deposited", manager.getDeposited());
            managersArray.add(managerNode);
            totalSpent = totalSpent + manager.getSpent();
            totalDeposited = totalDeposited + manager.getDeposited();
        }
        for (Employee employee : ((BusinessAccount) account).getEmployees()) {
            ObjectNode employeeNode = objectMapper.createObjectNode();
            employeeNode.put("username", employee.getName());
            employeeNode.put("spent", employee.getSpent());
            employeeNode.put("deposited", employee.getDeposited());
            employeesArray.add(employeeNode);
            totalSpent = totalSpent + employee.getSpent();
            totalDeposited = totalDeposited + employee.getDeposited();
        }
        outputNode.set("managers", managersArray);
        outputNode.set("employees", employeesArray);
        outputNode.put("total spent", totalSpent);
        outputNode.put("total deposited", totalDeposited);
        commandNode.set("output", outputNode);
        commandNode.put("timestamp", commandInput.getTimestamp());

        output.add(commandNode);
    }
    /**
     * Eroare pentru lipsa de autorizatie.
     */
    public static void spendingLimitError(final CommandInput commandInput,
                                         final ObjectMapper objectMapper,
                                         final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", commandInput.getCommand());
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("timestamp", commandInput.getTimestamp());
        outputNode.put("description", "You must be owner in "
                + "order to change spending limit.");

        commandNode.set("output", outputNode);
        commandNode.put("timestamp", commandInput.getTimestamp());
        output.add(commandNode);
    }
    /**
     * Afisare eroare schimbare limita depunere.
     *
     */
    public static void depositLimitError(final CommandInput commandInput,
                                          final ObjectMapper objectMapper,
                                          final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", commandInput.getCommand());
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("timestamp", commandInput.getTimestamp());
        outputNode.put("description", "You must be owner in order "
                + "to change deposit limit.");

        commandNode.set("output", outputNode);
        commandNode.put("timestamp", commandInput.getTimestamp());
        output.add(commandNode);
    }
    /**
     * Afisare eroare card business.
     *
     */
    public static void errorBusinessCard(final CommandInput commandInput,
                                          final ObjectMapper objectMapper,
                                          final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", commandInput.getCommand());
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("timestamp", commandInput.getTimestamp());
        outputNode.put("description", "This is not a business account");

        commandNode.set("output", outputNode);
        commandNode.put("timestamp", commandInput.getTimestamp());
        output.add(commandNode);
    }
    /**
     * Printare raport pentru comercianti.
     */
    public static void printBusinessReportCommerciant(final CommandInput commandInput,
                                                      final InfoBank infoBank,
                                                      final Account account,
                                                      final ObjectMapper objectMapper,
                                                      final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", "businessReport");
        double exchanged = 0.0;
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("IBAN", account.getIban());
        outputNode.put("balance", account.getBalance());
        outputNode.put("currency", account.getCurrency());
        outputNode.put("spending limit", ((BusinessAccount) account).getSpendingLimit());
        outputNode.put("deposit limit", ((BusinessAccount) account).getDepositLimit());
        outputNode.put("statistics type", commandInput.getType());
        ArrayNode commerciantsArray = objectMapper.createArrayNode();
        for (Commerciant commerciant : account.getCommerciants()) {
            ArrayNode managersArray = objectMapper.createArrayNode();
            ArrayNode employeesArray = objectMapper.createArrayNode();
            ObjectNode commerciantNode = objectMapper.createObjectNode();
            commerciantNode.put("commerciant", commerciant.getName());
            commerciantNode.put("total received", commerciant.getSpentBusiness());
            for (Client client : commerciant.getClients()) {
                for (Manager manager : ((BusinessAccount) account).getManagers()) {
                    if (manager.getEmail().equals(client.getEmail()) && client.getTimestamp()
                            >= commandInput.getStartTimestamp()
                            && client.getTimestamp() <= commandInput.getEndTimestamp()) {
                        managersArray.add(client.getName());
                    }
                }
            }
            for (Client client : commerciant.getClients()) {
                for (Employee employee : ((BusinessAccount) account).getEmployees()) {
                    if (employee.getEmail().equals(client.getEmail())
                            && client.getTimestamp() >= commandInput.getStartTimestamp()
                            && client.getTimestamp() <= commandInput.getEndTimestamp()) {
                        employeesArray.add(client.getName());
                    }
                }
            }
            commerciantNode.set("managers", managersArray);
            commerciantNode.set("employees", employeesArray);

            if (commerciant.getSpentBusiness() != 0) {
                commerciantsArray.add(commerciantNode);
            }
        }
        outputNode.set("commerciants", commerciantsArray);
        commandNode.set("output", outputNode);
        commandNode.put("timestamp", commandInput.getTimestamp());

        output.add(commandNode);
    }
}
