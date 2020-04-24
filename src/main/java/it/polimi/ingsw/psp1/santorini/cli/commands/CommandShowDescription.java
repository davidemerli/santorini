package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;

import java.util.Arrays;

public class CommandShowDescription extends Command {

    public CommandShowDescription() {
        super("showdescription",
                "show the selected God's description",
                "<God name>",
                "",
                Arrays.asList("sd", "desc"));
    }

    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        return null;
    }
}
