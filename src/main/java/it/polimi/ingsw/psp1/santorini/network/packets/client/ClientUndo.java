package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

/**
 * Client decides to restart his turn
 */
public class ClientUndo implements Packet<ClientHandler> {
    /**
     * {@inheritDoc}
     */
    @Override
    public void processPacket(ClientHandler netHandler) {
        netHandler.handleUndo();
    }

    @Override
    public String toString() {
        return toString("");
    }
}
