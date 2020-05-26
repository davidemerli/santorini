package it.polimi.ingsw.psp1.santorini.observer;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;

import java.util.List;
import java.util.Map;

public interface ModelObserver {

    /**
     * Called when a player places a worker on the map
     *
     * @param player the player making the move
     * @param worker the worker that has been placed
     */
    void playerPlaceWorker(Player player, Worker worker);

    /**
     * Called when a player moves a worker in the game
     *
     * @param player the player making the move
     * @param worker the worker that actually does move
     * @param from   old position
     * @param where  new position
     */
    void playerMove(Player player, Worker worker, Point from, Point where);

    /**
     * Called when a player builds with a worker in the game
     *
     * @param player    the player making the build
     * @param worker    the worker that actually does build
     * @param where     the position where the block is positioned
     * @param forceDome if player has forced dome build
     */
    void playerBuild(Player player, Worker worker, Point where, boolean forceDome);

    /**
     * Called on a generic player update
     *
     * @param game   current game object
     * @param player that has his status updated
     */
    void playerUpdate(Game game, Player player);

    /**
     * Called on a generic game update
     *
     * @param game current game object
     */
    void gameUpdate(Game game);

    /**
     * Called when new moves are available to the current player in his turn
     *
     * @param player       current turn player
     * @param validMoves   list of the valid selectable squares
     * @param blockedMoves map with blocked squares associated with the power that is blocking them
     */
    void availableMovesUpdate(Player player, List<Point> validMoves, Map<Power, List<Point>> blockedMoves);

    /**
     * Called when the games requests input from the player
     *
     * @param player      that needs to be prompted a request
     * @param requestType type of request
     */
    void requestToPlayer(Player player, EnumRequestType requestType);

    /**
     * Called when a player needs to be given a list of god powers to choose from
     * (used both for selecting all powers for the game or own personal power)
     *
     * @param availablePowers list with the god powers to show to the player
     * @param toSelect how many powers to select
     */
    void sendPowerList(List<Power> availablePowers, int toSelect);
}