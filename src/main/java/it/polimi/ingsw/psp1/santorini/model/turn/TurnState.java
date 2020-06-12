package it.polimi.ingsw.psp1.santorini.model.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class TurnState implements Cloneable {

    public void init(Game game) {
        game.notifyObservers(o -> o.gameUpdate(game, false));
    }

    /**
     * Called when the 'Challenger' chooses a starting player
     *
     * @param game             current game
     * @param player           current player
     * @param chosenPlayerName player chosen to start the game
     */
    public void selectStartingPlayer(Game game, Player player, String chosenPlayerName) {
        throw new UnsupportedOperationException("Not permitted in this state");
    }

    /**
     * Called when power is selected in ChoosePlayerPower or SelectPowers
     *
     * @param game   current game
     * @param player current player
     * @param power  selected power
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
    public void selectSquare(Game game, Player player, Point position) {
        throw new UnsupportedOperationException("Not permitted in this state");
    }

    /**
     * Called when a worker is selected by the player
     *
     * @param player current player
     * @param worker selected by the player
     */
    public void selectWorker(Game game, Player player, Worker worker) {
        throw new UnsupportedOperationException("Not permitted in this state");
    }

    /**
     * Checks if the custom interaction button should be shown in the current state of the turn
     *
     * @param player current player
     * @return true if the player can activate/deactivate powers
     */
    public abstract boolean shouldShowInteraction(Game game, Player player);

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
    public void toggleInteraction(Game game, Player player) {
        throw new UnsupportedOperationException("Not permitted in this state");
    }

    /**
     * Checks what squares near the selected worker are unavailable for the current action
     *
     * @param player current player
     * @param worker selected worker
     * @return the list of unavailable squares
     */
    public Map<Power, List<Point>> getBlockedMoves(Game game, Player player, Worker worker) {
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
    public List<Point> getValidMoves(Game game, Player player, Worker worker) {
        return player.getPower().getValidMoves(worker, game);
    }

    /**
     * Returns to the previous state of the map, deleting the last action made
     *
     * @param player current player
     */
    public void undo(Game game, Player player) {
        game.restoreSavedState();
    }

    /**
     * Utility check for blocked positions
     *
     * @param map   with blocked moves and by which power
     * @param point to be checked
     * @return true if given position is blocked by some power
     */
    public boolean isPositionBlocked(Game game, Map<Power, List<Point>> map, Point point) {
        return map.keySet().stream().anyMatch(power -> map.get(power).contains(point));
    }

    /**
     * Sends a request to select a worker or a square if the worker has already been selected
     */
    protected void genericMoveOrBuildRequest(Game game) {
        Player current = game.getCurrentPlayer();
        Optional<Worker> optWorker = current.getSelectedWorker();

        if (optWorker.isEmpty()) {
            game.askRequest(current, EnumRequestType.SELECT_WORKER);
        } else {
            game.askRequest(current, EnumRequestType.SELECT_SQUARE);
        }

        List<Point> validMoves = getValidMoves(game, current, optWorker.orElse(null));
        Map<Power, List<Point>> blockedMoves = getBlockedMoves(game, current, optWorker.orElse(null));

        game.notifyObservers(o -> o.availableMovesUpdate(game.getCurrentPlayer(), validMoves, blockedMoves));
    }

    public TurnState copy() {
        try {
            return (TurnState) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
