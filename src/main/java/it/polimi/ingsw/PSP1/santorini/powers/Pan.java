package it.polimi.ingsw.PSP1.santorini.powers;

import it.polimi.ingsw.PSP1.santorini.Game;
import it.polimi.ingsw.PSP1.santorini.map.Worker;
import it.polimi.ingsw.PSP1.santorini.player.Player;
import it.polimi.ingsw.PSP1.santorini.player.turn.TurnState;

import java.awt.*;

public class Pan extends Mortal {


    public Pan(Player player) {
        super(player);
    }

    @Override
    public TurnState onYourMove(Worker worker, Point where, Game game) {
        TurnState next = super.onYourMove(worker, where, game);
        int oldLevel = game.getGameMap().getSquareDataAt(worker.getPosition()).getLevel();
        int newLevel = game.getGameMap().getSquareDataAt(where).getLevel();

        if (oldLevel - newLevel >= 2) {
            player.setWinner();
        }
        return next;
    }
}
