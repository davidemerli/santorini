package it.polimi.ingsw.psp1.santorini.model.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.GameState;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TurnState {

    protected Game game;
    protected GameState previousState;//TODO: remove from here and copy manually on power

    public TurnState(Game game) {
        this.game = game;
        this.previousState = copyState();
    }

    public void init() {
    }

    /**
     * Called when the 'Challenger' chooses a starting player
     *
     * @param game current game
     * @param player current player
     * @param chosenPlayerName player chosen to start the game
     */
    public void selectStartingPlayer(Game game, Player player, String chosenPlayerName) {
        throw new UnsupportedOperationException("Not permitted in this state");
    }

    /**
     * Called when power is selected in ChoosePlayerPower or SelectPowers
     *
     * @param game current game
     * @param player current player
     * @param power selected power
     */
    public void selectGod(Game game, Player player, Power power) {
        throw new UnsupportedOperationException("Not permitted in this state");
    }

    /**
     * Called when a square is selected by the player
     *
     * @param player   current player
     * @param position of the selected square
     */
    public void selectSquare(Player player, Point position) {
        throw new UnsupportedOperationException("Not permitted in this state");
    }

    /**
     * Called when a worker is selected by the player
     *
     * @param player current player
     * @param worker selected by the player
     */
    public void selectWorker(Player player, Worker worker) {
        throw new UnsupportedOperationException("Not permitted in this state");
    }

    /**
     * Checks if the custom interaction button should be shown in the current state of the turn
     *
     * @param player current player
     * @return true if the player can activate/deactivate powers
     */
    public abstract boolean shouldShowInteraction(Player player);

    /**
     * Called every time the player clicks on a special button that's customized
     * based on the behaviour of the player current power. <br>
     * <p>
     * For example, if a player has the possibility to build another time in the turn,
     * the button allows him to end the turn instead. <br>
     * <p>
     * Every power is handled differently but uses the same button
     *
     * @param player current player
     */
    public void toggleInteraction(Player player) {
        throw new UnsupportedOperationException("Not permitted in this state");
    }

    /**
     * Checks what squares near the selected worker are unavailable for the current action
     *
     * @param player current player
     * @param worker selected worker
     * @return the list of unavailable squares
     */
    public Map<Power, List<Point>> getBlockedMoves(Player player, Worker worker) {
        Map<Power, List<Point>> map = new HashMap<>();
        game.getPlayerOpponents(player).forEach(p ->
                map.put(p.getPower(), p.getPower().getBlockedMoves(player, worker, game)));

        return map;
    }

    /**
     * Checks what squares near the selected worker are available for the current action
     *
     * @param player current player
     * @param worker selectedWorker
     * @return the list of available squares
     */
    public List<Point> getValidMoves(Player player, Worker worker) {
        return player.getPower().getValidMoves(worker, game);
    }

    /**
     * Returns to the previous state of the map, deleting the last action made
     *
     * @param player current player
     */
    public void undo(Player player) {
        player.getPower().undo();
    }

    /**
     * Utility check for blocked positions
     *
     * @param map   with blocked moves and by which power
     * @param point to be checked
     * @return true if given position is blocked by some power
     */
    public boolean isPositionBlocked(Map<Power, List<Point>> map, Point point) {
        return map.keySet().stream().anyMatch(power -> map.get(power).contains(point));
    }

    public GameState copyState() {
        GameMap copy = game.getMap().copy();
        HashMap<Player, HashMap<Worker, Point>> playerWorkerState = new HashMap<>();

        for (Player player : game.getPlayerList()) {
            HashMap<Worker, Point> workerState = new HashMap<>();
            player.getWorkers().forEach(w -> workerState.put(w, new Point(w.getPosition())));
            playerWorkerState.put(player, workerState);
        }

        TurnState previousTurnState = game.getTurnState();

        return new GameState(copy, previousTurnState, playerWorkerState);
    }
}
