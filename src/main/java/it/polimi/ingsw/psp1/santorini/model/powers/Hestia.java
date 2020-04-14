package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import it.polimi.ingsw.psp1.santorini.model.turn.EndTurn;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;

import java.awt.*;
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
        game.setTurnState(new EndTurn(game));
    }

    @Override
    public void onBuild(Player player, Worker worker, Point where, Game game) {
        if (player.equals(this.player)) {
            boolean shouldBuildDome = game.getMap().getLevel(where) == 3;
            game.getMap().buildBlock(where, shouldBuildDome);

            if (!hasBuilt) {
                game.setTurnState(new Build(game));
                hasBuilt = true;
            } else {
                game.setTurnState(new EndTurn(game));
            }
        } else {
            super.onBuild(player, worker, where, game);
        }
    }

    @Override
    public List<Point> getValidMoves(Worker worker, Game game) {
        List<Point> list = super.getValidMoves(worker, game);

        if (game.getTurnState() instanceof Build && hasBuilt) {
            return list.stream()
                    .filter(point -> !game.getMap().isPerimeter(point))
                    .collect(Collectors.toList());
        }

        return list;
    }
}