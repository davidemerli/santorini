package it.polimi.ingsw.psp1.santorini.network;

import it.polimi.ingsw.psp1.santorini.network.packets.client.*;

/**
 * Manages client actions
 */
public interface ClientHandler extends NetHandler {

    /**
     * Sets player's name
     *
     * @param packet to send
     */
    void handlePlayerSetName(ClientSetName packet);

    /**
     * Creates a new game
     *
     * @param packet to send
     */
    void handleCreateGame(ClientCreateGame packet);

    /**
     * Joins a new game
     *
     * @param packet to send
     */
    void handleJoinGame(ClientJoinGame packet);

    /**
     * Selects available gods
     *
     * @param packet to send
     */
    void handlePowerChoosing(ClientChoosePower packet);

    /**
     * Selects square of the map
     *
     * @param packet to send
     */
    void handleSquareSelect(ClientSelectSquare packet);

    /**
     * Selects worker on the map
     *
     * @param packet to send
     */
    void handleWorkerSelection(ClientSelectWorker packet);

    /**
     * Selects starting player
     *
     * @param packet to send
     */
    void handleSelectStartingPlayer(ClientSelectStartingPlayer packet);

    /**
     * Activates god's power
     */
    void handleInteractionToggle();

    /**
     * Requests updated game information
     */
    void handleRequestGameData();

    /**
     * Decides to forfeit and leave the game
     */
    void handlePlayerForfeit();

    /**
     * Keep alive packet
     */
    void handleKeepAlive();

    /**
     * Backs at the beginning of the turn
     */
    void handleUndo();
}
