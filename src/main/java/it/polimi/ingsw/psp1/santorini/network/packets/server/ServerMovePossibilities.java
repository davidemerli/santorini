package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Server packet containing all possible movements
 */
public class ServerMovePossibilities implements Packet<ServerHandler> {

    private final List<Point> validMoves;
    private final Map<Power, List<Point>> blockedMoves;

    /**
     * Generic constructor
     *
     * @param validMoves   valid moves list
     * @param blockedMoves blocked moves list
     */
    public ServerMovePossibilities(List<Point> validMoves, Map<Power, List<Point>> blockedMoves) {
        this.validMoves = validMoves;
        this.blockedMoves = blockedMoves;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processPacket(ServerHandler netHandler) {
        netHandler.handleReceivedMoves(this);
    }

    public List<Point> getValidMoves() {
        return validMoves;
    }

    public Map<Power, List<Point>> getBlockedMoves() {
        return blockedMoves;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String invalid = blockedMoves.entrySet().stream()
                .map(entry -> entry.getKey().getName() + ": [" + pointsToString(entry.getValue()) + "]")
                .collect(Collectors.joining(", "));

        return toString(pointsToString(validMoves), invalid);
    }

    private String pointsToString(List<Point> points) {
        return points.stream()
                .map(p -> String.format("[%d, %d]", p.x, p.y))
                .collect(Collectors.joining(", "));
    }
}
