package it.polimi.ingsw.psp1.santorini.model.game;

import it.polimi.ingsw.psp1.santorini.model.powers.Power;

public interface GameState {

    void selectGod(Power power);
    void undo();

}
