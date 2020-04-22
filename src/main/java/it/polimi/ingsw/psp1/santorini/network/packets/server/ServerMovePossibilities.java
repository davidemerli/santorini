package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class ServerMovePossibilities implements Packet<ServerHandler> {

    private final List<Point> validMoves;
    private final Map<Power, List<Point>> blockedMoves;

    public ServerMovePossibilities(List<Point> validMoves, Map<Power, List<Point>> blockedMoves) {
        this.validMoves = validMoves;
        this.blockedMoves = blockedMoves;
    }

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
}
