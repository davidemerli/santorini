package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.cli.Color;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientSetName;

import java.util.List;

/**
 * Used to set a player username
 */
public class CommandSetName extends Command {

    /**
     * Defines the command name, the description, the types of argument and all aliases
     */
    public CommandSetName() {
        super("setname",
                "Tries to set an username on the server",
                "<player-name>",
                ".+",
                List.of("sn", "name"));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Argument is the name of the player
     * Sets the name of the player
     */
    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        if (!client.isConnected()) {
            return "You are not connected.";
        }

        client.sendPacket(new ClientSetName(arguments[0]));
        serverHandler.setPlayerName(arguments[0]);

        return String.format("Set name to: '%s'", Color.GREEN + arguments[0] + Color.RESET);
    }
}
