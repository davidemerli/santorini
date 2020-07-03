package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;

import java.util.Collections;
import java.util.List;

/**
 * Power: Hephaestus can build one additional block (not dome) on top of his first block.
 */
public class Hephaestus extends Mortal {

    private boolean hasBuilt;
    private Point oldBuild;

    /**
     * {@inheritDoc}
     * <p>
     * Reset state
     */
    @Override
    public void onBeginTurn(Player player, Game game) {
        if (player.equals(this.player)) {
            oldBuild = null;
            hasBuilt = false;
        }

        super.onBeginTurn(player, game);
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the worker has make the first build, show the interaction bottom
     *
     * @return true if has make the first build
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
     * At the end of the turn the worker can build a block (not dome) again on the same position
     */
    @Override
    public void onBuild(Player player, Worker worker, Point where, Game game) {
        if (player.equals(this.player)) {
            boolean shouldBuildDome = game.getMap().getLevel(where) == 3;
            game.buildBlock(where, shouldBuildDome);

            if (!hasBuilt && game.getMap().getLevel(where) < 3) {
                oldBuild = new Point(where);
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
     * The second time, the worker can build another block only above the previous
     */
    @Override
    public void onEndTurn(Player player, Game game) {
        super.onEndTurn(player, game);
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the player has built once, it can build again on the same spot
     */
    @Override
    public List<Point> getValidMoves(Worker worker, Game game) {
        if (game.getTurnState() instanceof Build && worker != null && hasBuilt) {
            return Collections.singletonList(oldBuild);
        }

        return super.getValidMoves(worker, game);
    }
}
