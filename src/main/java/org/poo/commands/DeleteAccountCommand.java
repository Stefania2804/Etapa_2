package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.errorTransactions.DeleteAccountErrorTransaction;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;
import org.poo.main.JsonOutput;
import org.poo.bank.User;
import org.poo.transactions.Transaction;

public final class DeleteAccountCommand implements Command {
    /**
     * functia de executare pentru stergerea unui cont.
     */
    public void execute(final CommandInput commandInput, final InfoBank infoBank,
                        final ObjectMapper objectMapper, final ArrayNode output) {
        String ibanAccDelete = commandInput.getAccount();
        String userEmailDelete = commandInput.getEmail();
        for (User user : infoBank.getUsers()) {
            if (user.getEmail().equals(userEmailDelete)) {
                for (Account accs : user.getAccounts()) {
                    if (accs.getIban().equals(ibanAccDelete) && accs.getBalance() == 0.0) {
                        user.deleteFromUser(accs);
                        infoBank.deleteFromBank(accs);
                        JsonOutput.deleteAccount(commandInput, objectMapper, output);
                        break;
                    } else {
                        Transaction transaction = new
                                DeleteAccountErrorTransaction(commandInput.getTimestamp(),
                                "Account couldn't be deleted - "
                                        + "there are funds remaining");
                        user.addTransaction(transaction);
                        accs.addTransaction(transaction);
                        JsonOutput.deleteAccountError(commandInput, objectMapper, output);
                        break;
                    }
                }
            }
        }
    }
}
