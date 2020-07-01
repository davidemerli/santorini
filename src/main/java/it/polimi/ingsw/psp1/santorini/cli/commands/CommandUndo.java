package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientRequestGameData;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientUndo;

import java.util.List;

/**
 * Used to go back to the beginning of the current turn
 */
public class CommandUndo extends Command {

    /**
     * Generic constructor
     * Defines the command name, the description, the types of argument and all alias
     */
    public CommandUndo() {
        super("undo",
                "Goes back to the beginning of the current turn",
                "",
                "^$",
                List.of("back"));
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

        client.sendPacket(new ClientUndo());
        return "Went back to the beginning of the turn";
    }
}
