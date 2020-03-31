package it.polimi.ingsw.psp1.santorini.powers;

import it.polimi.ingsw.psp1.santorini.Game;
import it.polimi.ingsw.psp1.santorini.map.Worker;
import it.polimi.ingsw.psp1.santorini.player.Player;
import it.polimi.ingsw.psp1.santorini.player.turn.Move;
import it.polimi.ingsw.psp1.santorini.player.turn.TurnState;

import java.awt.*;
import java.util.List;

public class Artemis extends Mortal {

    private boolean abilityToggled;
    private boolean hasMoved;
    private Point oldPosition;

    public Artemis(Player player) {
        super(player);
    }

    /**
     * {@inheritDoc}
     * Reset state
     */
    @Override
    public void onBeginTurn(Game game) {
        abilityToggled = true;
        hasMoved = false;
    }

    /**
     * {@inheritDoc}
     * If the worker has make the first move, show the interaction bottom
     *
     * @return false if worker has make the first move
     */
    @Override
    public boolean shouldShowInteraction() {
        return hasMoved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onToggleInteraction(Game game) {
        abilityToggled = !abilityToggled;
    }

    /**
     * {@inheritDoc}
     * if the worker has moved once, he can move a second time but not in the previous position
     */
    @Override
    public List<Point> getValidMoves(Game game) {
        List<Point> list = super.getValidMoves(game);

        if (player.getTurnState() instanceof Move && hasMoved) {
            list.remove(oldPosition);
        }

        return list;
    }

    /**
     * {@inheritDoc}
     * If the worker must make the first move and the ability toggled is activated you can move one more time
     * otherwise next state is build
     */
    @Override
    public TurnState onYourMove(Worker worker, Point where, Game game) {
        TurnState next = super.onYourMove(worker, where, game);

        if (abilityToggled && !hasMoved) {
            oldPosition = new Point(where);
            hasMoved = true;

            return new Move(player, game);
        }

        return next;
    }
}
