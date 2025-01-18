package org.poo.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.account.Pair;
import org.poo.bank.InfoBank;
import org.poo.commands.ExecuteOnlinePayment;
import org.poo.commands.ExecuteSendMoney;
import org.poo.fileio.CommandInput;


public class CommandVisitor implements Visitor {
    /**
     * Functia de vizitare pentru utilizator.
     *
     */
    public void visitUser(final User user, final CommandInput commandInput,
                          final InfoBank infoBank, final Account account,
                          final ObjectMapper objectMapper, final ArrayNode output) {
        return;
    }
    /**
     * Functia de vizitare pentru titular.
     *
     */
    public void visitOwner(final Owner owner, final CommandInput commandInput,
                           final InfoBank infoBank, final Account account,
                           final ObjectMapper objectMapper, final ArrayNode output) {
        return;
    }
    /**
     * Functia de vizitare pentru angajat.
     *
     */
    public void visitEmployee(final Employee employee,
                              final CommandInput commandInput,
                              final InfoBank infoBank,
                              final Account account,
                              final ObjectMapper objectMapper,
                              final ArrayNode output) {
        switch (commandInput.getCommand()) {
            case "payOnline":
                double exhangedToRon = infoBank.exchange(commandInput.getCurrency(),
                        "RON", commandInput.getAmount());
                double exchangedAmount = infoBank.exchange(commandInput.getCurrency(),
                        account.getCurrency(), commandInput.getAmount());
                if (exchangedAmount <= ((BusinessAccount) account).getSpendingLimit()
                        && ExecuteOnlinePayment.enoughFunds(exchangedAmount,
                        account, exhangedToRon)) {
                    Pair pair = new Pair(exchangedAmount, commandInput.getEmail(),
                            "payOnline");
                    ((BusinessAccount) account).addEmployeeAction(
                            pair, commandInput.getTimestamp());
                    ExecuteOnlinePayment.execute(commandInput, infoBank, objectMapper, output);
                }
                break;
            case "sendMoney":
                double exchangedToRon = infoBank.exchange(account.getCurrency(),
                        "RON", commandInput.getAmount());
                if (ExecuteSendMoney.enoughFunds(commandInput.getAmount(),
                        account, exchangedToRon)
                        && commandInput.getAmount() <= ((BusinessAccount) account).
                        getSpendingLimit()) {
                    Pair pair = new Pair(commandInput.getAmount(),
                            commandInput.getEmail(), "sendMoney");
                    ((BusinessAccount) account).addEmployeeAction(pair,
                            commandInput.getTimestamp());
                    ExecuteSendMoney.execute(commandInput, infoBank, objectMapper, output);
                }
                break;
            case "addFunds":
                if (commandInput.getAmount()
                        <= ((BusinessAccount) account).getDepositLimit()) {
                    Pair pair = new Pair(commandInput.getAmount(),
                            commandInput.getEmail(), "addFunds");
                    ((BusinessAccount) account).addEmployeeAction(
                            pair, commandInput.getTimestamp());
                    account.setBalance(account.getBalance() + commandInput.getAmount());
                }
                break;
            default:
                break;
        }
    }
    /**
     * Functia de vizitare pentru manager.
     *
     */
    public void visitManager(final Manager manager,
                             final CommandInput commandInput,
                             final InfoBank infoBank,
                             final Account account,
                             final ObjectMapper objectMapper,
                             final ArrayNode output) {
        switch (commandInput.getCommand()) {
            case "payOnline":
                double exhangedToRon = infoBank.exchange(commandInput.getCurrency(),
                        "RON", commandInput.getAmount());
                double exchangedAmount = infoBank.exchange(commandInput.getCurrency(),
                        account.getCurrency(), commandInput.getAmount());
                if (ExecuteOnlinePayment.enoughFunds(exchangedAmount,
                        account, exhangedToRon)) {
                    Pair pair = new Pair(exchangedAmount,
                            commandInput.getEmail(), "payOnline");
                    ((BusinessAccount) account).addManagerAction(pair,
                            commandInput.getTimestamp());
                    ExecuteOnlinePayment.execute(commandInput,
                            infoBank, objectMapper, output);
                }
                break;
            case "sendMoney":
                double exchangedToRon = infoBank.exchange(account.getCurrency(),
                        "RON", commandInput.getAmount());
                if (ExecuteSendMoney.enoughFunds(commandInput.getAmount(),
                        account, exchangedToRon)) {
                    Pair pair = new Pair(commandInput.getAmount(),
                            commandInput.getEmail(), "sendMoney");
                    ((BusinessAccount) account).addManagerAction(pair, commandInput.getTimestamp());
                    ExecuteSendMoney.execute(commandInput, infoBank, objectMapper, output);
                }
                break;
            case "addFunds":
                Pair pair = new Pair(commandInput.getAmount(),
                        commandInput.getEmail(), "addFunds");
                ((BusinessAccount) account).addManagerAction(pair, commandInput.getTimestamp());
                account.setBalance(account.getBalance() + commandInput.getAmount());
                break;
            default:
                break;
        }
    }
}
