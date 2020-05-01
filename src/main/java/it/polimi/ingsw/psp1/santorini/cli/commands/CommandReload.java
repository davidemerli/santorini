package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientRequestGameData;

import java.util.List;

public class CommandReload extends Command {

    public CommandReload() {
        super("reload",
                "Sends the current map and all available info to the player",
                "",
                "",
                List.of("r", "clear"));
    }

    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        client.sendPacket(new ClientRequestGameData());
        return "";
    }
}
