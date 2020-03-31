package it.polimi.ingsw.psp1.santorini.powers;

import it.polimi.ingsw.psp1.santorini.Game;
import it.polimi.ingsw.psp1.santorini.map.Worker;
import it.polimi.ingsw.psp1.santorini.player.Player;
import it.polimi.ingsw.psp1.santorini.player.turn.TurnState;

import java.awt.*;
import java.util.List;

public interface Power {

    /**
     * Called when you want to set the initial state
     *
     * @param game current game
     */
    void onBeginTurn(Game game);

    /**
     * Called when you want to set the final state
     *
     * @param game current game
     */
    void onEndTurn(Game game);

    /**
     * Called when you want to build a block on the map
     *
     * @param worker current selected worker
     * @param where  point where you want to build
     * @param game   current game
     * @return next state
     */
    TurnState onYourBuild(Worker worker, Point where, Game game);

    /**
     * Called when enemy want to move a worker on the map
     *
     * @param worker current selected worker
     * @param where  point where you want to move
     * @param game   current game
     * @return next state
     */
    TurnState onYourMove(Worker worker, Point where, Game game);

    /**
     * Called when enemy want to build a block on the map
     *
     * @param player enemy player
     * @param worker enemy worker
     * @param where next position
     * @param game current game
     */
    void onOpponentsBuild(Player player, Worker worker, Point where, Game game);

    /**
     * @param player enemy player
     * @param worker enemy worker
     * @param oldPosition old position
     * @param where next position
     * @param game current game
     */
    void onOpponentsMove(Player player, Worker worker, Point oldPosition, Point where, Game game);

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
     * Called when you want to list map's blocks that a worker cannot use
     *
     * @param worker      current selected worker
     * @param validMoves  list of moves that a worker can make
     * @param playerState current player status
     * @param game        current game
     * @return list of blocked moves
     */
    public List<Point> getBlockedMoves(Worker worker, List<Point> validMoves, TurnState playerState, Game game);

    /**
     * Called when you want to list all the valid moves that a worker can make
     *
     * @param game current game
     * @return list of valid moves
     */
    List<Point> getValidMoves(Game game);
}
