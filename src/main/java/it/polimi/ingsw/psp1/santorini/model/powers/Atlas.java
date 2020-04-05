package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.controller.turn.Build;
import it.polimi.ingsw.psp1.santorini.controller.turn.EndTurn;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;

import java.awt.*;

public class Atlas extends Mortal {

    private boolean abilityToggled;

    public Atlas(Player player) {
        super(player);
    }

    /**
     * {@inheritDoc}<br>
     * <p>
     * Reset state
     */
    @Override
    public void onBeginTurn(Game game) {
        super.onBeginTurn(game);
        abilityToggled = false;
    }

    /**
     * {@inheritDoc}
     *
     * @return show interaction bottom during build state
     */
    @Override
    public boolean shouldShowInteraction() {
        return player.getTurnState() instanceof Build;
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
    public void onYourBuild(Worker worker, Point where, Game game) {
        boolean shouldBuildDome = game.getMap().getLevel(where) == 3;

        game.getMap().buildBlock(where, shouldBuildDome || abilityToggled);

        player.setTurnState(new EndTurn(player, game));
    }
}
