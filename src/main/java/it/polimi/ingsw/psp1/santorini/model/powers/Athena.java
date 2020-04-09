package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.turn.Move;
import it.polimi.ingsw.psp1.santorini.model.turn.TurnState;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class Athena extends Mortal {

    public Athena(Player player) {
        super(player);
    }

    private boolean hasMovedUpwards;

    /**
     * {@inheritDoc}
     * <p>
     * Resets the moving upwards status
     */
    @Override
    public void onBeginTurn(Game game) {
        super.onBeginTurn(game);

        hasMovedUpwards = false;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Checks if the new level is higher than previous level, if so blocks other players moves
     */
    @Override
    public void onYourMove(Worker worker, Point where, Game game) {
        int oldLevel = game.getMap().getLevel(worker.getPosition());
        int newLevel = game.getMap().getLevel(where);

        if(newLevel > oldLevel) {
            hasMovedUpwards = true;
        }

        super.onYourMove(worker, where, game);
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the condition of moving upwards is true, blocks the moves for other players that would
     * have made them go up
     */
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
