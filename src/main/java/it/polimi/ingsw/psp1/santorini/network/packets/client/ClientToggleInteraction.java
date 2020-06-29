package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

/**
 * Client toggles the interaction buttom
 */
public class ClientToggleInteraction implements Packet<ClientHandler> {
    @Override
    public void processPacket(ClientHandler netHandler) {
        netHandler.handleInteractionToggle();
    }

    @Override
    public String toString() {
        return toString("");
    }
}
