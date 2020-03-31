package it.polimi.ingsw.psp1.santorini.powers;

import it.polimi.ingsw.psp1.santorini.Game;
import it.polimi.ingsw.psp1.santorini.map.Worker;
import it.polimi.ingsw.psp1.santorini.player.Player;
import it.polimi.ingsw.psp1.santorini.player.turn.Move;
import it.polimi.ingsw.psp1.santorini.player.turn.TurnState;

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
    public TurnState onYourMove(Worker worker, Point where, Game game) {
        int oldLevel = getLevelAtPosition(game, worker.getPosition());
        int newLevel = getLevelAtPosition(game, where);

        if(newLevel > oldLevel) {
            hasMovedUpwards = true;
        }

        return super.onYourMove(worker, where, game);
    }

    @Override
    public List<Point> getBlockedMoves(Worker worker, List<Point> validMoves, TurnState playerState, Game game) {
        if(playerState instanceof Move && hasMovedUpwards) {
            int workerLevel = getLevelAtPosition(game, worker.getPosition());

            return validMoves.stream()
                    .filter(p -> getLevelAtPosition(game, p) > workerLevel)
                    .collect(Collectors.toList());
        }

        return super.getBlockedMoves(worker, validMoves, playerState, game);
    }
}
