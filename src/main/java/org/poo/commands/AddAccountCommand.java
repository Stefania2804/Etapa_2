package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.account.ClassicAccount;
import org.poo.account.SavingsAccount;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;
import org.poo.main.Constants;
import org.poo.visitor.Owner;
import org.poo.visitor.User;
import org.poo.transactions.NewAccTransaction;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

public final class AddAccountCommand implements Command {
    /**
     * functie pentru adaugarea unui cont nou.
     */
    public void execute(final CommandInput commandInput, final InfoBank infoBank,
                        final ObjectMapper objectMapper, final ArrayNode output) {
        String email = commandInput.getEmail();
        for (User user : infoBank.getUsers()) {
            if (user.getEmail().equals(email)) {
                Transaction transaction = new NewAccTransaction(
                        commandInput.getTimestamp(),
                        "New account created");
                user.addTransaction(transaction);
                String currency = commandInput.getCurrency();
                String type = commandInput.getAccountType();
                String iban = Utils.generateIBAN();
                switch (type) {
                    case "classic":
                        String plan = null;
                        if (user.getAccounts() != null && !user.getAccounts().isEmpty()) {
                            plan = user.getAccounts().get(0).getPlan();
                        } else {
                            if (user.getOccupation() != null) {
                                if (user.getOccupation().equals("student")) {
                                    plan = "student";
                                } else {
                                    plan = "standard";
                                }
                            }
                        }
                        Account classic = new ClassicAccount(iban, 0.0, currency, type, plan);
                        user.addClassic((ClassicAccount) classic);
                        user.addAccounts(classic);
                        infoBank.addAccount(classic);
                        classic.addTransaction(transaction);
                        break;
                    case "savings":
                        String planSavings = null;
                        if (user.getAccounts() != null && !user.getAccounts().isEmpty()) {
                            planSavings = user.getAccounts().get(0).getPlan();
                        } else {
                            if (user.getOccupation() != null) {
                                if (user.getOccupation().equals("student")) {
                                    planSavings = "student";
                                } else {
                                    planSavings = "standard";
                                }
                            }
                        }
                        Account savings = new SavingsAccount(iban, 0.0, currency, type,
                                commandInput.getInterestRate(), planSavings);
                        user.addSavings((SavingsAccount) savings);
                        user.addAccounts(savings);
                        infoBank.addAccount(savings);
                        savings.addTransaction(transaction);
                        break;
                    case "business":
                        String planBusiness = null;
                        if (user.getAccounts() != null && !user.getAccounts().isEmpty()) {
                            if (user.getAccounts().get(0).getType().equals("business")
                                    && user.getEmail().equals(((BusinessAccount) user.getAccounts().
                                    get(0)).getOwner().getEmail())) {
                                planBusiness = user.getAccounts().get(0).getPlan();
                            } else if (!user.getAccounts().get(0).getType().equals("business")) {
                                planBusiness = user.getAccounts().get(0).getPlan();
                            } else {
                                if (user.getOccupation() != null) {
                                    if (user.getOccupation().equals("student")) {
                                        planBusiness = "student";
                                    } else {
                                        planBusiness = "standard";
                                    }
                                }
                            }
                        } else {
                            if (user.getOccupation() != null) {
                                if (user.getOccupation().equals("student")) {
                                    planBusiness = "student";
                                } else {
                                    planBusiness = "standard";
                                }
                            }
                        }
                        Account business = new BusinessAccount(iban, 0.0,
                                currency, type, planBusiness);
                        user.addBusiness((BusinessAccount) business);
                        user.addAccounts(business);
                        infoBank.addAccount(business);
                        business.addTransaction(transaction);
                        Owner owner = new Owner(user);
                        ((BusinessAccount) business).setOwner(owner);
                        double exchangedSpendingLimit = infoBank.exchange("RON",
                                commandInput.getCurrency(),
                                Constants.SPENDINGLIMIT.getValue());
                        double exchangedDepositLimit = infoBank.exchange("RON",
                                commandInput.getCurrency(),
                                Constants.DEPOSITLIMIT.getValue());
                        ((BusinessAccount) business).setDepositLimit(exchangedDepositLimit);
                        ((BusinessAccount) business).setSpendingLimit(exchangedSpendingLimit);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
