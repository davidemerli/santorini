package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientSelectSquare;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CommandBuild extends Command {

    public CommandBuild() {
        super("build",
                "place a block in the selected square",
                " <x> <y> / <n-move>",
                "",
                Arrays.asList("b", "place"));
    }

    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        if (arguments.length == 1) {
            List<Point> validMoves = serverHandler.getValidMoves();
            Map<Power, List<Point>> blockedMoves = serverHandler.getBlockedMoves();
            int positionList = Integer.parseInt(arguments[0]) - 1;
            if (positionList < 0 || positionList >= validMoves.size()) {
                return "Invalid argument";
            }
            Point build = validMoves.get(positionList);
            if (!validMoves.contains(build)) {
                return "Invalid argument";
            }
            if (blockedMoves.values().stream().flatMap(Collection::stream).anyMatch(p -> p.equals(build))) {
                return "Blocked build";
            }
            client.sendPacket(new ClientSelectSquare(build));
            return String.format("Built at %d, %d", build.x, build.y);
        } else if (arguments.length == 2) {
            int x = Integer.parseInt(arguments[0]);
            int y = Integer.parseInt(arguments[1]);
            Point build = new Point(x, y);
            List<Point> validMoves = serverHandler.getValidMoves();
            Map<Power, List<Point>> blockedMoves = serverHandler.getBlockedMoves();
            if (!validMoves.contains(build)) {
                return "Invalid argument";
            }
            if (blockedMoves.values().stream().flatMap(Collection::stream).anyMatch(p -> p.equals(build))) {
                return "Blocked build";
            }
            client.sendPacket(new ClientSelectSquare(build));
            return String.format("Built at %d, %d", build.x, build.y);
        }
        return "Invalid argument";
    }
}
