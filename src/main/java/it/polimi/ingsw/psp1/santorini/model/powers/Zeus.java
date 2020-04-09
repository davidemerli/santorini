package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;

import java.awt.*;
import java.util.List;

public class Zeus extends Mortal {

    public Zeus(Player player) {
        super(player);
    }

    @Override
    public List<Point> getValidMoves(Game game) {
        List<Point> list = super.getValidMoves(game);

        if (player.getTurnState() instanceof Build) {
            int level = game.getMap().getLevel(player.getSelectedWorker().getPosition());

            if (level < 3) {
                list.add(player.getSelectedWorker().getPosition());
            }
        }

        return list;
    }
}
