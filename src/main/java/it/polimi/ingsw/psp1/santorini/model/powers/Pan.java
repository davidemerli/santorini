package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.controller.turn.TurnState;

import java.awt.*;

public class Pan extends Mortal {

    public Pan(Player player) {
        super(player);
    }

    @Override
    public void onYourMove(Worker worker, Point where, Game game) {
        int oldLevel = game.getMap().getLevel(worker.getPosition());
        int newLevel = game.getMap().getLevel(where);

        if (oldLevel - newLevel >= 2) {
            player.setWinner(true);
            //TODO put directly on endturn if wincondition satisfied?
        }

        super.onYourMove(worker, where, game);
    }
}
