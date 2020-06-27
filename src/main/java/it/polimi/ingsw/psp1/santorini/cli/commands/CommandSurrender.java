package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientForfeit;

import java.util.List;

public class CommandSurrender extends Command {

    public CommandSurrender() {
        super("surrender",
                "Forfeits the current game",
                "",
                "^$",
                List.of("ff", "forfeit"));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Sends a surrender packet to server and notifies the player with a message
     */
    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        client.sendPacket(new ClientForfeit());
        return "You have surrendered";
    }
}
