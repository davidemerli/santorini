package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientSelectSquare;

import java.awt.*;
import java.util.Arrays;

public class CommandShowDescription extends Command {

    public CommandShowDescription() {
        super("showdescription",
                "show the selected God's description",
                "<God name>",
                "",
                Arrays.asList("sd", "desc"));
    }

    @Override
    public String onCommand(String input, String[] arguments) throws Exception {
        return null;
    }
}
