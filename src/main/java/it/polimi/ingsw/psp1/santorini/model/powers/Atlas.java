package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;

public class Atlas extends Mortal {

    private boolean abilityToggled;

    /**
     * {@inheritDoc}<br>
     * <p>
     * Reset state
     */
    @Override
    public void onBeginTurn(Player player, Game game) {
        super.onBeginTurn(player, game);

        if (player.equals(this.player)) {
            abilityToggled = false;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return show interaction bottom during build state
     */
    @Override
    public boolean shouldShowInteraction(Game game) {
        return game.getTurnState() instanceof Build;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onToggleInteraction(Game game) {
        abilityToggled = !abilityToggled;
    }

    /**
     * {@inheritDoc}<br>
     * <p>
     * A dome can be built everywhere by worker if the ability toggle is true or if is the third level
     */
    @Override
    public void onBuild(Player player, Worker worker, Point where, Game game) {
        if (player.equals(this.player)) {
            game.buildBlock(where, abilityToggled);

            game.endTurn();
        } else {
            super.onBuild(player, worker, where, game);
        }
    }
}
