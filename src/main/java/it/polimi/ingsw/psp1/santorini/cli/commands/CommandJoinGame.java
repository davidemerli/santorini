package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientCreateGame;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientJoinGame;

import java.util.List;

public class CommandJoinGame extends Command {

    public CommandJoinGame() {
        super("join",
                "Joins a game",
                "<num-players (2 or 3)>/<game-id>",
                "([2-3])|(\\d+)",
                List.of("j", "joingame"));
    }

    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        int number = Integer.parseInt(arguments[0]);

        boolean isID = number != 2 && number != 3;

        client.sendPacket(new ClientJoinGame(isID ? -1 : number, isID ? number : -1));

        if(isID) {
            return String.format("Trying to join game with ID: %s", number);
        } else {
            return String.format("Trying to join game with player number: %s", number);
        }
    }
}
