package it.polimi.ingsw.PSP1.santorini.player.turn;

import it.polimi.ingsw.PSP1.santorini.Game;
import it.polimi.ingsw.PSP1.santorini.map.Worker;
import it.polimi.ingsw.PSP1.santorini.player.Player;

import java.awt.*;

public class Move extends TurnState {

    public Move(Player player, Game game, TurnState previousTurn) {
        super(player, game, previousTurn);
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
    public void endState() {

    }
}
