package it.polimi.ingsw.psp1.santorini.model.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;

public class EndTurn extends TurnState {

    public EndTurn(Game game) {
        super(game);

        game.getPlayerList().forEach(p -> p.getPower().onEndTurn(p, game));

        resetWorkerState();
    }

    private void resetWorkerState() {
        game.getCurrentPlayer().setSelectedWorker(null);
        game.getCurrentPlayer().unlockWorker();
    }

    @Override
    public boolean shouldShowInteraction(Player player) {
        return false;
    }
}
