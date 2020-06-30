package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

/**
 * Client creates a game specifying the name of the players
 */
public class ClientCreateGame implements Packet<ClientHandler> {

    private final int playerNumber;

    /**
     * Generic constructor using the number of players in game
     *
     * @param playerNumber number of players
     */
    public ClientCreateGame(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processPacket(ClientHandler netHandler) {
        netHandler.handleCreateGame(this);
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    @Override
    public String toString() {
        return toString(playerNumber);
    }
}
