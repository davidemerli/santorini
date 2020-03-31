package it.polimi.ingsw.psp1.santorini.player.turn;

import it.polimi.ingsw.psp1.santorini.Game;
import it.polimi.ingsw.psp1.santorini.map.Worker;
import it.polimi.ingsw.psp1.santorini.player.Player;

import java.awt.*;
import java.util.List;

public class BeginTurn extends TurnState {

    public BeginTurn(Player player, Game game) {
        super(player, game);

        this.previousTurn = null;

        player.getPower().onBeginTurn(game);
    }

    @Override
    public void selectSquare(Point position) {

    }

    @Override
    public void selectWorker(Worker worker) {

    }

    @Override
    public void toggleInteraction() {

    }

    @Override
    public boolean shouldShowInteraction() {
        return false;
    }

    @Override
    public List<Point> getBlockedMoves() {
        return null;
    }

    @Override
    public List<Point> getValidMoves() {
        return null;
    }
}
