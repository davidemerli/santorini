package it.polimi.ingsw.psp1.santorini.player.turn;

import it.polimi.ingsw.psp1.santorini.Game;
import it.polimi.ingsw.psp1.santorini.map.Worker;
import it.polimi.ingsw.psp1.santorini.player.Player;

import java.awt.*;

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

        Point oldPosition = player.getSelectedWorker().getPosition();

        TurnState nextState = player.getPower().onYourMove(player.getSelectedWorker(), position, game);

        game.getPlayerList().stream()
                .filter(p -> p != player)
                .forEach(p -> p.getPower().onOpponentsMove(player, player.getSelectedWorker(), oldPosition, position, game));

        player.setTurnState(nextState);
    }

    @Override
    public void selectWorker(Worker worker) {
        if (player.isWorkerLocked()) {
            throw new UnsupportedOperationException("Worker is locked from previous turn");
        }

        //TODO: check if he got moves to do

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