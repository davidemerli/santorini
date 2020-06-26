package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientSelectSquare;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CommandSelect extends Command {

    public CommandSelect() {
        super("select",
                "Moves/Build in the selected square",
                "<x> <y>/<move-index>",
                "(\\d+ \\d+)|(\\d+)",
                List.of("sel", "sos"));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Arguments are the new coordinates of the worker
     * Checks if the move is valid
     */
    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        Point move;

        List<Point> validMoves = serverHandler.getValidMoves();
        Map<Power, List<Point>> blockedMoves = serverHandler.getBlockedMoves();

        if (arguments.length == 1) {
            int positionList = Integer.parseInt(arguments[0]) - 1;

            if (positionList < 0 || positionList >= validMoves.size()) {
                return "Invalid argument";
            }

            move = validMoves.get(positionList);
        } else {
            int x = Integer.parseInt(arguments[0]);
            int y = Integer.parseInt(arguments[1]);

            move = new Point(x, y);
        }

        if (!validMoves.contains(move)) {
            return "Invalid argument";
        }

        if (blockedMoves.values().stream().flatMap(Collection::stream).anyMatch(p -> p.equals(move))) {
            return "Blocked move";
        }

        client.sendPacket(new ClientSelectSquare(move));

        return String.format("%s at %d, %d", serverHandler.getLastRequest(), move.x, move.y);
    }
}
