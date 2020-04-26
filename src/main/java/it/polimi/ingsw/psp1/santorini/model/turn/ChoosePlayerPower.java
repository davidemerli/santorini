package it.polimi.ingsw.psp1.santorini.model.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;

public class ChoosePlayerPower extends TurnState {

    public ChoosePlayerPower(Game game) {
        super(game);
    }

    @Override
    public void init() {
        game.askRequest(game.getCurrentPlayer(), EnumRequestType.SELECT_POWER);
        game.notifyObservers(o -> o.sendPowerList(game.getCurrentPlayer(), game.getAvailablePowers()));
    }

    @Override
    public void selectGod(Game game, Player player, Power power) {
        if (!game.getAvailablePowers().contains(power)) {
            throw new IllegalArgumentException("Invalid power selected");
        }

        game.getAvailablePowers().remove(power);
        //TODO: maybe setPower in game? (for observer call)
        player.setPower(power);
        game.notifyObservers(o -> o.playerUpdate(game, player));

        game.shiftPlayers(-1);

        if (game.getAvailablePowers().size() == 1) {
            //TODO: maybe setPower in game? (for observer call)
            game.getCurrentPlayer().setPower(game.getAvailablePowers().get(0));
            game.notifyObservers(o -> o.playerUpdate(game, game.getCurrentPlayer()));

            game.getAvailablePowers().clear();
            game.setTurnState(new SelectStartingPlayer(game));
        } else {
            game.setTurnState(new ChoosePlayerPower(game));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldShowInteraction(Player player) {
        return false;
    }
}
