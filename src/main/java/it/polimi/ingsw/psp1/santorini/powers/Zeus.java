package it.polimi.ingsw.psp1.santorini.powers;

import it.polimi.ingsw.psp1.santorini.Game;
import it.polimi.ingsw.psp1.santorini.map.Worker;
import it.polimi.ingsw.psp1.santorini.player.Player;
import it.polimi.ingsw.psp1.santorini.player.turn.Build;
import it.polimi.ingsw.psp1.santorini.player.turn.TurnState;

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
            int level = game.getGameMap().getSquareDataAt(player.getSelectedWorker().getPosition()).getLevel();
            if (level < 3) {
                list.add(player.getSelectedWorker().getPosition());
            }
        }

        return list;
    }

    @Override
    public TurnState onYourBuild(Worker worker, Point where, Game game) {
        TurnState next = super.onYourBuild(worker, where, game);

        if (game.getGameMap().isWorkerOn(where)) {
            game.getGameMap().removeWorker(worker);
            game.getGameMap().buildBlock(where, false);
            game.getGameMap().addWorker(worker);
        }

        return next;
    }
}
