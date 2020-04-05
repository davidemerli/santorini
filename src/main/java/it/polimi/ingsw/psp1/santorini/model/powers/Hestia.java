package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.controller.turn.Build;
import it.polimi.ingsw.psp1.santorini.controller.turn.EndTurn;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class Hestia extends Mortal {

    private boolean hasBuilt;

    public Hestia(Player player) {
        super(player);
    }

    @Override
    public void onBeginTurn(Game game) {
        super.onBeginTurn(game);
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
        super.onYourBuild(worker, where, game);

        if (!hasBuilt) {
            player.setTurnState(new Build(player, game));
            hasBuilt = true;
        }
    }

    @Override
    public List<Point> getValidMoves(Game game) {
        List<Point> list = super.getValidMoves(game);

        if (player.getTurnState() instanceof Build && hasBuilt) {
            return list.stream()
                    .filter(point -> !game.getMap().isPerimeter(point))
                    .collect(Collectors.toList());
        }

        return list;
    }
}
