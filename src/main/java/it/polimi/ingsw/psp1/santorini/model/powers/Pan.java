package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;

/**
 * Power: Pan wins if moves down at least two levels
 */
public class Pan extends Mortal {

    /**
     * {@inheritDoc}
     * <p>
     * Checks if workers goes down al least two level
     */
    @Override
    public void onMove(Player player, Worker worker, Point where, Game game) {
        if (player.equals(this.player)) {
            int oldLevel = game.getMap().getLevel(worker.getPosition());
            int newLevel = game.getMap().getLevel(where);

            if (oldLevel - newLevel >= 2) {
                player.setWinner(true);
            }
        }

        super.onMove(player, worker, where, game);
    }
}
