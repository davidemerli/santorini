package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;

import java.util.Arrays;
import java.util.List;

public class CommandHelp extends Command {

    public CommandHelp() {
        super("help",
                "Shows the list of commands",
                "",
                "",
                List.of("h", "getcommands"));
    }

    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        return "";
    }
}
