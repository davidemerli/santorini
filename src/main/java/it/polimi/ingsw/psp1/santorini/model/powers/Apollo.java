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

public class Apollo extends Mortal {

    public Apollo(Player player) {
        super(player);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Blocks occupied by enemy worker are included in valid moves
     */
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

        return neighbors.stream()
                .filter(getStandardDomeCheck(game))
                .filter(getStandardMoveCheck(game))
                .filter(getStandardWorkerCheck(game).or(enemyWorkerCheck))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}<br>
     * <p>
     * If worker moves on enemy worker, they are swapped.
     */
    @Override
    public void onYourMove(Worker worker, Point where, Game game) {
        Optional<Worker> optWorker = game.getWorkerOn(where);

        if (optWorker.isPresent()) {
            Point oldPos = player.getSelectedWorker().getPosition();
            optWorker.get().setPosition(oldPos);//switch
        }

        super.onYourMove(worker, where, game);//normal moving behaviour
    }
}
