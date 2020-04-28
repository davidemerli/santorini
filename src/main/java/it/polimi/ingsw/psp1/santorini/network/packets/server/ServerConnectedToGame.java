package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

public class ServerConnectedToGame implements Packet<ServerHandler> {

    private final String name;

    public ServerConnectedToGame(String name) {
        this.name = name;
    }

    @Override
    public void processPacket(ServerHandler netHandler) {
        netHandler.handlePlayerConnected(this);
    }

    public String getName() {
        return name;
    }
}
