package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

/**
 * Server packet containing an error message
 */
public class ServerInvalidPacket implements Packet<ServerHandler> {

    private final String error;

    /**
     * Generic constructor
     *
     * @param error message
     */
    public ServerInvalidPacket(String error) {
        this.error = error;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processPacket(ServerHandler netHandler) {
        netHandler.handleError(this);
    }

    public String getError() {
        return error;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toString(error);
    }
}
