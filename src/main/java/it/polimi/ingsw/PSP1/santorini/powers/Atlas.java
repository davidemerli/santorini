package it.polimi.ingsw.PSP1.santorini.powers;

import it.polimi.ingsw.PSP1.santorini.Game;
import it.polimi.ingsw.PSP1.santorini.map.SquareData;
import it.polimi.ingsw.PSP1.santorini.map.Worker;
import it.polimi.ingsw.PSP1.santorini.player.Player;
import it.polimi.ingsw.PSP1.santorini.player.turn.EndTurn;
import it.polimi.ingsw.PSP1.santorini.player.turn.TurnState;

import java.awt.*;

public class Atlas extends Mortal {

    private boolean abilityToggled;

    public Atlas(Player player) {
        super(player);
    }

    @Override
    public void onBeginTurn(Game game) {
        super.onBeginTurn(game);
        abilityToggled = false;
    }

    @Override
    public boolean shouldShowInteraction() {
        return true;
    }

    @Override
    public void onToggleInteraction(Game game) {
        abilityToggled = !abilityToggled;
    }

    @Override
    public TurnState onYourBuild(Worker worker, Point where, Game game) {
        SquareData squareData = game.getGameMap().getSquareDataAt(where);
        boolean shouldBuildDome = squareData.getLevel() == 3;

        game.buildBlock(where, shouldBuildDome || abilityToggled);

        return new EndTurn(player, game);
    }
}
