package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

public class ClientCreateGame implements Packet<ClientHandler> {

    private final int playerNumber;

    public ClientCreateGame(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    @Override
    public void processPacket(ClientHandler netHandler) {
        netHandler.handleCreateGame(this);
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
