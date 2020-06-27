package it.polimi.ingsw.psp1.santorini.model.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;

import java.util.ArrayList;
import java.util.List;

public class SelectPowers extends TurnState {

    private final List<Power> selectedPowers = new ArrayList<>();

    @Override
    public void init(Game game) {
        super.init(game);

        game.askRequest(game.getCurrentPlayer(), EnumRequestType.CHOOSE_POWERS);
        game.notifyObservers(o -> o.sendPowerList(game.getAvailablePowers(), game.getPlayerNumber()));
    }

    @Override
    public void selectGod(Game game, Player player, Power power) {
        if (selectedPowers.contains(power)) {
            throw new UnsupportedOperationException("Same player already chosen");
        }

        if (!game.getAvailablePowers().contains(power)) {
            throw new IllegalArgumentException("Given power is not playable in this game");
        }

        selectedPowers.add(power);

        if (selectedPowers.size() == game.getPlayerNumber()) {
            //TODO: maybe don't expose the REP
            game.getAvailablePowers().clear();
            game.getAvailablePowers().addAll(selectedPowers);

            game.shiftPlayers(-1);
            game.setTurnState(new ChoosePlayerPower());
        }
    }

    @Override
    public void undo(Game game, Player player) {
        throw new UnsupportedOperationException("Cannot undo in power selection");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldShowInteraction(Game game, Player player) {
        return false;
    }
}
