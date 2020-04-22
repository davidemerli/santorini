package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import it.polimi.ingsw.psp1.santorini.model.turn.Move;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Minotaur extends Mortal {

    @Override
    public List<Point> getValidMoves(Worker worker, Game game) {
        if (!(game.getTurnState() instanceof Move)) {
            return super.getValidMoves(worker, game);
        }

        List<Point> neighbors = game.getMap().getNeighbors(worker.getPosition());

        Predicate<Point> enemyWorkerCheck = p -> {
            Optional<Player> optionalPlayer = game.getPlayerOf(game.getWorkerOn(p).get());
            return optionalPlayer.isPresent() && optionalPlayer.get() != player;
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

    @Override
    public void onMove(Player player, Worker worker, Point where, Game game) {
        if (player.equals(this.player)) {
            Optional<Worker> optWorker = game.getWorkerOn(where);

            if (optWorker.isPresent()) {
                Point pushPos = getPushLocation(worker.getPosition(), where);

                optWorker.get().setPosition(pushPos);
            }
        }

        super.onMove(player, worker, where, game);
    }

    private Point getPushLocation(Point position, Point opponent) {
        int[] diff = new int[]{position.x - opponent.x, position.y - opponent.y};

        return new Point(opponent.x - diff[0], opponent.y - diff[1]);
    }
}
