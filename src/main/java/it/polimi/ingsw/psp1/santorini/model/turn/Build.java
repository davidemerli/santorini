package it.polimi.ingsw.psp1.santorini.model.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;

public class Build extends TurnState {

    public Build(Game game) {
        super(game);
    }

    @Override
    public void init() {
        super.init();

        Player current = game.getCurrentPlayer();
        Worker currentWorker = game.getCurrentPlayer().getSelectedWorker().orElse(null);

        if (current.getSelectedWorker().isEmpty()) {
            game.askRequest(current, EnumRequestType.SELECT_WORKER);
        } else {
            game.askRequest(current, EnumRequestType.SELECT_SQUARE);
        }

        game.notifyObservers(o -> o.availableMovesUpdate(game.getCurrentPlayer(),
                getValidMoves(current, currentWorker), getBlockedMoves(current, currentWorker)));
    }

    @Override
    public void selectSquare(Player player, Point position) {
        if (player.getSelectedWorker().isEmpty()) {
            throw new UnsupportedOperationException("Tried to build with no selected worker");
        }

        Worker worker = player.getSelectedWorker().get();

        if (isPositionBlocked(getBlockedMoves(player, worker), position)) {
            throw new IllegalArgumentException("Given position is a forbidden build position by some power");
        }

        if (!getValidMoves(player, worker).contains(position)) {
            throw new IllegalArgumentException("Invalid build position");
        }

        player.getPower().onBuild(player, worker, position, game);

        game.notifyObservers(o -> o.playerBuild(player, worker, position));
    }

    @Override
    public void selectWorker(Player player, Worker worker) {
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
