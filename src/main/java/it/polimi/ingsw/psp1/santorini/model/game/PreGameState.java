package it.polimi.ingsw.psp1.santorini.model.game;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;

public abstract class PreGameState {

    public void selectGod(Game game, Player player, Power power) {
        throw new UnsupportedOperationException("Cannot undo in this state");
    }

    public void undo() {
        throw new UnsupportedOperationException("Cannot undo in this state");
    }
}
