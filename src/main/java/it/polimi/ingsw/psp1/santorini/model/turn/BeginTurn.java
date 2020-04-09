package it.polimi.ingsw.psp1.santorini.model.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;

public class BeginTurn extends TurnState {

    public BeginTurn(Player player, Game game) {
        super(player, game);

        this.previousTurn = null;

        player.getPower().onBeginTurn(game);
        game.getPlayerOpponents(player).forEach(p -> p.getPower().onEnemyBeginTurn(game, player));
    }

    @Override
    public boolean shouldShowInteraction() {
        return false;
    }
}
