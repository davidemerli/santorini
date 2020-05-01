package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import it.polimi.ingsw.psp1.santorini.model.turn.Move;

import java.util.List;

public class Artemis extends Mortal {

    private boolean hasMoved;
    private Point oldPosition;

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
            oldPosition = null;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the worker has make the first move, show the interaction bottom
     *
     * @return false if worker has make the first move
     */
    @Override
    public boolean shouldShowInteraction(Game game) {
        return hasMoved && game.getTurnState() instanceof Move;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onToggleInteraction(Game game) {
        game.setTurnState(new Build(game));
    }

    /**
     * {@inheritDoc}
     * <p>
     * if the worker has moved once, he can move a second time but not in the previous position
     */
    @Override
    public List<Point> getValidMoves(Worker worker, Game game) {
        List<Point> list = super.getValidMoves(worker, game);

        if (game.getTurnState() instanceof Move && worker != null && hasMoved) {
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
    public void onMove(Player player, Worker worker, Point where, Game game) {
        if (player.equals(this.player)) {
            oldPosition = worker.getPosition();
        }

        super.onMove(player, worker, where, game);

        if (player.equals(this.player) && !hasMoved) {
            hasMoved = true;

            game.setTurnState(new Move(game));
        }
    }
}
