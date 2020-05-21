package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientSelectSquare;

import java.util.Collection;
import java.util.List;

public class CommandPlaceWorker extends Command {

    public CommandPlaceWorker() {
        super("placeworker",
                "Places your worker in the map",
                "<x> <y>/<move-index>",
                "(\\d+ \\d+)|(\\d+)",
                List.of("pw", "pworker"));
    }

    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        if (arguments.length == 1) {
            int i = Integer.parseInt(arguments[0]) - 1;

            if (i < 0 || i >= serverHandler.getValidMoves().size()) {
                return "Invalid move";
            }

            Point point = serverHandler.getValidMoves().get(i);

            if (serverHandler.getBlockedMoves().values().stream()
                    .flatMap(Collection::stream).anyMatch(p -> p.equals(point))) {
                return "Blocked move";
            }

            client.sendPacket(new ClientSelectSquare(point));

            return String.format("Placed Worker at position %d, %d", point.x, point.y);
        } else {
            int x = Integer.parseInt(arguments[0]);
            int y = Integer.parseInt(arguments[1]);

            Point point = new Point(x, y);

            if (!serverHandler.getValidMoves().contains(point)) {
                return "Invalid move";
            }

            if (serverHandler.getBlockedMoves().values().stream()
                    .flatMap(Collection::stream).anyMatch(p -> p.equals(point))) {
                return "Blocked move";
            }

            client.sendPacket(new ClientSelectSquare(point));
            return String.format("Placed Worker at position %d, %d", point.x, point.y);
        }
    }
}
