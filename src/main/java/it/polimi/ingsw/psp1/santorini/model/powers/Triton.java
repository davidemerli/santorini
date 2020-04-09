package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import it.polimi.ingsw.psp1.santorini.model.turn.Move;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;

import java.awt.*;

public class Triton extends Mortal {

    private boolean firstMove;

    public Triton(Player player) {
        super(player);
    }

    @Override
    public void onBeginTurn(Game game) {
        super.onBeginTurn(game);
        firstMove = false;
    }

    @Override
    public boolean shouldShowInteraction() {
        return player.getTurnState() instanceof Move && firstMove;
    }

    @Override
    public void onToggleInteraction(Game game) {
        player.setTurnState(new Build(player, game));
    }

    @Override
    public void onYourMove(Worker worker, Point where, Game game) {
        super.onYourMove(worker, where, game);

        if (game.getMap().isPerimeter(where)) {
            if (!firstMove) {
                firstMove = true;
            }
            player.setTurnState(new Move(player, game));
        }
    }
}
