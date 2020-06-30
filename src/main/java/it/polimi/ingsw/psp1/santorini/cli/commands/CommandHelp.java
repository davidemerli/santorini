package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;

import java.util.List;

/**
 * Used to show the complete list of command
 */
public class CommandHelp extends Command {

    /**
     * Generic constructor
     * Defines the command name, the description, the types of argument and all alias
     */
    public CommandHelp() {
        super("help",
                "Shows the list of commands",
                "",
                "^$",
                List.of("h", "getcommands"));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Prints helps
     */
    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        return "";
    }
}
