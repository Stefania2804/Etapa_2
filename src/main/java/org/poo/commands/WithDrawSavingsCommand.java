package org.poo.commands;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.bank.InfoBank;
import org.poo.visitor.User;
import org.poo.errortransactions.WithDrawSavingsErrorTransaction;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.transactions.WithDrawSavingsTransaction;


public class WithDrawSavingsCommand implements Command {
    /**
     * functie pentru retragerea economiilor.
     */
    public void execute(final CommandInput commandInput, final InfoBank infoBank,
                        final ObjectMapper objectMapper, final ArrayNode output) {
        for (User user : infoBank.getUsers()) {
            for (Account account : user.getSavings()) {
                if (account.getIban().equals(commandInput.getAccount())) {
                    boolean classicFound = false;
                    for (Account acc : user.getClassics()) {
                        if (acc.getCurrency().equals(commandInput.getCurrency())) {
                            classicFound = true;
                            if (calculateAge(user)!= null && calculateAge(user).getYears() >= 21) {
                                double exchangedAmount = infoBank.exchange(commandInput.getCurrency(),
                                        account.getCurrency(), commandInput.getAmount());
                                if (exchangedAmount <= account.getBalance()) {
                                    account.setBalance(account.getBalance() - exchangedAmount);
                                    acc.setBalance(acc.getBalance() + commandInput.getAmount());
                                    Transaction transaction = new WithDrawSavingsTransaction(commandInput.getTimestamp(), "Savings withdrawal",
                                            account.getIban(), acc.getIban(), commandInput.getAmount());
                                    user.addTransaction(transaction);
                                    user.addTransaction((transaction));
                                } else {
                                    Transaction transaction = new WithDrawSavingsErrorTransaction(commandInput.getTimestamp(),
                                            "Insufficient funds");
                                    user.addTransaction(transaction);
                                }
                            } else {
                                Transaction transaction = new WithDrawSavingsErrorTransaction(commandInput.getTimestamp(),
                                        "You don't have the minimum age required.");
                                user.addTransaction(transaction);
                            }
                        }
                    }
                    if (classicFound == false) {
                        Transaction transaction = new WithDrawSavingsErrorTransaction(commandInput.getTimestamp(), "You do not have a classic account.");
                        user.addTransaction(transaction);
                        account.addTransaction(transaction);
                    }
                }
            }
        }
    }

    public Period  calculateAge(final User user) {
        Period age = null;
        String dataString = user.getBirthDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            if (dataString != null) {
                LocalDate birthDate = LocalDate.parse(dataString, formatter);
                LocalDate currentDate = LocalDate.now();
                age = Period.between(birthDate, currentDate);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Formatul datei este invalid!");
        }
        return age;
    }
}
