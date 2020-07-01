package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientSelectWorker;
import it.polimi.ingsw.psp1.santorini.network.packets.server.PlayerData;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Used to select worker
 */
public class CommandSelectWorker extends Command {

    /**
     * Generic constructor
     * Defines the command name, the description, the types of argument and all alias
     */
    public CommandSelectWorker() {
        super("selectworker",
                "Chooses the worker you want to use in this turn",
                "<x> <y>/<move-index>",
                "(\\d+ \\d+)|(\\d+)",
                List.of("sw", "sworker"));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Argument is the number of the worker or the coordinates of the worker
     * Checks the successful of the selection
     */
    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        if (!client.isConnected()) {
            return "You are not connected.";
        }

        Optional<PlayerData> optPlayer = serverHandler.getPlayerData();

        if(optPlayer.isEmpty()) {
            return "You are not part of any game";
        }

        List<Point> validMoves = optPlayer.get().getWorkers().stream()
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
