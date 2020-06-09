package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

public class ServerInvalidPacket implements Packet<ServerHandler> {

    private final String error;

    public ServerInvalidPacket(String error) {
        this.error = error;
    }

    @Override
    public void processPacket(ServerHandler netHandler) {
        netHandler.handleError(this);
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return toString(error);
    }
}
