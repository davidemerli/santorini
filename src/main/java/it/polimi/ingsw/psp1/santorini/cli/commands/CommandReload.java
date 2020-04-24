package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientForfeit;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientRequestGameData;

import java.util.Arrays;

public class CommandReload extends Command {

    public CommandReload() {
        super("reload",
                "send the current map and all available info to the player",
                "",
                "",
                Arrays.asList("r", "loadagain"));
    }

    @Override
    public String onCommand(String input, String[] arguments) throws Exception {
        ClientRequestGameData packet = new ClientRequestGameData();
        return "";
    }
}
