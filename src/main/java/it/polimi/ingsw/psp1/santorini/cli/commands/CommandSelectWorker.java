package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientSelectWorker;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CommandSelectWorker extends Command {

    public CommandSelectWorker() {
        super("selectworker",
                "Chooses the worker you want to use in this turn",
                " <x> <y> / <n-move>",
                "",
                List.of("sw", "sworker"));
    }

    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        List<Point> validMoves = serverHandler.getPlayerData().getWorkers().stream()
                .map(Worker::getPosition).collect(Collectors.toList());

        if (arguments.length == 1) {
            int i = Integer.parseInt(arguments[0]) - 1;

            if (i < 0 || i >= validMoves.size()) {
                return "Invalid move";
            }

            Point point = validMoves.get(i);

            if (serverHandler.getBlockedMoves().values().stream()
                    .flatMap(Collection::stream).anyMatch(p -> p.equals(point))) {
                return "Blocked move";
            }

            client.sendPacket(new ClientSelectWorker(point));

            return String.format("Selected Worker at position %d, %d", point.x, point.y);
        } else {
            int x = Integer.parseInt(arguments[0]);
            int y = Integer.parseInt(arguments[1]);

            Point point = new Point(x, y);

            if (!validMoves.contains(point)) {
                return "Invalid move";
            }

            if (serverHandler.getBlockedMoves().values().stream()
                    .flatMap(Collection::stream).anyMatch(p -> p.equals(point))) {
                return "Blocked move";
            }

            client.sendPacket(new ClientSelectWorker(point));

            return String.format("Selected Worker at position %d, %d", point.x, point.y);
        }
    }
}
