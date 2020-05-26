package it.polimi.ingsw.psp1.santorini.model.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;

import java.util.NoSuchElementException;
import java.util.Optional;

public class Move extends TurnState {

    public Move(Game game) {
        super(game);
    }

    @Override
    public void init() {
        super.init();

        genericMoveOrBuildRequest();
    }

    @Override
    public void selectSquare(Player player, Point position) {
        Optional<Worker> optWorker = player.getSelectedWorker();

        if (optWorker.isEmpty()) {
            throw new UnsupportedOperationException("Tried to move with no selected worker");
        }

        if (isPositionBlocked(getBlockedMoves(player, optWorker.get()), position)) {
            throw new IllegalArgumentException("Given position is a forbidden move position by some power");
        }

        if (!getValidMoves(player, optWorker.get()).contains(position)) {
            throw new IllegalArgumentException("Invalid move");
        }

        Point old = optWorker.get().getPosition();

        game.getPlayerList().forEach(p -> p.getPower().onMove(player, optWorker.get(), position, game));

//        game.notifyObservers(o -> o.playerMove(player, optWorker.get(), old, position));
    }

    @Override
    public void selectWorker(Player player, Worker worker) {
        if (!player.getWorkers().contains(worker)) {
            throw new NoSuchElementException("Player does not own this worker");
        }

        if (player.isWorkerLocked()) {
            throw new UnsupportedOperationException("Worker is locked from previous turn");
        }

        player.setSelectedWorker(worker);
        game.notifyObservers(o -> o.availableMovesUpdate(game.getCurrentPlayer(),
                getValidMoves(player, worker), getBlockedMoves(player, worker)));

        game.askRequest(game.getCurrentPlayer(), EnumRequestType.SELECT_SQUARE);
    }

    @Override
    public void toggleInteraction(Player player) {
        player.getPower().onToggleInteraction(game);
    }

    @Override
    public boolean shouldShowInteraction(Player player) {
        return player.getPower().shouldShowInteraction(game);
    }
}
