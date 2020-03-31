package it.polimi.ingsw.psp1.santorini.powers;

import it.polimi.ingsw.psp1.santorini.Game;
import it.polimi.ingsw.psp1.santorini.map.Worker;
import it.polimi.ingsw.psp1.santorini.player.Player;
import it.polimi.ingsw.psp1.santorini.player.turn.Build;
import it.polimi.ingsw.psp1.santorini.player.turn.EndTurn;
import it.polimi.ingsw.psp1.santorini.player.turn.TurnState;

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
     * If the player press the interaction bottom, he ends the turn
     */
    @Override
    public void onToggleInteraction(Game game) {
        player.setTurnState(new EndTurn(player, game));
    }

    /**
     * If the worker has built once, the old position is saved
     */
    @Override
    public TurnState onYourBuild(Worker worker, Point where, Game game) {
        TurnState next = super.onYourBuild(worker, where, game);

        if (!hasBuilt) {
            oldBuild = new Point(where);
            hasBuilt = true;
            return new Build(player, game);
        }

        return next;
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
