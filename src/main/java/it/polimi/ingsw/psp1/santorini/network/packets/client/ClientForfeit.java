package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

public class ClientForfeit implements Packet<ClientHandler> {
    @Override
    public void processPacket(ClientHandler netHandler) {
        netHandler.handlePlayerForfeit();
    }

    @Override
    public String toString() {
        return toString("");
    }
}
