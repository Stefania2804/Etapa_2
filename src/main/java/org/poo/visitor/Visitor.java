package org.poo.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.bank.InfoBank;
import org.poo.fileio.CommandInput;

public interface Visitor {
    void visitEmployee(Employee employee, CommandInput commandInput, InfoBank infoBank, Account account, ObjectMapper objectMapper, ArrayNode output);
    void visitManager(Manager manager, CommandInput commandInput, InfoBank infoBank, Account account, ObjectMapper objectMapper, ArrayNode output);
    void visitOwner(Owner owner, CommandInput commandInput, InfoBank infoBank, Account account, ObjectMapper objectMapper, ArrayNode output);
    void visitUser(User user, CommandInput commandInput, InfoBank infoBank, Account account, ObjectMapper objectMapper, ArrayNode output);
}
