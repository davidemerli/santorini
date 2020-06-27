package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import it.polimi.ingsw.psp1.santorini.model.turn.Move;
import it.polimi.ingsw.psp1.santorini.model.turn.TurnState;

import java.util.List;
import java.util.stream.Collectors;

public class Prometheus extends Mortal {

    private boolean hasMoved;
    private boolean hasBuiltBeforeMoving;

    /**
     * {@inheritDoc}
     * <p>
     * Reset state
     */
    @Override
    public void onBeginTurn(Player player, Game game) {
        super.onBeginTurn(player, game);

        if (player.equals(this.player)) {
            hasMoved = false;
            hasBuiltBeforeMoving = false;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     *
     * @return true if worker has to build the first time
     */
    @Override
    public boolean shouldShowInteraction(Game game) {
        return !hasMoved && !hasBuiltBeforeMoving;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onToggleInteraction(Game game) {
        if(!hasBuiltBeforeMoving) {
            TurnState newState = game.getTurnState() instanceof Move ? new Build() : new Move();

            game.setTurnState(newState);
        }
    }

    @Override
    public void onMove(Player player, Worker worker, Point where, Game game) {
        super.onMove(player, worker, where, game);

        hasMoved = true;
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the worker has built before moving, he can build a second time after the move, but he can't move up
     */
    @Override
    public void onBuild(Player player, Worker worker, Point where, Game game) {
        if (player.equals(this.player)) {
            boolean shouldBuildDome = game.getMap().getLevel(where) == 3;
            game.buildBlock(where, shouldBuildDome);

            if (!hasMoved) {
                player.lockWorker();

                hasBuiltBeforeMoving = true;
                game.setTurnState(new Move());
            } else {
                game.endTurn();
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the worker has built before moving, he can't move up
     */
    @Override
    public List<Point> getValidMoves(Worker worker, Game game) {
        List<Point> list = super.getValidMoves(worker, game);

        if (game.getTurnState() instanceof Move && worker != null && hasBuiltBeforeMoving) {
            int level = game.getMap().getLevel(worker.getPosition());

            return list.stream()
                    .filter(p -> game.getMap().getLevel(p) <= level)
                    .collect(Collectors.toList());
        }

        return list;
    }
}
