package it.polimi.ingsw.psp1.santorini.model.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class WorkerPlacing extends TurnState {

    public WorkerPlacing(Game game) {
        super(game);
    }

    @Override
    public boolean shouldShowInteraction(Player player) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void selectSquare(Player player, Point position) {
        if (game.getWorkerOn(position).isPresent()) {
            throw new IllegalArgumentException("Occupied square");
        }

        player.addWorker(new Worker(position));

        if (player.getWorkers().size() == 2 &&
                game.getPlayerOpponents(player).stream().allMatch(p -> p.getWorkers().size() == 2)) {
            game.setTurnState(new BeginTurn(game));
        } else {
            game.setTurnState(new WorkerPlacing(game));
        }
    }

    @Override
    public List<Point> getValidMoves(Player player, Worker worker) {
        return player.getPower().getValidMoves(worker, game);
    }
}
