package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.controller.turn.Move;
import it.polimi.ingsw.psp1.santorini.controller.turn.TurnState;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class Athena extends Mortal {

    public Athena(Player player) {
        super(player);
    }

    private boolean hasMovedUpwards;

    @Override
    public void onBeginTurn(Game game) {
        super.onBeginTurn(game);

        hasMovedUpwards = false;
    }

    @Override
    public void onYourMove(Worker worker, Point where, Game game) {
        int oldLevel = game.getMap().getLevel(worker.getPosition());
        int newLevel = game.getMap().getLevel(where);

        if(newLevel > oldLevel) {
            hasMovedUpwards = true;
        }

        super.onYourMove(worker, where, game);
    }

    @Override
    public List<Point> getBlockedMoves(Worker worker, TurnState playerState, Game game) {
        if(playerState instanceof Move && hasMovedUpwards) {
            int workerLevel = game.getMap().getLevel(worker.getPosition());

            return game.getMap().getAllSquares().stream()
                    .filter(p -> game.getMap().getLevel(p) > workerLevel)
                    .collect(Collectors.toUnmodifiableList());
        }

        return super.getBlockedMoves(worker, playerState, game);
    }
}
