package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;

import java.util.List;

public class Zeus extends Mortal {

    @Override
    public List<Point> getValidMoves(Worker worker, Game game) {
        List<Point> list = super.getValidMoves(worker, game);

        if (game.getTurnState() instanceof Build) {
            int level = game.getMap().getLevel(worker.getPosition());

            if (level < 3) {
                list.add(worker.getPosition());
            }
        }

        return list;
    }
}
