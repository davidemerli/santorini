package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

public class ServerConnectedToGame implements Packet<ServerHandler> {

    private final String name;
    private final String gameID;

    public ServerConnectedToGame(String name, String gameID) {
        this.name = name;
        this.gameID = gameID;
    }

    @Override
    public void processPacket(ServerHandler netHandler) {
        netHandler.handlePlayerConnected(this);
    }

    public String getName() {
        return name;
    }

    public String getGameID() {
        return gameID;
    }

    @Override
    public String toString() {
        return toString(name, gameID);
    }
}
