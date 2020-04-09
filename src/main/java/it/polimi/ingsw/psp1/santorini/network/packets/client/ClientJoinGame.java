package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

public class ClientJoinGame implements Packet<ClientHandler> {

    private final String gameRoomName;

    public ClientJoinGame(String name) {
        this.gameRoomName = name;
    }

    @Override
    public void processPacket(ClientHandler netHandler) {
        netHandler.handleJoinGame(this);
    }

    public String getGameRoomName() {
        return this.gameRoomName;
    }
}
