package it.polimi.ingsw.psp1.santorini.model.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Defines the state where a player must choose the first player
 */
public class SelectStartingPlayer extends TurnState {

    @Override
    public void init(Game game) {
        super.init(game);

        game.askRequest(game.getCurrentPlayer(), EnumRequestType.SELECT_STARTING_PLAYER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void selectStartingPlayer(Game game, Player player, String chosenPlayerName) {
        Optional<Player> chosenPlayer = game.getPlayerList().stream()
                .filter(p -> p.getName().equalsIgnoreCase(chosenPlayerName))
                .findFirst();

        if (chosenPlayer.isEmpty()) {
            throw new NoSuchElementException("Given player is not in the game");
        }

        int playerIndex = game.getPlayerList().indexOf(chosenPlayer.get());

        game.shiftPlayers(-playerIndex);
        game.setTurnState(new WorkerPlacing());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldShowInteraction(Game game, Player player) {
        return false;
    }
}
