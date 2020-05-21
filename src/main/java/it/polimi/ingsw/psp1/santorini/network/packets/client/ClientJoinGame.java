package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

public class ClientJoinGame implements Packet<ClientHandler> {

    private final int gameRoom;
    private final int playerNumber;

    public ClientJoinGame(int playerNumber, int gameRoom) {
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

    public int getGameRoom() {
        return this.gameRoom;
    }
}
