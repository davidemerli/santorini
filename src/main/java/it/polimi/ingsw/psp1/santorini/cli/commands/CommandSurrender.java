package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientForfeit;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientSelectSquare;

import java.awt.*;
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
    public String onCommand(String input, String[] arguments) throws Exception {
        ClientForfeit packet = new ClientForfeit();
        return "You have surrendered";
    }
}
