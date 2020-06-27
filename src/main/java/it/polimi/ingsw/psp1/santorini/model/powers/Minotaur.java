package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.turn.Move;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Minotaur extends Mortal {

    /**
     * {@inheritDoc}
     * <p>
     * Adds worker's opponent position as valid moves
     */
    @Override
    public List<Point> getValidMoves(Worker worker, Game game) {
        if (!(game.getTurnState() instanceof Move) || worker == null) {
            return super.getValidMoves(worker, game);
        }

        List<Point> neighbors = game.getMap().getNeighbors(worker.getPosition());

        Predicate<Point> enemyWorkerCheck = p -> {
            Optional<Player> optionalPlayer = game.getPlayerOf(game.getWorkerOn(p).get());
            return optionalPlayer.isPresent() && !optionalPlayer.get().equals(player);
        };

        Predicate<Point> isValidPosition = p -> {
            Point pushPos = getPushLocation(worker.getPosition(), p);

            if (game.getMap().isPositionOutOfMap(p) || game.getMap().isPositionOutOfMap(pushPos)) {
                return false;
            }

            return !game.getMap().hasDome(pushPos) && game.getWorkerOn(pushPos).isEmpty();
        };

        return neighbors.stream()
                .filter(getStandardDomeCheck(game))
                .filter(getStandardMoveCheck(worker, game))
                .filter(getStandardWorkerCheck(game).or(enemyWorkerCheck.and(isValidPosition)))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     * <p>
     * The worker can move into an opponent worker's space, if this one can be forced
     * one space straight backwards to an unoccupied space, at any level
     */
    @Override
    public void onMove(Player player, Worker worker, Point where, Game game) {
        if (player.equals(this.player)) {
            Optional<Worker> optWorker = game.getWorkerOn(where);

            if (optWorker.isPresent()) {
                Point pushPos = getPushLocation(worker.getPosition(), where);

                Optional<Player> opponent = game.getPlayerOf(optWorker.get());

                if (opponent.isPresent()) {
                    game.moveWorker(opponent.get(), optWorker.get(), pushPos);
                } else {
                    throw new IllegalStateException("Player of opponent worker not found");
                }
            }
        }

        super.onMove(player, worker, where, game);
    }

    /**
     * Calculates the new position of the worker's opponent
     *
     * @param position current position
     * @param opponent worker's opponent position
     * @return new position of the worker's opponent
     */
    private Point getPushLocation(Point position, Point opponent) {
        int[] diff = new int[]{position.x - opponent.x, position.y - opponent.y};

        return new Point(opponent.x - diff[0], opponent.y - diff[1]);
    }
}
