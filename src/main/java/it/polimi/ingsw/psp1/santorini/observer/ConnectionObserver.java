package it.polimi.ingsw.psp1.santorini.observer;

import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;

import java.util.List;

public interface ConnectionObserver {

    /**
     * Called when a packet with a powerList is received
     *
     * @param powerList list with god powers
     */
    void processPowerList(List<Power> powerList);

    /**
     * Called when a packet a selected square is received
     *
     * @param square selected square
     */
    void processSquareSelection(Point square);

    /**
     * Called when a packet that requests an interaction is received
     */
    void processToggleInteraction();

    /**
     * Called when packet with a player request of surrender is received
     */
    void handlePlayerForfeit();

    /**
     * Called when a packet requests the server to send game data again
     */
    void processRequestGameData();

    /**
     * Called when a packet with a worker selection is received
     *
     * @param workerPosition position where the worker selected is supposed to be at
     */
    void processWorkerSelection(Point workerPosition);

    /**
     * Called when a packet with a starting player name is received
     *
     * @param name of the player that will start the game
     */
    void processStartingPlayerSelection(String name);

    /**
     * Called when a client tries to close the connection, or called forcefully if
     * the connections times out
     */
    void handleCloseConnection();
}