package it.polimi.ingsw.psp1.santorini.model.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;

import java.awt.*;
import java.util.NoSuchElementException;

public class Move extends TurnState {

    public Move(Game game) {
        super(game);
    }

    @Override
    public void selectSquare(Player player, Point position) {
        if (!player.isWorkerSelected()) {
            throw new UnsupportedOperationException("Tried to move with no selected worker");
        }

        if (isPositionBlocked(getBlockedMoves(player, player.getSelectedWorker()), position)) {
            throw new IllegalArgumentException("Given position is a forbidden move position by some power");
        }

        if (!getValidMoves(player, player.getSelectedWorker()).contains(position)) {
            throw new IllegalArgumentException("Invalid move");
        }

        game.getPlayerList().forEach(p -> p.getPower().onMove(player, player.getSelectedWorker(), position, game));
    }

    @Override
    public void selectWorker(Player player, Worker worker) {
        if (!player.getWorkers().contains(worker)) {
            throw new NoSuchElementException("Player does not own this worker");
        }

        if (player.isWorkerLocked()) {
            throw new UnsupportedOperationException("Worker is locked from previous turn");
        }

        //TODO: check if he got moves to do
        //should we block selection if no moves are

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
