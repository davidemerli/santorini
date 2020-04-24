package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientSelectSquare;

import java.awt.*;
import java.util.Arrays;

public class CommandHelp extends Command {

    public CommandHelp() {
        super("help",
                "shows the list of commands",
                "",
                "",
                Arrays.asList("h", "getcommands"));
    }

    @Override
    public String onCommand(String input, String[] arguments) throws Exception {
        return null;
    }
}
