package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.turn.Move;

import java.util.List;

public class Chronus extends Mortal {

    @Override
    public void onBeginTurn(Player player, Game game) {
        if (player.equals(this.player) && customWinCondition(game)) {
            player.setWinner(true);
            game.setTurnState(new Move(game));
        } else {
            super.onBeginTurn(player, game);
        }
    }

    private boolean customWinCondition(Game game) {
        return game.getMap().getAllSquares().stream()
                .filter(p -> game.getMap().getLevel(p) == 4)
                .count() >= 5;
    }
}