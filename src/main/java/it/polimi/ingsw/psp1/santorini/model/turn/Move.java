package it.polimi.ingsw.psp1.santorini.model.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;

import java.util.NoSuchElementException;
import java.util.Optional;

public class Move extends TurnState {

    @Override
    public void init(Game game) {
        super.init(game);

        genericMoveOrBuildRequest(game);
    }

    @Override
    public void selectSquare(Game game, Player player, Point position) {
        Optional<Worker> optWorker = player.getSelectedWorker();

        if (optWorker.isEmpty()) {
            throw new UnsupportedOperationException("Tried to move with no selected worker");
        }

        if (isPositionBlocked(game, getBlockedMoves(game, player, optWorker.get()), position)) {
            throw new IllegalArgumentException("Given position is a forbidden move position by some power");
        }

        if (!getValidMoves(game, player, optWorker.get()).contains(position)) {
            throw new IllegalArgumentException("Invalid move");
        }

        game.getPlayerList().forEach(p -> p.getPower().onMove(player, optWorker.get(), position, game));
    }

    @Override
    public void selectWorker(Game game, Player player, Worker worker) {
        if (!player.getWorkers().contains(worker)) {
            throw new NoSuchElementException("Player does not own this worker");
        }

        if (player.isWorkerLocked()) {
            throw new UnsupportedOperationException("Worker is locked from previous turn");
        }

        player.setSelectedWorker(worker);
        game.notifyObservers(o -> o.availableMovesUpdate(game.getCurrentPlayer(),
                getValidMoves(game, player, worker), getBlockedMoves(game, player, worker)));

        game.askRequest(game.getCurrentPlayer(), EnumRequestType.SELECT_SQUARE);
    }

    @Override
    public void toggleInteraction(Game game, Player player) {
        player.getPower().onToggleInteraction(game);
    }

    @Override
    public boolean shouldShowInteraction(Game game, Player player) {
        return player.getPower().shouldShowInteraction(game);
    }
}
