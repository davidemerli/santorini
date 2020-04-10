package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.Player;

import java.awt.*;

public class Pan extends Mortal {

    @Override
    public void onMove(Player player, Worker worker, Point where, Game game) {
        if(player.equals(this.player)) {
            int oldLevel = game.getMap().getLevel(worker.getPosition());
            int newLevel = game.getMap().getLevel(where);

            if (oldLevel - newLevel >= 2) {
                player.setWinner(true);
                //TODO put directly on endturn if wincondition satisfied?
            }
        }

        super.onMove(player, worker, where, game);
    }
}
