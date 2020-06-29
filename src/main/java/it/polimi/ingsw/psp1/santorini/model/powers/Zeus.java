package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;

import java.util.List;

/**
 * Power: Zeus can build under himself
 */
public class Zeus extends Mortal {

    /**
     * {@inheritDoc}
     * <p>
     * Adds one valid move, in fact the worker can build a block (not a dome) under himself
     */
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
