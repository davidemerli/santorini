package it.polimi.ingsw.psp1.santorini.model.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.Player;

import java.awt.*;
import java.util.NoSuchElementException;

public class Move extends TurnState {

    public Move(Player player, Game game) {
        super(player, game);
    }

    @Override
    public void selectSquare(Point position) {
        if (!player.isWorkerSelected()) {
            throw new UnsupportedOperationException("Tried to move with no selected worker");
        }

        if (getBlockedMoves().contains(position)) {
            throw new IllegalArgumentException("Given position is a forbidden move position by some power");
        }

        if (!getValidMoves().contains(position)) {
            throw new IllegalArgumentException("Invalid move");
        }

        game.getPlayerList().stream()
                .filter(p -> p != player)
                .forEach(p -> p.getPower().onOpponentsMove(player, position, game));

        player.getPower().onYourMove(player.getSelectedWorker(), position, game);
    }

    @Override
    public void selectWorker(Worker worker) {
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
    public void toggleInteraction() {
        player.getPower().onToggleInteraction(game);
    }

    @Override
    public boolean shouldShowInteraction() {
        return player.getPower().shouldShowInteraction();
    }
}
