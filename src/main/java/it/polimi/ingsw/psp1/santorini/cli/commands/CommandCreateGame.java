package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientCreateGame;

import java.util.List;

public class CommandCreateGame extends Command {

    public CommandCreateGame() {
        super("creategame",
                "Creates a new game",
                "<num-players (2 or 3)>",
                "[2-3]",
                List.of("cg", "newgame"));
    }

    /**
     * {@inheritDoc}
     * <p>
     * The argument is the number of player of the game that will be created
     */
    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        int number = Integer.parseInt(arguments[0]);

        client.sendPacket(new ClientCreateGame(number));

        return String.format("Created new game for '%d' players", number);
    }
}
