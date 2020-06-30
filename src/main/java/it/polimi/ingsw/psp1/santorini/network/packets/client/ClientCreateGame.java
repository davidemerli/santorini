package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

/**
 * Client packet containing the number of player in order to create a game
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toString(playerNumber);
    }
}
