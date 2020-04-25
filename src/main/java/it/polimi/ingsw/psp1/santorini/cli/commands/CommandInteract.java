package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientToggleInteraction;

import java.util.Arrays;

public class CommandInteract extends Command {

    public CommandInteract() {
        super("interact",
                "Activates your God's power, if possible",
                "",
                "",
                Arrays.asList("usepower", "power"));
    }

    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        ClientToggleInteraction packet = new ClientToggleInteraction();
        return "";
    }
}
