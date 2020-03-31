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

public class Apollo extends Mortal {

    public Apollo(Player player) {
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

        Predicate<Point> domeCheck = p -> !game.getGameMap().getSquareDataAt(p).isDome();

        Predicate<Point> canMoveTo = p -> getLevelAtPosition(game, p) <= currentLevel ||
                getLevelAtPosition(game, p) == currentLevel + 1;

        Predicate<Point> workerCheck = p -> !game.getGameMap().isWorkerOn(p);

        Predicate<Point> isEnemyWorker = p -> getWorkerOn(p, game).isPresent() &&
                getWorkerOn(p, game).get().getPlayer() != player;

        return neighbors.stream()
                .filter(domeCheck.and(canMoveTo).and(workerCheck.or(isEnemyWorker)))
                .collect(Collectors.toList());
    }

    @Override
    public TurnState onYourMove(Worker worker, Point where, Game game) {
        Optional<Worker> optWorker = getWorkerOn(where, game);

        if (optWorker.isPresent()) {
            Point oldPosition = worker.getPosition();
            game.getGameMap().removeWorker(optWorker.get());

            TurnState next = super.onYourMove(worker, where, game);
            game.getGameMap().addWorker(new Worker(optWorker.get().getPlayer(), oldPosition));

            return next;
        }

        return super.onYourMove(worker, where, game);
    }

    private Optional<Worker> getWorkerOn(Point position, Game game) {
        return game.getGameMap().getWorkersList().stream()
                .filter(w -> w.getPosition().equals(position))
                .findFirst();
    }
}
