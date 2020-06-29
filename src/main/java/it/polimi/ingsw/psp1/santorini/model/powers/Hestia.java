package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Power: Hestia can build one additional time but non in the perimeter zone
 */
public class Hestia extends Mortal {

    private boolean hasBuilt;

    /**
     * {@inheritDoc}
     * <p>
     * Reset state
     */
    @Override
    public void onBeginTurn(Player player, Game game) {
        super.onBeginTurn(player, game);

        if (player.equals(this.player)) {
            hasBuilt = false;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the worker has built the first time, show interaction bottom
     *
     * @return true if the worker has built the first time
     */
    @Override
    public boolean shouldShowInteraction(Game game) {
        return hasBuilt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onToggleInteraction(Game game) {
        game.endTurn();
    }

    /**
     * {@inheritDoc}
     * <p>
     * At the end of the turn, the worker can build another block but not in the perimeter
     */
    @Override
    public void onBuild(Player player, Worker worker, Point where, Game game) {
        if (player.equals(this.player)) {
            boolean shouldBuildDome = game.getMap().getLevel(where) == 3;
            game.buildBlock(where, shouldBuildDome);

            if (!hasBuilt) {
                hasBuilt = true;

                game.setTurnState(new Build());
            } else {
                game.endTurn();
            }
        } else {
            super.onBuild(player, worker, where, game);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * The second time, the worker can build another block but not in the perimeter
     */
    @Override
    public List<Point> getValidMoves(Worker worker, Game game) {
        List<Point> list = super.getValidMoves(worker, game);

        if (game.getTurnState() instanceof Build && worker != null && hasBuilt) {
            return list.stream()
                    .filter(point -> !game.getMap().isPerimeter(point))
                    .collect(Collectors.toList());
        }

        return list;
    }
}
