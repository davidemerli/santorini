package it.polimi.ingsw.psp1.santorini.powers;

import it.polimi.ingsw.psp1.santorini.Game;
import it.polimi.ingsw.psp1.santorini.map.Worker;
import it.polimi.ingsw.psp1.santorini.player.Player;
import it.polimi.ingsw.psp1.santorini.player.turn.Build;
import it.polimi.ingsw.psp1.santorini.player.turn.TurnState;

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

        Point workerPosition = player.getSelectedWorker().getPosition();
        int currentLevel = getLevelAtPosition(game, workerPosition);

        List<Point> neighbors = game.getGameMap().getNeighbors(workerPosition);

        //TODO: try to simplify predicates

        Predicate<Point> domeCheck = p -> !game.getGameMap().getSquareDataAt(p).isDome();

        Predicate<Point> canMoveTo = p -> getLevelAtPosition(game, p) <= currentLevel ||
                getLevelAtPosition(game, p) == currentLevel + 1;

        Predicate<Point> workerCheck = p -> !game.getGameMap().isWorkerOn(p);

        Predicate<Point> isEnemyWorker = p -> getWorkerOn(p, game).isPresent() &&
                getWorkerOn(p, game).get().getPlayer() != player;

        Predicate<Point> isValidPosition = p -> {
            Point pushPos = getPushLocation(workerPosition, p);

            return !game.getGameMap().isPositionOutOfMap(p) &&
                    !game.getGameMap().getSquareDataAt(pushPos).isDome() &&
                    !game.getGameMap().isWorkerOn(pushPos);
        };

        return neighbors.stream()
                .filter(domeCheck.and(canMoveTo).and(workerCheck.or(isEnemyWorker.and(isValidPosition))))
                .collect(Collectors.toList());
    }

    @Override
    public TurnState onYourMove(Worker worker, Point where, Game game) {
        Optional<Worker> optWorker = getWorkerOn(where, game);

        if (optWorker.isPresent()) {
            Point pushPos = getPushLocation(worker.getPosition(), where);

            game.getGameMap().moveWorker(optWorker.get(), pushPos);
        }

        return super.onYourMove(worker, where, game);
    }

    private Point getPushLocation(Point position, Point opponent) {
        int[] diff = new int[]{position.x - opponent.x, position.y - opponent.y};

        return new Point(opponent.x + diff[0], opponent.y + diff[1]);
    }

    private Optional<Worker> getWorkerOn(Point position, Game game) {
        return game.getGameMap().getWorkersList().stream()
                .filter(w -> w.getPosition().equals(position))
                .findFirst();
    }
}
