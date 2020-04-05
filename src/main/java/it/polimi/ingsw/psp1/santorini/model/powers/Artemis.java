package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.controller.turn.Build;
import it.polimi.ingsw.psp1.santorini.controller.turn.Move;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;

import java.awt.*;
import java.util.List;

public class Artemis extends Mortal {

    private boolean hasMoved;
    private Point oldPosition;

    public Artemis(Player player) {
        super(player);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Reset state
     */
    @Override
    public void onBeginTurn(Game game) {
        hasMoved = false;
        oldPosition = null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the worker has make the first move, show the interaction bottom
     *
     * @return false if worker has make the first move
     */
    @Override
    public boolean shouldShowInteraction() {
        return hasMoved && player.getTurnState() instanceof Move;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onToggleInteraction(Game game) {
        player.setTurnState(new Build(player, game));
    }

    /**
     * {@inheritDoc}
     * <p>
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
     * <p>
     * If the worker must make the first move and the ability toggled is activated you can move one more time
     * otherwise next state is build
     */
    @Override
    public void onYourMove(Worker worker, Point where, Game game) {
        oldPosition = worker.getPosition();
        super.onYourMove(worker, where, game);

        if (!hasMoved) {
            hasMoved = true;
            player.setTurnState(new Move(player, game));
        }
    }
}
