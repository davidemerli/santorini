package it.polimi.ingsw.psp1.santorini.observer;

import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.view.RemoteView;
import it.polimi.ingsw.psp1.santorini.view.View;

import java.util.List;

public interface ViewObserver {

    /**
     * Called when a view selects a square on the map
     *
     * @param view     the view where the even is coming from
     * @param player   the player associated with the view
     * @param location of the square
     */
    void selectSquare(View view, Player player, Point location);

    /**
     * Called when a view selects a Worker on the map (its position is given)
     *
     * @param view           the view where the even is coming from
     * @param player         the player associated with the view
     * @param workerPosition on the map
     */
    void selectWorker(View view, Player player, Point workerPosition);

    /**
     * Called when a view toggles an interaction with the player's god power
     *
     * @param view   the view where the even is coming from
     * @param player the player associated with the view
     */
    void toggleInteraction(View view, Player player);

    /**
     * Called when a view selects a god power or a list of powers
     *
     * @param view      the view where the even is coming from
     * @param player    the player associated with the view
     * @param powerList with the select powers (singletonList or 2 to 3 power list)
     */
    void selectPowers(View view, Player player, List<Power> powerList);

    /**
     * Called when a view tries to select a starting
     *
     * @param view             the view where the even is coming from
     * @param player           the player associated with the view
     * @param chosenPlayerName starting player name
     */
    void selectStartingPlayer(View view, Player player, String chosenPlayerName);

    /**
     * Called when a view tries to call an undo operation on the current game turn
     *
     * @param view   the view where the even is coming from
     * @param player the player associated with the view
     */
    void undo(View view, Player player);

    /**
     * Called when a view tries to close the connection / leave the game
     *
     * @param view the view where the even is coming from
     */
    void leaveGame(View view);

    /**
     * Called when a player surrenders
     *
     * @param view the view where the even is coming from
     * @param player the player associated with the view
     */
    void playerSurrender(View view, Player player);
}
