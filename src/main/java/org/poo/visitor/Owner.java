package org.poo.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.bank.InfoBank;
import org.poo.fileio.CommandInput;

public final class Owner extends User {
    public Owner(final User user) {
        super(user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getBirthDate(), user.getOccupation());
    }

    @Override
    public void accept(final Visitor v,
                       final CommandInput commandInput,
                       final InfoBank infoBank,
                       final Account account,
                       final ObjectMapper objectMapper,
                       final ArrayNode output) {
        v.visitOwner(this, commandInput, infoBank, account, objectMapper, output);
    }
}
