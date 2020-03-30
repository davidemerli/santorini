package it.polimi.ingsw.PSP1.santorini.powers;

import it.polimi.ingsw.PSP1.santorini.Game;
import it.polimi.ingsw.PSP1.santorini.map.Worker;
import it.polimi.ingsw.PSP1.santorini.player.Player;
import it.polimi.ingsw.PSP1.santorini.player.turn.Build;
import it.polimi.ingsw.PSP1.santorini.player.turn.Move;
import it.polimi.ingsw.PSP1.santorini.player.turn.TurnState;

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
    public TurnState onYourBuild(Worker worker, Point where, Game game) {
        TurnState next = super.onYourBuild(worker, where, game);

        if (firstBuild) {
            player.lockWorker();
            firstBuild = false;
            hasBuiltBeforeMoving = true;
            return new Move(player, game);
        }

        return next;
    }

    @Override
    public List<Point> getValidMoves(Game game) {
        List<Point> list = super.getValidMoves(game);

        if (player.getTurnState() instanceof Move && hasBuiltBeforeMoving) {
            int level = game.getGameMap().getSquareDataAt(player.getSelectedWorker().getPosition()).getLevel();

            return list.stream()
                    .filter(p -> game.getGameMap().getSquareDataAt(p).getLevel() <= level)
                    .collect(Collectors.toList());
        }

        return list;
    }
}
