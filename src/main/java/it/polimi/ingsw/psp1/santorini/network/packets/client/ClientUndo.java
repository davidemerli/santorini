package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

public class ClientUndo implements Packet<ClientHandler> {

    @Override
    public void processPacket(ClientHandler netHandler) {
        netHandler.handleUndo();
    }

    @Override
    public String toString() {
        return toString("");
    }
}
