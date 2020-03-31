package it.polimi.ingsw.psp1.santorini.powers;

import it.polimi.ingsw.psp1.santorini.Game;
import it.polimi.ingsw.psp1.santorini.map.SquareData;
import it.polimi.ingsw.psp1.santorini.map.Worker;
import it.polimi.ingsw.psp1.santorini.player.Player;
import it.polimi.ingsw.psp1.santorini.player.turn.Build;
import it.polimi.ingsw.psp1.santorini.player.turn.EndTurn;
import it.polimi.ingsw.psp1.santorini.player.turn.TurnState;

import java.awt.*;

public class Atlas extends Mortal {

    private boolean abilityToggled;

    public Atlas(Player player) {
        super(player);
    }

    /**
     * {@inheritDoc}
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
        return player.getGameState() instanceof Build;
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
     * A dome can be built everywhere by worker if the ability toggle is true or if is the third level
     */
    @Override
    public TurnState onYourBuild(Worker worker, Point where, Game game) {
        SquareData squareData = game.getGameMap().getSquareDataAt(where);
        boolean shouldBuildDome = squareData.getLevel() == 3;

        game.buildBlock(where, shouldBuildDome || abilityToggled);

        return new EndTurn(player, game);
    }
}
