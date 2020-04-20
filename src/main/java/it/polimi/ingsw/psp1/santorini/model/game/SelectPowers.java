package it.polimi.ingsw.psp1.santorini.model.game;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;

import java.util.ArrayList;
import java.util.List;

public class SelectPowers extends PreGameState {

    private final List<Power> selectedPowers = new ArrayList<>();

    @Override
    public void selectGod(Game game, Power power) {
        if(!selectedPowers.contains(power)) {
            selectedPowers.add(power);
        }

        if (selectedPowers.size() == game.getPlayerList().size()) {
            game.getAvailablePowers().addAll(selectedPowers);

            player.setGameState(new Wait());
            game.getPlayerOpponents(player).forEach(p -> p.setGameState(new ChoosePlayerPower()));
        }
    }

    @Override
    public void undo() {
        if(selectedPowers.size() == 0) {
            throw new UnsupportedOperationException("Cannot undo, no gods selected");
        }

    }
}
