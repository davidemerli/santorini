package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientRequestGameData;

import java.util.List;

/**
 * Used to reload the game map and information about players
 */
public class CommandReload extends Command {

    /**
     * Generic constructor
     * Defines the command name, the description, the types of argument and all alias
     */
    public CommandReload() {
        super("reload",
                "Sends the current map and all available info to the player",
                "",
                "^$",
                List.of("r", "clear"));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Sends a game data request
     */
    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        if (!client.isConnected()) {
            return "You are not connected.";
        }

        client.sendPacket(new ClientRequestGameData());
        return "";
    }
}
