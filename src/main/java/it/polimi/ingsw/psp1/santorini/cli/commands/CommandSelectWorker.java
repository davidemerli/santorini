package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientSelectSquare;

import java.awt.*;
import java.util.Arrays;

public class CommandSelectWorker extends Command {

    public CommandSelectWorker() {
        super("selectworker",
                "choose the worker you want to use in this turn",
                " <x> <y> / <n-move>",
                "",
                Arrays.asList("sw", "sworker"));
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
