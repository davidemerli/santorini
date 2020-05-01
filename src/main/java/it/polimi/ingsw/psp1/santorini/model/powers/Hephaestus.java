package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;

import java.util.Collections;
import java.util.List;

public class Hephaestus extends Mortal {

    private boolean hasBuilt;
    private Point oldBuild;

    @Override
    public void onBeginTurn(Player player, Game game) {
        super.onBeginTurn(player, game);

        if (player.equals(this.player)) {
            oldBuild = null;
            hasBuilt = false;
        }
    }

    @Override
    public boolean shouldShowInteraction(Game game) {
        return hasBuilt;
    }

    @Override
    public void onToggleInteraction(Game game) {
        game.endTurn();
    }

    @Override
    public void onBuild(Player player, Worker worker, Point where, Game game) {
        if (player.equals(this.player)) {
            boolean shouldBuildDome = game.getMap().getLevel(where) == 3;
            game.buildBlock(where, shouldBuildDome);

            if (!hasBuilt && game.getMap().getLevel(where) < 3) {
                oldBuild = new Point(where);
                hasBuilt = true;
                game.setTurnState(new Build(game));
            } else {
                game.endTurn();
            }
        } else {
            super.onBuild(player, worker, where, game);
        }
    }

    @Override
    public List<Point> getValidMoves(Worker worker, Game game) {
        if (game.getTurnState() instanceof Build && worker != null && hasBuilt) {
            return Collections.singletonList(oldBuild);
        }

        return super.getValidMoves(worker, game);
    }
}
