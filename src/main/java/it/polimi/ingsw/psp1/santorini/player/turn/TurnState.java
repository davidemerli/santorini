package it.polimi.ingsw.psp1.santorini.player.turn;

import it.polimi.ingsw.psp1.santorini.Game;
import it.polimi.ingsw.psp1.santorini.map.Map;
import it.polimi.ingsw.psp1.santorini.map.Worker;
import it.polimi.ingsw.psp1.santorini.player.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class TurnState {

    protected Player player;
    protected Game game;
    protected TurnState previousTurn;
    protected Map previousMap;

    public TurnState(Player player, Game game) {
        this.player = player;
        this.game = game;
        this.previousTurn = player.getTurnState();
        this.previousMap = game.getGameMap();
    }

    public abstract void selectSquare(Point position);

    public abstract void selectWorker(Worker worker);

    public abstract void toggleInteraction();

    public abstract boolean shouldShowInteraction();

    //TODO: check where these should be called from
    public List<Point> getBlockedMoves() {
        if (!player.isWorkerSelected()) {
            return new ArrayList<>();
        }

        List<Point> blockedMoves = new ArrayList<>();

        game.getPlayerList().stream()
                .filter(p -> p != player)
                .forEach(p -> blockedMoves.addAll(
                        p.getPower().getBlockedMoves(
                                p.getSelectedWorker(),
                                getValidMoves(),
                                this,
                                game)));

        return blockedMoves;
    }

    public List<Point> getValidMoves() {
        if (!player.isWorkerSelected()) {
            return new ArrayList<>();
        }

        return player.getPower().getValidMoves(game);
    }

    public void undo() {
        if (previousTurn == null) {
            throw new IllegalStateException("There is no previous move to return to");
        }

        game.setGameMap(previousMap);
        player.setTurnState(previousTurn);
    }
}
