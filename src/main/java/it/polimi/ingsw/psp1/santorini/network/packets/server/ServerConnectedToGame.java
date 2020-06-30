package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

/**
 * Server packet used to connect with the game
 */
public class ServerConnectedToGame implements Packet<ServerHandler> {

    private final String username;
    private final String gameID;
    private final int playerNumber;

    /**
     * Generic constructor
     *
     * @param username     player username
     * @param gameID       game ID
     * @param playerNumber number of player in the game
     */
    public ServerConnectedToGame(String username, String gameID, int playerNumber) {
        this.username = username;
        this.gameID = gameID;
        this.playerNumber = playerNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processPacket(ServerHandler netHandler) {
        netHandler.handlePlayerConnected(this);
    }

    public String getUsername() {
        return username;
    }

    public String getGameID() {
        return gameID;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toString(username, gameID, playerNumber);
    }
}
