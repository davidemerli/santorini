package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.controller.turn.Build;
import it.polimi.ingsw.psp1.santorini.controller.turn.EndTurn;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class Hephaestus extends Mortal {

    private boolean hasBuilt;
    private Point oldBuild;

    public Hephaestus(Player player) {
        super(player);
    }

    @Override
    public void onBeginTurn(Game game) {
        super.onBeginTurn(game);
        oldBuild = null;
        hasBuilt = false;
    }

    @Override
    public boolean shouldShowInteraction() {
        return hasBuilt;
    }

    @Override
    public void onToggleInteraction(Game game) {
        player.setTurnState(new EndTurn(player, game));
    }

    @Override
    public void onYourBuild(Worker worker, Point where, Game game) {
        boolean shouldBuildDome = game.getMap().getLevel(where) == 3;
        game.getMap().buildBlock(where, shouldBuildDome);

        if (!hasBuilt && game.getMap().getLevel(where) < 3) {
            oldBuild = new Point(where);
            hasBuilt = true;
            player.setTurnState(new Build(player, game));
        } else  {
            player.setTurnState(new EndTurn(player, game));
        }
    }

    @Override
    public List<Point> getValidMoves(Game game) {
        if (player.getTurnState() instanceof Build && hasBuilt) {
            return Collections.singletonList(oldBuild);
        }

        return super.getValidMoves(game);
    }
}
