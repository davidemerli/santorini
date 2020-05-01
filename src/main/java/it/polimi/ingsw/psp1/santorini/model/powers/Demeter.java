package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;

import java.util.List;

public class Demeter extends Mortal {

    private boolean hasBuilt;
    private Point oldBuild;

    /**
     * {@inheritDoc}
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
     * If the worker has built once, interaction bottom is shown
     *
     * @return true if worker has built once
     */
    @Override
    public boolean shouldShowInteraction(Game game) {
        return hasBuilt;
    }

    /**
     * {@inheritDoc}
     * If the player press the interaction button, he ends the turn
     */
    @Override
    public void onToggleInteraction(Game game) {
        game.endTurn();
    }

    /**
     * If the worker has built once, the old position is saved
     */
    @Override
    public void onBuild(Player player, Worker worker, Point where, Game game) {
        if (player.equals(this.player)) {
            boolean shouldBuildDome = game.getMap().getLevel(where) == 3;
            game.buildBlock(where, shouldBuildDome);

            if (!hasBuilt) {
                oldBuild = new Point(where);
                hasBuilt = true;

                game.setTurnState(new Build(game));
            } else {
                game.endTurn();
            }
        } else {
            super.onBuild(player, worker, where, game);
        }
    }

    /**
     * {@inheritDoc}
     * If the worker can build a second time, the old position is blocked
     */
    @Override
    public List<Point> getValidMoves(Worker worker, Game game) {
        List<Point> list = super.getValidMoves(worker, game);

        if (game.getTurnState() instanceof Build && worker != null && hasBuilt) {
            list.remove(oldBuild);
        }

        return list;
    }
}
