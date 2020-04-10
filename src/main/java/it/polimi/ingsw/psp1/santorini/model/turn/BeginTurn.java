package it.polimi.ingsw.psp1.santorini.model.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;

public class BeginTurn extends TurnState {

    public BeginTurn(Game game) {
        super(game);
    }

    public void onStart() {
        game.getPlayerList().forEach(p -> p.getPower().onBeginTurn(game.getCurrentPlayer(), game));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldShowInteraction(Player player) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undo(Player player) {
        throw new UnsupportedOperationException("Cannot undo at the beginning of the turn");
    }
}
