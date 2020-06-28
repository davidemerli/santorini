package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
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
    public void onBeginTurn(Player player, Game game) {
        if (player.equals(this.player) && customWinCondition(game)) {
            player.setWinner(true);
            game.setTurnState(new Move());
        } else {
            super.onBeginTurn(player, game);
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