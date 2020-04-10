package it.polimi.ingsw.psp1.santorini.model.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.Player;

import java.awt.*;

public class Build extends TurnState {

    public Build(Game game) {
        super(game);
    }

    @Override
    public void selectSquare(Player player, Point position) {
        if (!player.isWorkerSelected()) {
            throw new UnsupportedOperationException("Tried to build with no selected worker");
        }

        if (isPositionBlocked(getBlockedMoves(player, player.getSelectedWorker()), position)) {
            throw new IllegalArgumentException("Given position is a forbidden build position by some power");
        }

        if (!getValidMoves(player, player.getSelectedWorker()).contains(position)) {
            throw new IllegalArgumentException("Invalid build position");
        }

        player.getPower().onBuild(player, player.getSelectedWorker(), position, game);
    }

    @Override
    public void selectWorker(Player player, Worker worker) {
        if (player.isWorkerLocked()) {
            throw new UnsupportedOperationException("Worker is locked from previous turn");
        }

        player.setSelectedWorker(worker);
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
