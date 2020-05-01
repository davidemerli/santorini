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

public class Apollo extends Mortal {

    /**
     * {@inheritDoc}
     * <p>
     * Blocks occupied by enemy worker are included in valid moves
     */
    @Override
    public List<Point> getValidMoves(Worker worker, Game game) {
        if (game.getTurnState() instanceof Move && worker != null) {
            Point wPos = worker.getPosition();
            List<Point> neighbors = game.getMap().getNeighbors(wPos);

            Predicate<Point> enemyWorkerCheck = p -> {
                Optional<Player> optionalPlayer = game.getPlayerOf(game.getWorkerOn(p).get());
                return optionalPlayer.isPresent() && optionalPlayer.get() != player;
            };

            return neighbors.stream()
                    .filter(getStandardDomeCheck(game))
                    .filter(getStandardMoveCheck(worker, game))
                    .filter(getStandardWorkerCheck(game).or(enemyWorkerCheck))
                    .collect(Collectors.toList());
        } else {
            return super.getValidMoves(worker, game);
        }
    }

    /**
     * {@inheritDoc}<br>
     * <p>
     * If worker moves on enemy worker, they are swapped.
     */
    @Override
    public void onMove(Player player, Worker worker, Point where, Game game) {
        if (player.equals(this.player)) {
            Optional<Worker> optWorker = game.getWorkerOn(where);

            if (optWorker.isPresent()) {
                Point oldPos = worker.getPosition();
                optWorker.get().setPosition(oldPos);//switch
            }
        }

        super.onMove(player, worker, where, game);//normal moving behaviour
    }
}
