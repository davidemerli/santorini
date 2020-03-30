package it.polimi.ingsw.PSP1.santorini.powers;

import it.polimi.ingsw.PSP1.santorini.Game;
import it.polimi.ingsw.PSP1.santorini.map.Worker;
import it.polimi.ingsw.PSP1.santorini.player.Player;
import it.polimi.ingsw.PSP1.santorini.player.turn.Build;
import it.polimi.ingsw.PSP1.santorini.player.turn.EndTurn;
import it.polimi.ingsw.PSP1.santorini.player.turn.TurnState;

import java.awt.*;
import java.util.List;

public class Demeter extends Mortal {
    boolean hasBuilt;
    Point oldBuild;

    public Demeter(Player player) {
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
    public TurnState onYourBuild(Worker worker, Point where, Game game) {
        TurnState next = super.onYourBuild(worker, where, game);
        if (!hasBuilt) {
            oldBuild = new Point(where);
            return new Build(player, game);
        } else {
            return next;
        }
    }

    @Override
    public List<Point> getValidMoves(Game game) {
        List<Point> list = super.getValidMoves(game);
        if (player.getTurnState() instanceof Build && hasBuilt) {
            list.remove(oldBuild);
        }
        return list;
    }
}
