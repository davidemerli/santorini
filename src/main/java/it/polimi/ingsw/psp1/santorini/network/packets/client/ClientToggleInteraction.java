package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

/**
 * Client packet containing the button interaction
 */
public class ClientToggleInteraction implements Packet<ClientHandler> {
    /**
     * {@inheritDoc}
     */
    @Override
    public void processPacket(ClientHandler netHandler) {
        netHandler.handleInteractionToggle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toString("");
    }
}
