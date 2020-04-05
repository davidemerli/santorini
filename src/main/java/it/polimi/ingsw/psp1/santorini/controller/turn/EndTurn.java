package it.polimi.ingsw.psp1.santorini.controller.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;

public class EndTurn extends TurnState {

    public EndTurn(Player player, Game game) {
        super(player, game);

        player.getPower().onEndTurn(game);
        game.getPlayerOpponents(player).forEach(p -> p.getPower().onEnemyEndTurn(game, player));

        player.setSelectedWorker(null);
        player.unlockWorker();
    }

    @Override
    public boolean shouldShowInteraction() {
        return false;
    }
}
