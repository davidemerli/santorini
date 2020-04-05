package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.controller.turn.EndTurn;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.controller.turn.Build;
import it.polimi.ingsw.psp1.santorini.controller.turn.Move;
import it.polimi.ingsw.psp1.santorini.controller.turn.TurnState;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class Prometheus extends Mortal {

    private boolean firstBuild;
    private boolean hasBuiltBeforeMoving;

    public Prometheus(Player player) {
        super(player);
    }

    @Override
    public void onBeginTurn(Game game) {
        //TODO: check if loose condition is to be called here
        firstBuild = true;
        player.setTurnState(new Build(player, game));
    }

    @Override
    public boolean shouldShowInteraction() {
        return firstBuild;
    }

    @Override
    public void onToggleInteraction(Game game) {
        firstBuild = false;
        player.setTurnState(new Move(player, game));
    }

    @Override
    public void onYourBuild(Worker worker, Point where, Game game) {
        boolean shouldBuildDome = game.getMap().getLevel(where) == 3;
        game.getMap().buildBlock(where, shouldBuildDome);

        if (firstBuild) {
            player.lockWorker();

            firstBuild = false;
            hasBuiltBeforeMoving = true;
            player.setTurnState(new Move(player, game));
        } else {
            player.setTurnState(new EndTurn(player, game));
        }
    }

    @Override
    public List<Point> getValidMoves(Game game) {
        List<Point> list = super.getValidMoves(game);

        if (player.getTurnState() instanceof Move && hasBuiltBeforeMoving) {
            int level = game.getMap().getLevel(player.getSelectedWorker().getPosition());

            return list.stream()
                    .filter(p -> game.getMap().getLevel(p) <= level)
                    .collect(Collectors.toList());
        }

        return list;
    }
}
