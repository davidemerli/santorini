package it.polimi.ingsw.psp1.santorini.model.game;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;

public class ChoosePlayerPower extends PreGameState {

    @Override
    public void selectGod(Game game, Player player, Power power) {
        if(!game.getAvailableGodList().contains(power)) {
            throw new IllegalArgumentException("Invalid power selected");
        }

        game.getAvailableGodList().remove(power);

        player.setPower(power);
        player.setGameState(new Wait());
    }

}
