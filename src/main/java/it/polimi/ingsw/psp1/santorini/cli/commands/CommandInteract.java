package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientSelectSquare;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientToggleInteraction;

import java.awt.*;
import java.util.Arrays;

public class CommandInteract extends Command {

    public CommandInteract() {
        super("interact",
                "activate your God's power, if possible",
                "",
                "",
                Arrays.asList("usepower", "power"));
    }

    @Override
    public String onCommand(String input, String[] arguments) throws Exception {
        ClientToggleInteraction packet = new ClientToggleInteraction();
        return "";
    }
}
