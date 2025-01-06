package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.Business;
import org.poo.account.Classic;
import org.poo.account.Savings;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;
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
                        Account classic = new Classic(iban, 0.0, currency, type, plan);
                        user.addClassic((Classic) classic);
                        user.addAccounts(classic);
                        infoBank.addAccount(classic);
                        classic.addTransaction(transaction);
                        break;
                    case "savings":
                        String plan_savings = null;
                        if (user.getAccounts() != null && !user.getAccounts().isEmpty()) {
                            plan_savings = user.getAccounts().get(0).getPlan();
                        } else {
                            if (user.getOccupation() != null) {
                                if (user.getOccupation().equals("student")) {
                                    plan_savings = "student";
                                } else {
                                    plan_savings = "standard";
                                }
                            }
                        }
                        Account savings = new Savings(iban, 0.0, currency, type,
                                commandInput.getInterestRate(), plan_savings);
                        user.addSavings((Savings) savings);
                        user.addAccounts(savings);
                        infoBank.addAccount(savings);
                        savings.addTransaction(transaction);
                        break;
                    case "business":
                        String plan_business = null;
                        if (user.getAccounts() != null && !user.getAccounts().isEmpty()) {
                            if (user.getAccounts().get(0).getType().equals("business")
                                    && user.getEmail().equals(((Business)user.getAccounts().get(0)).getOwner())) {
                                plan_business = user.getAccounts().get(0).getPlan();
                            } else if (!user.getAccounts().get(0).getType().equals("business")){
                                plan_business = user.getAccounts().get(0).getPlan();
                            } else {
                                if (user.getOccupation() != null) {
                                    if (user.getOccupation().equals("student")) {
                                        plan_business = "student";
                                    } else {
                                        plan_business = "standard";
                                    }
                                }
                            }
                        } else {
                            if (user.getOccupation() != null) {
                                if (user.getOccupation().equals("student")) {
                                    plan_business = "student";
                                } else {
                                    plan_business = "standard";
                                }
                            }
                        }
                        Account business = new Business(iban, 0.0, currency, type, plan_business);
                        user.addBusiness((Business) business);
                        user.addAccounts(business);
                        infoBank.addAccount(business);
                        business.addTransaction(transaction);
                        Owner owner = new Owner(user);
                        ((Business) business).setOwner(owner);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
