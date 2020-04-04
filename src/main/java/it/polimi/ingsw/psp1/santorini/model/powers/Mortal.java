package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.controller.turn.Build;
import it.polimi.ingsw.psp1.santorini.controller.turn.EndTurn;
import it.polimi.ingsw.psp1.santorini.controller.turn.Move;
import it.polimi.ingsw.psp1.santorini.controller.turn.TurnState;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

//TODO: handle loosing condition whether you cannot build after any move
public class Mortal implements Power {

    protected final Player player;

    public Mortal(Player player) {
        this.player = player;
    }

    /**
     * {@inheritDoc} <br>
     * <p>
     * Checks valid and blocked moves <br>
     * If selected worker cannot make valid moves or all the moves are blocked, lose condition is set true
     */
    @Override
    public void onBeginTurn(Game game) {
        player.setTurnState(new Move(player, game));

        List<Point> validMoves = player.getTurnState().getValidMoves();
        List<Point> blockedMoves = player.getTurnState().getBlockedMoves();

        if (validMoves.size() == 0 || blockedMoves.containsAll(validMoves)) {
            player.setLost(true);
        }
    }

    /**
     * Called on the beginning of enemy turn
     *
     * @param game   current game
     * @param player enemy player
     */
    @Override
    public void onEnemyBeginTurn(Game game, Player player) {

    }

    /**
     * {@inheritDoc} <br>
     * <p>
     * Checks if you have to build a dome or not in the selected position
     */
    @Override
    public void onYourBuild(Worker worker, Point where, Game game) {
        boolean shouldBuildDome = game.getMap().getLevel(where) == 3;

        game.getMap().buildBlock(where, shouldBuildDome);

        player.setTurnState(new EndTurn(player, game));
    }

    /**
     * {@inheritDoc} <br>
     * <p>
     * If worker moved from level 2 to level 3, the player has won the game <br>
     * Locks the chosen worker for the next TurnState
     */
    @Override
    public void onYourMove(Worker worker, Point where, Game game) {
        int oldLevel = game.getMap().getLevel(worker.getPosition());
        int newLevel = game.getMap().getLevel(where);

        game.moveWorker(player, worker, where);

        if (newLevel == 3 && oldLevel == 2) {
            player.setWinner(true);
        }

        player.lockWorker();

        player.setTurnState(new Build(player, game));
    }

    /**
     * {@inheritDoc} <br>
     * By default the bottom is not shown
     *
     * @return boolean is set false, so the bottom is not shown
     */
    @Override
    public boolean shouldShowInteraction() {
        return false;
    }

    /**
     * {@inheritDoc}
     * <p>
     * By default list of blocked moves is empty.
     * All moves are allowed
     */
    @Override
    public List<Point> getBlockedMoves(Worker worker, TurnState playerState, Game game) {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Excludes from possible moves:
     * if on Move:
     * - positions with other workers
     * - positions with domes on them
     * - positions with upward height difference > 2
     * if on Build:
     * - positions with domes on them
     * - positions with workers on them
     */
    @Override
    public List<Point> getValidMoves(Game game) {
        /* getSelectedWorker is always != null */
        Point wPos = player.getSelectedWorker().getPosition();
        List<Point> neighbors = game.getMap().getNeighbors(wPos);

        if (player.getTurnState() instanceof Move) {
            return neighbors.stream()
                    .filter(getStandardDomeCheck(game))
                    .filter(getStandardMoveCheck(game))
                    .filter(getStandardWorkerCheck(game))
                    .collect(Collectors.toList());
        } else if (player.getTurnState() instanceof Build) {
            return neighbors.stream()
                    .filter(getStandardDomeCheck(game))
                    .filter(getStandardWorkerCheck(game))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndTurn(Game game) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEnemyEndTurn(Game game, Player player) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onOpponentsBuild(Player player, Worker worker, Point where, Game game) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onOpponentsMove(Player player, Point where, Game game) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onToggleInteraction(Game game) {
    }

    protected Predicate<Point> getStandardDomeCheck(Game game) {
        return p -> !game.getMap().hasDome(p);
    }

    protected Predicate<Point> getStandardMoveCheck(Game game) {
        Point wPos = player.getSelectedWorker().getPosition();
        int currentLevel = game.getMap().getLevel(wPos);

        return p -> game.getMap().getLevel(p) <= currentLevel + 1;
    }

    protected Predicate<Point> getStandardWorkerCheck(Game game) {
        return p -> game.getWorkerOn(p).isEmpty();
    }
}
