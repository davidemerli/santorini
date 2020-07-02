package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.turn.Move;

/**
 * Power: Chronus wins when there are at least five complete tower on the board
 */
public class Chronus extends Mortal {

    /**
     * {@inheritDoc}
     * <p>
     * Checks if the winning condition is true
     */
    @Override
    public void onBuild(Player player, Worker worker, Point where, Game game) {
        super.onBuild(player, worker, where, game);

        if (customWinCondition(game)) {
            this.player.setWinner(true);
        }
    }

    /**
     * Winning condition to be checked at the beginning of the turn
     *
     * @param game current game
     * @return true if the custom winning condition is valid
     */
    private boolean customWinCondition(Game game) {
        return game.getMap().getAllSquares().stream()
                .filter(p -> game.getMap().getLevel(p) == 4)
                .count() >= 5;
    }
}