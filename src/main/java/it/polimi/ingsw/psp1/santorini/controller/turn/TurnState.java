package it.polimi.ingsw.psp1.santorini.controller.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.map.Map;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.Player;

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
        this.previousMap = game.getMap().copy();
    }

    /**
     * Called when a square is selected by the player
     *
     * @param position of the selected square
     */
    public void selectSquare(Point position) {
        throw new UnsupportedOperationException("Not permitted in this state");
    }

    /**
     * Called when a worker is selected by the player
     *
     * @param worker selected by the player
     */
    public void selectWorker(Worker worker) {
        throw new UnsupportedOperationException("Not permitted in this state");
    }

    /**
     * Checks if the custom interaction button should be shown in the current state of the turn
     *
     * @return true if the player can activate/deactivate powers
     */
    public abstract boolean shouldShowInteraction();

    /**
     * Called every time the player clicks on a special button that's customized
     * based on the behaviour of the player current power. <br>
     * <p>
     * For example, if a player has the possibility to build another time in the turn,
     * the button allows him to end the turn instead. <br>
     * <p>
     * Every power is handled differently but uses the same button
     */
    public void toggleInteraction() {
    }

    /**
     * Checks what squares near the selected worker are unavailable for the current action
     *
     * @return the list of unavailable squares
     */
    public List<Point> getBlockedMoves() {
        if (!player.isWorkerSelected()) {
            return new ArrayList<>();
        }

        List<Point> blockedMoves = new ArrayList<>();

        game.getPlayerOpponents(player)
                .forEach(p -> blockedMoves.addAll(p.getPower().getBlockedMoves(player.getSelectedWorker(),
                        this, game)));

        return blockedMoves;
    }

    /**
     * Checks what squares near the selected worker are available for the current action
     *
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
        //TODO: copy and restore worker positions
        if (previousTurn == null) {
            throw new IllegalStateException("There is no previous move to return to");
        }

        game.setMap(previousMap);
        player.setTurnState(previousTurn);
    }
}
