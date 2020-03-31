package it.polimi.ingsw.psp1.santorini.powers;

import it.polimi.ingsw.psp1.santorini.Game;
import it.polimi.ingsw.psp1.santorini.map.SquareData;
import it.polimi.ingsw.psp1.santorini.map.Worker;
import it.polimi.ingsw.psp1.santorini.player.Player;
import it.polimi.ingsw.psp1.santorini.player.turn.Build;
import it.polimi.ingsw.psp1.santorini.player.turn.EndTurn;
import it.polimi.ingsw.psp1.santorini.player.turn.Move;
import it.polimi.ingsw.psp1.santorini.player.turn.TurnState;

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
     * {@inheritDoc}
     * Checks valid and blocked moves
     * If selected worker cannot make valid moves or all the moves are blocked, lose condition is set true
     */
    @Override
    public void onBeginTurn(Game game) {
        player.setTurnState(new Move(player, game));

        List<Point> validMoves = player.getTurnState().getValidMoves();
        List<Point> blockedMoves = player.getTurnState().getBlockedMoves();

        if (validMoves.size() == 0 || blockedMoves.containsAll(validMoves)) {
            player.setLost();
        }
    }

    /**
     * {@inheritDoc}
     * Checks if you have to build a dome or not in the selected position
     *
     * @return end state
     */
    @Override
    public TurnState onYourBuild(Worker worker, Point where, Game game) {
        SquareData squareData = game.getGameMap().getSquareDataAt(where);
        boolean shouldBuildDome = squareData.getLevel() == 3;

        game.buildBlock(where, shouldBuildDome);

        return new EndTurn(player, game);
    }

    /**
     * {@inheritDoc}
     * If worker moved from level 2 to level 3, winner condition is set true
     * Sets worker locked
     *
     * @return build state
     */
    @Override
    public TurnState onYourMove(Worker worker, Point where, Game game) {
        int oldLevel = game.getGameMap().getSquareDataAt(worker.getPosition()).getLevel();
        int newLevel = game.getGameMap().getSquareDataAt(where).getLevel();

        game.moveWorker(worker, where);

        if (newLevel == 3 && oldLevel == 2) {
            player.setWinner();
        }

        player.lockWorker();

        return new Build(player, game);
    }

    /**
     * {@inheritDoc}
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
     * By default list of blocked moves is empty.
     * All moves are allowed
     */
    @Override
    public List<Point> getBlockedMoves(Worker worker, List<Point> validMoves, TurnState playerState, Game game) {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     * Checks that worker can't moves on another worker.
     * Checks that worker can't moves on a dome.
     * Checks that the level of the new position is allowed.
     * Checks the current state of the player
     */
    @Override
    public List<Point> getValidMoves(Game game) {
        /* getSelectedWorker is always != null */
        Point workerPosition = player.getSelectedWorker().getPosition();

        int currentLevel = getLevelAtPosition(game, workerPosition);

        List<Point> neighbors = game.getGameMap().getNeighbors(workerPosition);

        Predicate<Point> domeCheck = p -> !game.getGameMap().getSquareDataAt(p).isDome();

        Predicate<Point> canMoveTo = p -> getLevelAtPosition(game, p) <= currentLevel ||
                getLevelAtPosition(game, p) == currentLevel + 1;

        Predicate<Point> workerCheck = p -> !game.getGameMap().isWorkerOn(p);

        if (player.getTurnState() instanceof Move) {
            return neighbors.stream()
                    .filter(domeCheck.and(canMoveTo).and(workerCheck))
                    .collect(Collectors.toList());
        } else if (player.getTurnState() instanceof Build) {
            return neighbors.stream()
                    .filter(domeCheck.and(workerCheck))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Called when you want to know the level of a point on the map
     */
    int getLevelAtPosition(Game game, Point point) {
        return game.getGameMap().getSquareDataAt(point).getLevel();
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
    public void onOpponentsBuild(Player player, Worker worker, Point where, Game game) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onOpponentsMove(Player player, Worker worker, Point oldPosition, Point where, Game game) {
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public void onToggleInteraction(Game game) {
    }
}
