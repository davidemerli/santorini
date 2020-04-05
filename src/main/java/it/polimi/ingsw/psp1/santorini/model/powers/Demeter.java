package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.controller.turn.Build;
import it.polimi.ingsw.psp1.santorini.controller.turn.EndTurn;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;

import java.awt.*;
import java.util.List;

public class Demeter extends Mortal {

    private boolean hasBuilt;
    private Point oldBuild;

    public Demeter(Player player) {
        super(player);
    }

    /**
     * {@inheritDoc}
     * Reset state
     */
    @Override
    public void onBeginTurn(Game game) {
        super.onBeginTurn(game);
        hasBuilt = false;
    }

    /**
     * {@inheritDoc}
     * If the worker has built once, interaction bottom is shown
     *
     * @return true if worker has built once
     */
    @Override
    public boolean shouldShowInteraction() {
        return hasBuilt;
    }

    /**
     * {@inheritDoc}
     * If the player press the interaction button, he ends the turn
     */
    @Override
    public void onToggleInteraction(Game game) {
        player.setTurnState(new EndTurn(player, game));
    }

    /**
     * If the worker has built once, the old position is saved
     */
    @Override
    public void onYourBuild(Worker worker, Point where, Game game) {
        super.onYourBuild(worker, where, game);

        if (!hasBuilt) {
            oldBuild = new Point(where);
            hasBuilt = true;
            player.setTurnState(new Build(player, game));
        }
    }

    /**
     * {@inheritDoc}
     * If the worker can build a second time, the old position is blocked
     */
    @Override
    public List<Point> getValidMoves(Game game) {
        List<Point> list = super.getValidMoves(game);

        if (player.getTurnState() instanceof Build && hasBuilt) {
            list.remove(oldBuild);
        }

        return list;
    }
}
