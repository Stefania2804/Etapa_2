package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.CommandInput;
import org.poo.bank.InfoBank;

public interface Command {
    /**
     * functia generica, implementata de fiecare comanda
     */
    void execute(CommandInput commandInput, InfoBank infoBank,
                 ObjectMapper objectMapper, ArrayNode output);
}
