package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import it.polimi.ingsw.psp1.santorini.model.turn.Move;

public class Triton extends Mortal {

    private boolean hasMoved;

    /**
     * {@inheritDoc}
     * <p>
     * Checks at the beginning of turn if the worker has moved
     */
    @Override
    public void onBeginTurn(Player player, Game game) {
        super.onBeginTurn(player, game);

        if (player.equals(this.player)) {
            hasMoved = false;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the worker has moved the first time, show the interaction bottom
     *
     * @return true if the worker has moved the first time
     */
    @Override
    public boolean shouldShowInteraction(Game game) {
        return game.getTurnState() instanceof Move && hasMoved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onToggleInteraction(Game game) {
        game.setTurnState(new Build());
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the worker is on perimeter, he can move again
     */
    @Override
    public void onMove(Player player, Worker worker, Point where, Game game) {
        super.onMove(player, worker, where, game);

        if (player.equals(this.player) && game.getMap().isPerimeter(where)) {
            if (!hasMoved) {
                hasMoved = true;
            }

            game.setTurnState(new Move());
        }
    }
}
