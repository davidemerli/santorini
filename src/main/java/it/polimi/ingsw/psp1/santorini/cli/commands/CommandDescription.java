package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;

import java.util.Arrays;

public class CommandDescription extends Command {

    public CommandDescription() {
        super("description",
                "Shows the selected God's description",
                "<power-name>",
                "",
                Arrays.asList("d", "desc"));
    }

    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        return null;
    }
}
