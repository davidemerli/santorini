package it.polimi.ingsw.PSP1.santorini.player.turn;

import it.polimi.ingsw.PSP1.santorini.Game;
import it.polimi.ingsw.PSP1.santorini.map.Worker;
import it.polimi.ingsw.PSP1.santorini.player.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Build extends TurnState {

    public Build(Player player, Game game) {
        super(player, game);
    }

    @Override
    public void selectSquare(Point position) {
        if (!player.isWorkerLocked()) {
            throw new UnsupportedOperationException("Tried to build with no selected worker");
        }

        if(getBlockedMoves().contains(position)) {
            throw new IllegalArgumentException("Given position is a forbidden build position by some power");
        }

        if(!getValidMoves().contains(position)) {
            throw new IllegalArgumentException("Invalid build position");
        }

        game.getPlayerList().stream()
                .filter(p -> p != player)
                .forEach(p -> p.getPower().onOpponentsBuild(player, player.getSelectedWorker(), position, game));

        TurnState nextState = player.getPower().onYourBuild(player.getSelectedWorker(), position, game);

        player.setTurnState(nextState);
    }

    @Override
    public void selectWorker(Worker worker) {
        if(player.isWorkerLocked()) {
            throw new UnsupportedOperationException("Worker is locked from previous turn");
        }

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

    //TODO: check where these should be called from

    @Override
    public List<Point> getBlockedMoves() {
        if(!player.isWorkerSelected()) {
            return new ArrayList<>();
        }

        return player.getPower().getBlockedMoves(game);
    }

    @Override
    public List<Point> getValidMoves() {
        if(!player.isWorkerSelected()) {
            return new ArrayList<>();
        }

        return player.getPower().getValidMoves(game);
    }
}
