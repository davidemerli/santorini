package it.polimi.ingsw.psp1.santorini.powers;

import it.polimi.ingsw.psp1.santorini.Game;
import it.polimi.ingsw.psp1.santorini.map.Worker;
import it.polimi.ingsw.psp1.santorini.player.Player;
import it.polimi.ingsw.psp1.santorini.player.turn.Build;
import it.polimi.ingsw.psp1.santorini.player.turn.Move;
import it.polimi.ingsw.psp1.santorini.player.turn.TurnState;

import java.awt.*;
import java.util.List;

public class Triton extends Mortal {

    private boolean abilityToggled;
    private boolean firstMove;

    public Triton(Player player) {
        super(player);
    }

    @Override
    public void onBeginTurn(Game game) {
        firstMove = false;
    }

    @Override
    public boolean shouldShowInteraction() {
        return firstMove;
    }

    @Override
    public void onToggleInteraction(Game game) {
        player.setTurnState(new Build(player, game));
    }

    @Override
    public TurnState onYourMove(Worker worker, Point where, Game game) {
        TurnState next = super.onYourMove(worker, where, game);

        if (game.getGameMap().isPerimeter(where)) {
            if (!firstMove) {
                firstMove = true;
            }
            return new Move(player, game);
        }

        return next;
    }
}
