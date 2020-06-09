package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

public class ClientJoinGame implements Packet<ClientHandler> {

    private final String gameRoom;
    private final int playerNumber;

    public ClientJoinGame(int playerNumber, String gameRoom) {
        this.playerNumber = playerNumber;
        this.gameRoom = gameRoom;
    }

    @Override
    public void processPacket(ClientHandler netHandler) {
        netHandler.handleJoinGame(this);
    }

    public int getPlayerNumber() {
        return this.playerNumber;
    }

    public String getGameRoom() {
        return this.gameRoom;
    }

    @Override
    public String toString() {
        return toString(gameRoom, playerNumber);
    }
}
