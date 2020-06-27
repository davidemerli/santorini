package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientToggleInteraction;

import java.util.List;

public class CommandInteract extends Command {

    public CommandInteract() {
        super("interact",
                "Activates your God's power, if possible",
                "",
                "^$",
                List.of("usepower", "power"));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Command notifies server that the player wants to activates or deactivates his power
     * Prints successful message or an error
     */
    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        if (serverHandler.getShowInteraction()) {
            serverHandler.setShouldShowInteraction(false);
            client.sendPacket(new ClientToggleInteraction());
            return "Activated power";
        }

        return "Command not available in this state";
    }
}
