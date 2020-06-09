package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

public class ServerKeepAlive implements Packet<ServerHandler> {

    @Override
    public void processPacket(ServerHandler netHandler) {
        netHandler.handleKeepAlive();
    }

}
