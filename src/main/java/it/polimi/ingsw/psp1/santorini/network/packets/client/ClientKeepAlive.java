package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

public class ClientKeepAlive implements Packet<ClientHandler> {
    @Override
    public void processPacket(ClientHandler netHandler) {
        netHandler.handleKeepAlive(this);
    }
}
