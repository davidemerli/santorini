package it.polimi.ingsw.psp1.santorini.model.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;

import java.util.List;

/**
 * Defines the state where a player must choose the initial placing of his workers
 */
public class WorkerPlacing extends TurnState {

    @Override
    public void init(Game game) {
        super.init(game);

        game.notifyObservers(o -> o.availableMovesUpdate(game.getCurrentPlayer(),
                getValidMoves(game, game.getCurrentPlayer(), null),
                getBlockedMoves(game, game.getCurrentPlayer(), null)));

        game.askRequest(game.getCurrentPlayer(), EnumRequestType.PLACE_WORKER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldShowInteraction(Game game, Player player) {
        return false;
    }

    /**
     * {@inheritDoc}
     * <p>
     *
     * @param game     current game
     * @param player   current player
     * @param position of the selected square
     * @throws IllegalArgumentException if the square is occupied
     */
    @Override
    public void selectSquare(Game game, Player player, Point position) {
        if (game.getWorkerOn(position).isPresent()) {
            throw new IllegalArgumentException("Occupied square");
        }

        game.addWorker(player, position);

        if (player.getWorkers().size() == 2) {
            game.endTurn();
            return;
        }

        init(game);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Point> getValidMoves(Game game, Player player, Worker worker) {
        return player.getPower().getValidMoves(worker, game);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undo(Game game, Player player) {
        game.restoreSavedState();
    }
}
