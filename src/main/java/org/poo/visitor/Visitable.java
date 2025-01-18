package org.poo.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.bank.InfoBank;
import org.poo.fileio.CommandInput;

public interface Visitable {
    /**
     * Functia generica de acceptarea vizitatorului.
     *
     */
    void accept(Visitor v, CommandInput commandInput, InfoBank infoBank,
                Account account, ObjectMapper objectMapper, ArrayNode output);
}
