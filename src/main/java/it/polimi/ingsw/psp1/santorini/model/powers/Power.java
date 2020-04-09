package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.turn.TurnState;

import java.awt.*;
import java.util.List;

public interface Power {

    /**
     * Called on the beginning of own turn
     *
     * @param game current game
     */
    void onBeginTurn(Game game);

    /**
     * Called on the beginning of enemy turn
     *
     * @param game current game
     * @param player enemy player
     */
    void onEnemyBeginTurn(Game game, Player player);

    /**
     * Called when you want to set the final state
     *
     * @param game current game
     */
    void onEndTurn(Game game);

    /**
     * Called on the end of enemy turn
     *
     * @param game current game
     * @param player enemy player
     */
    void onEnemyEndTurn(Game game, Player player);

    /**
     * Called after input from the view that asks for a build on given position
     *
     * @param worker current selected worker
     * @param where point where you want to build
     * @param game   current game
     */
    void onYourBuild(Worker worker, Point where, Game game);

    /**
     * Called after input from the view that asks for a move on given position
     *
     * @param worker current selected worker
     * @param where  point where you want to move
     * @param game   current game
     */
    void onYourMove(Worker worker, Point where, Game game);

    /**
     * Called before the enemy build on the given position
     *
     * @param player enemy player
     * @param worker enemy worker
     * @param where next position
     * @param game current game
     */
    void onOpponentsBuild(Player player, Worker worker, Point where, Game game);

    /**
     * Called before the move build on the given position
     *
     * @param player enemy player
     * @param where next position
     * @param game current game
     */
    void onOpponentsMove(Player player, Point where, Game game);

    /**
     * Called when the player interacts with custom bottom on the UI
     *
     * @param game current game
     */
    void onToggleInteraction(Game game);

    /**
     * Called when the player decides to show the bottom for interaction or not
     *
     * @return true if the GUI needs to enable the bottom for interaction
     */
    boolean shouldShowInteraction();

    /**
     * Gets the list of unavailable moves for the enemy worker
     *
     * @param worker current selected worker
     * @param playerState current player status
     * @param game current game
     * @return list of blocked moves
     */
    List<Point> getBlockedMoves(Worker worker, TurnState playerState, Game game);

    /**
     * Called when you want to list all the valid moves that a worker can make
     *
     * @param game current game
     * @return list of valid moves
     */
    List<Point> getValidMoves(Game game);
}
