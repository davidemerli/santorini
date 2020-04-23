package it.polimi.ingsw.psp1.santorini.model.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;

import java.util.ArrayList;
import java.util.List;

public class SelectPowers extends TurnState {

    private final List<Power> selectedPowers = new ArrayList<>();

    public SelectPowers(Game game) {
        super(game);

        game.askRequest(game.getCurrentPlayer(), EnumRequestType.SELECT_POWER);
        game.notifyObservers(o -> o.sendPowerList(game.getCurrentPlayer(), game.getAvailablePowers()));
    }

    @Override
    public void selectGod(Game game, Player player, Power power) {
        if(!selectedPowers.contains(power) && !game.getAvailablePowers().contains(power)) {
            selectedPowers.add(power);
            //TODO: throw error if more times same power
        }

        if (selectedPowers.size() == game.getPlayerNumber()) {
            //TODO: maybe don't expose the REP
            game.getAvailablePowers().clear();
            game.getAvailablePowers().addAll(selectedPowers);

            game.shiftPlayers(-1);
            game.setTurnState(new ChoosePlayerPower(game));
        } else {
            game.askRequest(game.getCurrentPlayer(), EnumRequestType.SELECT_POWER);
        }
    }

    @Override
    public void undo(Player player) {
        if(selectedPowers.size() == 0) {
            throw new UnsupportedOperationException("Cannot undo, no gods selected");
        }
        //TODO: undo
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldShowInteraction(Player player) {
        return false;
    }
}
