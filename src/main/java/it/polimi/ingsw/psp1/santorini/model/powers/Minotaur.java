package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.controller.turn.Build;
import it.polimi.ingsw.psp1.santorini.controller.turn.TurnState;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Minotaur extends Mortal {

    public Minotaur(Player player) {
        super(player);
    }

    @Override
    public List<Point> getValidMoves(Game game) {
        if (player.getTurnState() instanceof Build) {
            return super.getValidMoves(game);
        }

        Point wPos = player.getSelectedWorker().getPosition();
        List<Point> neighbors = game.getMap().getNeighbors(wPos);

        Predicate<Point> enemyWorkerCheck = p -> {
            Optional<Player> optionalPlayer = game.getPlayerOf(game.getWorkerOn(p).get());
            return optionalPlayer.isPresent() && optionalPlayer.get() != player;
        };

        Predicate<Point> isValidPosition = p -> {
            Point pushPos = getPushLocation(wPos, p);

            return !game.getMap().isPositionOutOfMap(p) && !game.getMap().hasDome(pushPos) &&
                    game.getWorkerOn(pushPos).isEmpty();
        };

        return neighbors.stream()
                .filter(getStandardDomeCheck(game))
                .filter(getStandardMoveCheck(game))
                .filter(getStandardWorkerCheck(game).or(enemyWorkerCheck.and(isValidPosition)))
                .collect(Collectors.toList());
    }

    @Override
    public void onYourMove(Worker worker, Point where, Game game) {
        Optional<Worker> optWorker = game.getWorkerOn(where);

        if (optWorker.isPresent()) {
            Point pushPos = getPushLocation(worker.getPosition(), where);

            optWorker.get().setPosition(pushPos);
        }

        super.onYourMove(worker, where, game);
    }

    private Point getPushLocation(Point position, Point opponent) {
        int[] diff = new int[]{position.x - opponent.x, position.y - opponent.y};

        return new Point(opponent.x - diff[0], opponent.y - diff[1]);
    }
}
