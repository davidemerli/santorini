package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;

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
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        return "";
    }
}
