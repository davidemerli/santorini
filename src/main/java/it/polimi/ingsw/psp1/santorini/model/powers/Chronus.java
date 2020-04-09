package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.turn.Move;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;

public class Chronus extends Mortal {

    public Chronus(Player player) {
        super(player);
    }

    @Override
    public void onBeginTurn(Game game) {
        if (customWinCondition(game)) {
            player.setWinner(true);
            player.setTurnState(new Move(player, game));
        } else {
            super.onBeginTurn(game);
        }
    }

    private boolean customWinCondition(Game game) {
        return game.getMap().getAllSquares().stream()
                .filter(p -> game.getMap().getLevel(p) == 4)
                .count() >= 5;
    }
}