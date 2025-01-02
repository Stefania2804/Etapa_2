package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;
import org.poo.main.JsonOutput;

public final class PrintUsersCommand implements Command {
    /**
     * functia de executare pentru afisarea utilizatorilor.
     */
    public void execute(final CommandInput commandInput, final InfoBank infoBank,
                        final ObjectMapper objectMapper, final ArrayNode output) {
        JsonOutput.printUsers(infoBank, commandInput, objectMapper, output);
    }
}
