package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientSelectSquare;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CommandMove extends Command {

    public CommandMove() {
        super("move",
                "move the worker in the selected square",
                " <x> <y> / <n-move>",
                "",
                Arrays.asList("m", "mv"));
    }

    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        //TODO: ricevere la lista per controllare lo square selezionato
        if (arguments.length == 1) {
            List<Point> validMoves = serverHandler.getValidMoves();
            Map<Power, List<Point>> blockedMoves = serverHandler.getBlockedMoves();
            int positionList = Integer.parseInt(arguments[0]) - 1;
            if (positionList < 0 || positionList >= validMoves.size()) {
                return "Invalid argument";
            }
            Point move = validMoves.get(positionList);
            if (!validMoves.contains(move)) {
                return "Invalid argument";
            }
            if (blockedMoves.values().stream().flatMap(Collection::stream).anyMatch(p -> p.equals(move))) {
                return "Blocked move";
            }
            client.sendPacket(new ClientSelectSquare(move));
            return String.format("Built at %d, %d", move.x, move.y);
        } else if (arguments.length == 2) {
            int x = Integer.parseInt(arguments[0]);
            int y = Integer.parseInt(arguments[1]);
            Point move = new Point(x, y);
            List<Point> validMoves = serverHandler.getValidMoves();
            Map<Power, List<Point>> blockedMoves = serverHandler.getBlockedMoves();
            if (!validMoves.contains(move)) {
                return "Invalid argument";
            }
            if (blockedMoves.values().stream().flatMap(Collection::stream).anyMatch(p -> p.equals(move))) {
                return "Blocked move";
            }
            client.sendPacket(new ClientSelectSquare(move));
            return String.format("Built at %d, %d", move.x, move.y);
        }
        return null;
    }
}
