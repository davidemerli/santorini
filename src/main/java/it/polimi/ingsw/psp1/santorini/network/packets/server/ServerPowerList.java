package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

import java.util.List;

public class ServerPowerList implements Packet<ServerHandler> {

    private final List<Power> validMoves;

    public ServerPowerList(List<Power> validMoves) {
        this.validMoves = validMoves;
    }

    @Override
    public void processPacket(ServerHandler netHandler) {
        netHandler.handlePowerList(this);
    }

    public List<Power> getValidMoves() {
        return validMoves;
    }
}
