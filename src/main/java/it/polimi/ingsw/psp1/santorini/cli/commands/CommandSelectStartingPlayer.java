package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientSelectStartingPlayer;
import it.polimi.ingsw.psp1.santorini.network.packets.server.PlayerData;

import java.util.List;
import java.util.Optional;

/**
 * Used to select the starting player
 */
public class CommandSelectStartingPlayer extends Command {

    /**
     * Generic constructor
     * Defines the command name, the description, the types of argument and all alias
     */
    public CommandSelectStartingPlayer() {
        super("startingplayer",
                "Selects the starting player",
                "<player-name>/<player-index>",
                "(\\w+)|(\\d+)",
                List.of("start", "begin"));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Argument is the name of the player who will start the game
     */
    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        if (isNumeric(arguments[0])) {
            int index = Integer.parseInt(arguments[0]) - 1;

            if (index < 0 || index >= serverHandler.getPlayerDataList().size()) {
                return "Invalid index";
            }

            PlayerData player = serverHandler.getPlayerDataList().get(index);
            client.sendPacket(new ClientSelectStartingPlayer(player.getName()));

            return String.format("Selected starting player: '%s'", player.getName());
        }

        Optional<PlayerData> player = serverHandler.getPlayerDataList().stream()
                .filter(p -> p.getName().equalsIgnoreCase(arguments[0]))
                .findFirst();

        if (player.isEmpty()) {
            return "Invalid name";
        }

        client.sendPacket(new ClientSelectStartingPlayer(player.get().getName()));

        return String.format("Selected starting player: '%s'", player.get().getName());
    }

    /**
     * Checks if the string is an integer value
     * @param string to check
     * @return true if the string is an integer value
     */
    private boolean isNumeric(String string) {
        return string.matches("\\d+");
    }
}
