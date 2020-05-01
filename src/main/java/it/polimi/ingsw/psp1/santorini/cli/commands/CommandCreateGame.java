package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientCreateGame;

import java.util.List;

public class CommandCreateGame extends Command {

    public CommandCreateGame() {
        super("creategame",
                "Creates a new game",
                "<game-name> <num-players>",
                "",
                List.of("cg", "newgame"));
    }

    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        String name = arguments[0];
        int number = Integer.parseInt(arguments[1]);

        client.sendPacket(new ClientCreateGame(name, number));

        return String.format("Created new game '%s' with #players: '%d'", name, number);
    }
}
