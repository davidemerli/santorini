package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

/**
 * Client packet containing
 * Client decided to retire from the game
 */
public class ClientForfeit implements Packet<ClientHandler> {
    /**
     * {@inheritDoc}
     */
    @Override
    public void processPacket(ClientHandler netHandler) {
        netHandler.handlePlayerForfeit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toString("");
    }
}
