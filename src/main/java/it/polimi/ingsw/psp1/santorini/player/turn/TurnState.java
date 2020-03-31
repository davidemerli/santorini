package it.polimi.ingsw.psp1.santorini.player.turn;

import it.polimi.ingsw.psp1.santorini.Game;
import it.polimi.ingsw.psp1.santorini.map.Map;
import it.polimi.ingsw.psp1.santorini.map.Worker;
import it.polimi.ingsw.psp1.santorini.player.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
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

    /**
     * Prompted when a square is selected by the player
     * @param position is the square the player is giving as a input
     */
    public abstract void selectSquare(Point position);

    /**
     * Prompted when a worker is selected by the player
     * @param worker is the worker the player is giving as a input
     */
    public abstract void selectWorker(Worker worker);

    /**
     * Prompted when the player is using the power pressing the interaction button
     */
    public abstract void toggleInteraction();

    /**
     * Checks if the button to press in order to activate/deactivate powers
     * should be shown in the current state of the turn
     * @return true if the player can activate/deactivate powers
     */
    public abstract boolean shouldShowInteraction();

    //TODO: check where these should be called from

    /**
     * Checks what squares near the selected worker are unavailable for the current action
     * @return the list of unavailable squares
     */
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

    /**
     * Checks what squares near the selected worker are available for the current action
     * @return the list of available squares
     */
    public List<Point> getValidMoves() {
        if (!player.isWorkerSelected()) {
            return Collections.emptyList();
        }

        return player.getPower().getValidMoves(game);
    }

    /**
     * Returns to the previous state of the map, deleting the last action made
     */
    public void undo() {
        if (previousTurn == null) {
            throw new IllegalStateException("There is no previous move to return to");
        }

        game.setGameMap(previousMap);
        player.setTurnState(previousTurn);
    }
}
