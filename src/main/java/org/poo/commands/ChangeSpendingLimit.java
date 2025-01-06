package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.Business;
import org.poo.bank.InfoBank;
import org.poo.fileio.CommandInput;
import org.poo.visitor.Owner;
import org.poo.visitor.User;

public class ChangeSpendingLimit implements Command {
    public void execute(final CommandInput commandInput,
                        final InfoBank infoBank,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        for (User user : infoBank.getUsers()) {
            if (user.getEmail().equals(commandInput.getEmail())) {
                for (Account account : user.getAccounts()) {
                    if (account.getType().equals("business") && account.getIban().equals(commandInput.getAccount())
                            && ((Business) account).getOwner().getEmail().equals(commandInput.getEmail())) {
                        double exchangedToRon = infoBank.exchange(account.getCurrency(), "RON", commandInput.getAmount());
                        ((Business) account).setSpendingLimit(exchangedToRon);
                    }
                }
            }
        }
    }
}
