package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;

import java.util.List;
import java.util.stream.Collectors;

public class Hestia extends Mortal {

    private boolean hasBuilt;

    @Override
    public void onBeginTurn(Player player, Game game) {
        super.onBeginTurn(player, game);

        if (player.equals(this.player)) {
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

            if (!hasBuilt) {
                hasBuilt = true;

                game.setTurnState(new Build());
            } else {
                game.endTurn();
            }
        } else {
            super.onBuild(player, worker, where, game);
        }
    }

    @Override
    public List<Point> getValidMoves(Worker worker, Game game) {
        List<Point> list = super.getValidMoves(worker, game);

        if (game.getTurnState() instanceof Build && worker != null && hasBuilt) {
            return list.stream()
                    .filter(point -> !game.getMap().isPerimeter(point))
                    .collect(Collectors.toList());
        }

        return list;
    }
}
