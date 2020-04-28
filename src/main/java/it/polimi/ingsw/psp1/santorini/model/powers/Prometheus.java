package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import it.polimi.ingsw.psp1.santorini.model.turn.Move;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class Prometheus extends Mortal {

    private boolean firstBuild;
    private boolean hasBuiltBeforeMoving;

    @Override
    public void onBeginTurn(Player player, Game game) {
        super.onBeginTurn(player, game);

        if (player.equals(this.player)) {
            firstBuild = true;
            hasBuiltBeforeMoving = false;
            game.setTurnState(new Build(game));
        }
    }

    @Override
    public boolean shouldShowInteraction(Game game) {
        return firstBuild;
    }

    @Override
    public void onToggleInteraction(Game game) {
        firstBuild = false;
        game.setTurnState(new Move(game));
    }

    @Override
    public void onBuild(Player player, Worker worker, Point where, Game game) {
        if (player.equals(this.player)) {
            boolean shouldBuildDome = game.getMap().getLevel(where) == 3;
            game.getMap().buildBlock(where, shouldBuildDome);

            if (firstBuild) {
                player.lockWorker();

                firstBuild = false;
                hasBuiltBeforeMoving = true;
                game.setTurnState(new Move(game));
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

        if (game.getTurnState() instanceof Move && worker != null && hasBuiltBeforeMoving) {
            int level = game.getMap().getLevel(worker.getPosition());

            return list.stream()
                    .filter(p -> game.getMap().getLevel(p) <= level)
                    .collect(Collectors.toList());
        }

        return list;
    }
}
