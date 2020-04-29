package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import it.polimi.ingsw.psp1.santorini.model.turn.Move;
import it.polimi.ingsw.psp1.santorini.model.turn.WorkerPlacing;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

//TODO: handle loosing condition whether you cannot build after any move
public class Mortal implements Power {

    protected transient Player player;

    /**
     * {@inheritDoc} <br>
     * <p>
     * Checks valid and blocked moves <br>
     * If selected worker cannot make valid moves or all the moves are blocked, lose condition is set true
     */
    @Override
    public void onBeginTurn(Player player, Game game) {
        if (player.equals(this.player)) {
            game.setTurnState(new Move(game));

            if (player.getWorkers().stream().allMatch(noValidMoves(game))) {
                game.setLoser(player);
            }
        }
    }

    /**
     * Called upon finish turn
     *
     * @param player that is ending his turn
     * @param game   current game
     */
    @Override
    public void onEndTurn(Player player, Game game) {
    }

    /**
     * {@inheritDoc} <br>
     * <p>
     * Checks if you have to build a dome or not in the selected position
     */
    @Override
    public void onBuild(Player player, Worker worker, Point where, Game game) {
        if (player.equals(this.player)) {
            boolean shouldBuildDome = game.getMap().getLevel(where) == 3;

            game.getMap().buildBlock(where, shouldBuildDome);

            game.endTurn();
        }
    }

    /**
     * {@inheritDoc} <br>
     * <p>
     * If worker moved from level 2 to level 3, the player has won the game <br>
     * Locks the chosen worker for the next TurnState
     */
    @Override
    public void onMove(Player player, Worker worker, Point where, Game game) {
        if (player.equals(this.player)) {
            int oldLevel = game.getMap().getLevel(worker.getPosition());
            int newLevel = game.getMap().getLevel(where);

            game.moveWorker(player, worker, where);

            if (newLevel == 3 && oldLevel == 2) {
                player.setWinner(true);
            }

            player.lockWorker();

            game.setTurnState(new Build(game));
        }
    }

    /**
     * {@inheritDoc} <br>
     * By default the bottom is not shown
     *
     * @return boolean is set false, so the bottom is not shown
     */
    @Override
    public boolean shouldShowInteraction(Game game) {
        return false;
    }

    /**
     * {@inheritDoc}
     * <p>
     * By default list of blocked moves is empty.
     * All moves are allowed
     */
    @Override
    public List<Point> getBlockedMoves(Player player, Worker worker, Game game) {
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
    public List<Point> getValidMoves(Worker worker, Game game) {
        if (game.getTurnState() instanceof WorkerPlacing) {
            return game.getMap().getAllSquares().stream()
                    .filter(p -> game.getWorkerOn(p).isEmpty())
                    .collect(Collectors.toUnmodifiableList());
        }

        //worker is null if the player has not selected the current worker
        //valid moves include worker positions
        if(worker == null) {
            return player.getWorkers().stream()
                    .map(Worker::getPosition).collect(Collectors.toList());
        }

        List<Point> neighbors = game.getMap().getNeighbors(worker.getPosition());

        if (game.getTurnState() instanceof Move) {
            return neighbors.stream()
                    .filter(getStandardDomeCheck(game))
                    .filter(getStandardMoveCheck(worker, game))
                    .filter(getStandardWorkerCheck(game))
                    .collect(Collectors.toList());
        } else if (game.getTurnState() instanceof Build) {
            return neighbors.stream()
                    .filter(getStandardDomeCheck(game))
                    .filter(getStandardWorkerCheck(game))
                    .collect(Collectors.toList());
        }  else {
            return Collections.emptyList();
        }
    }

    /**
     * Returns to the previous turn state, should be customized for powers that can possibly do lots of moves
     */
    @Override
    public void undo() {

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

    protected Predicate<Point> getStandardMoveCheck(Worker worker, Game game) {
        return p -> game.getMap().getLevel(p) <= game.getMap().getLevel(worker.getPosition()) + 1;
    }

    protected Predicate<Point> getStandardWorkerCheck(Game game) {
        return p -> game.getWorkerOn(p).isEmpty();
    }

    protected Predicate<Worker> noValidMoves(Game game) {
        Predicate<Worker> noValidMoves = w -> game.getTurnState().getValidMoves(player, w).size() == 0;
        Predicate<Worker> allBlocked = w -> {
            Map<Power, List<Point>> blockedMoves = game.getTurnState().getBlockedMoves(player, w);
            List<Point> notAvailable = blockedMoves.values().stream()
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            return notAvailable.containsAll(game.getTurnState().getValidMoves(player, w));
        };

        return allBlocked.or(noValidMoves);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != getClass()) {
            return false;
        }

        //TODO: add name check ?

        return Objects.equals(this.player, ((Mortal) obj).player);
    }

    public Power copy() {
        try {
            return (Power) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }
}

