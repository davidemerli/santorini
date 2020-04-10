package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.turn.TurnState;

import java.awt.*;
import java.util.List;

public interface Power {

    /**
     * Called on the beginning of a player turn (both own and enemy)
     *
     * @param player current player
     * @param game   current game
     */
    void onBeginTurn(Player player, Game game);

    /**
     * Called when you want to set the final state
     *
     * @param game current game
     */
    void onEndTurn(Player player, Game game);

    /**
     * Called after input from the view that asks for a build on given position
     *
     * @param player current player
     * @param worker current selected worker
     * @param where  point where you want to build
     * @param game   current game
     */
    void onBuild(Player player, Worker worker, Point where, Game game);

    /**
     * Called after input from the view that asks for a move on given position
     *
     * @param player current player
     * @param worker current selected worker
     * @param where  point where you want to move
     * @param game   current game
     */
    void onMove(Player player, Worker worker, Point where, Game game);

    /**
     * Called when the player interacts with custom bottom on the UI
     *
     * @param game current game
     */
    void onToggleInteraction(Game game);

    /**
     * Called when the player decides to show the bottom for interaction or not
     *
     * @param game current game
     * @return true if the GUI needs to enable the bottom for interaction
     */
    boolean shouldShowInteraction(Game game);

    /**
     * Gets the list of unavailable moves for the enemy worker
     *
     * @param player current player playing
     * @param worker worker to get blocked moves of
     * @param game   current game
     * @return list of blocked moves
     */
    List<Point> getBlockedMoves(Player player, Worker worker, Game game);

    /**
     * Called when you want to list all the valid moves that a worker can make
     *
     * @param game   current game
     * @param worker to get valid moves of
     * @return list of valid moves
     */
    List<Point> getValidMoves(Worker worker, Game game);

    /**
     * Returns to the previous turn state, should be customized for powers that can possibly do lots of moves
     */
    void undo();
}
