package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

public class ClientCreateGame implements Packet<ClientHandler> {

    private final String gameRoomName;

    public ClientCreateGame(String name) {
        this.gameRoomName = name;
    }

    @Override
    public void processPacket(ClientHandler netHandler) {
        netHandler.handleCreateGame(this);
    }

    public String getGameRoomName() {
        return this.gameRoomName;
    }
}
