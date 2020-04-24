package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientSelectSquare;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class CommandBuild extends Command {

    public CommandBuild() {
        super("build",
                "place a block in the selected square",
                " <x> <y> / <n-move>",
                "",
                Arrays.asList("b", "place"));
    }

    @Override
    public String onCommand(String input, String[] arguments) throws Exception {
        if (arguments.length == 1) {
        //TODO: ricevere la lista per controllare lo square selezionato
        } else if (arguments.length == 2) {
            int x = Integer.parseInt(arguments[0]);
            int y = Integer.parseInt(arguments[1]);
            ClientSelectSquare packet = new ClientSelectSquare(new Point(x, y));
            return "";
        }
        return null;
    }
}
