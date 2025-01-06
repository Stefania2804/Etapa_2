package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;

import java.util.HashMap;

public final class Invoker {
    private HashMap<String, Command> commandMap;

    public Invoker() {
        commandMap = new HashMap<>();
        commandMap.put("printUsers", new PrintUsersCommand());
        commandMap.put("addAccount", new AddAccountCommand());
        commandMap.put("addFunds", new AddFundsCommand());
        commandMap.put("createCard", new CreateCardCommand());
        commandMap.put("createOneTimeCard", new CreateOneTimeCardCommand());
        commandMap.put("deleteAccount", new DeleteAccountCommand());
        commandMap.put("deleteCard", new DeleteCardCommand());
        commandMap.put("setMinBalance", new SetMinBalanceCommand());
        commandMap.put("checkCardStatus", new CheckCardStatusCommand());
        commandMap.put("payOnline", new PayOnlineCommand());
        commandMap.put("sendMoney", new SendMoneyCommand());
        commandMap.put("setAlias", new SetAliasCommand());
        commandMap.put("printTransactions", new PrintTransactionsCommand());
        commandMap.put("splitPayment", new SplitPaymentCommand());
        commandMap.put("addInterest", new AddInterestCommand());
        commandMap.put("changeInterestRate", new ChangeInterestRateCommand());
        commandMap.put("report", new ReportCommand());
        commandMap.put("spendingsReport", new SpendingsReportCommand());
        commandMap.put("withdrawSavings", new WithDrawSavingsCommand());
        commandMap.put("upgradePlan", new UpgradePlanCommand());
        commandMap.put("cashWithdrawal", new CashWithdrawalCommand());
        commandMap.put("acceptSplitPayment", new AcceptSplitPaymentCommand());
        commandMap.put("addNewBusinessAssociate", new AddNewBusinessAssociate());
        commandMap.put("businessReport", new BusinessReportCommand());
        commandMap.put("changeSpendingLimit", new ChangeSpendingLimit());
    }
    /**
     * functie pentru executarea comenzilor
     */
    public void executeCommand(final CommandInput commandInput, final InfoBank infoBank,
                               final ObjectMapper objectMapper, final ArrayNode output) {
        Command command = commandMap.get(commandInput.getCommand());
        if (command != null) {
            command.execute(commandInput, infoBank, objectMapper, output);
        } else {
            return;
        }
    }
}
