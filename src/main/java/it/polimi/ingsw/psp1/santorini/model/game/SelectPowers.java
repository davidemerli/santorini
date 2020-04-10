package it.polimi.ingsw.psp1.santorini.model.game;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectPowers extends PreGameState {

    private List<Power> selectedPowers = new ArrayList<>();

    @Override
    public void selectGod(Game game, Player player, Power power) {
        if(!selectedPowers.contains(power)) {
            selectedPowers.add(power);
        }

        if (selectedPowers.size() == game.getPlayerList().size()) {
            game.getAvailableGodList().addAll(selectedPowers);

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
