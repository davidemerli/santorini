package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientForfeit;

import java.util.Arrays;

public class CommandSurrender extends Command {

    public CommandSurrender() {
        super("surrender",
                "desc",
                "",
                "",
                Arrays.asList("ff", "forfeit"));
    }

    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        client.sendPacket(new ClientForfeit());
        return "You have surrendered";
    }
}
