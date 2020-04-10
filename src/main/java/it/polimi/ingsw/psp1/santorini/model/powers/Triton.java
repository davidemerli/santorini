package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import it.polimi.ingsw.psp1.santorini.model.turn.Move;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;

import java.awt.*;

public class Triton extends Mortal {

    private boolean hasMoved;

    @Override
    public void onBeginTurn(Player player, Game game) {
        super.onBeginTurn(player, game);

        if (player.equals(this.player)) {
            hasMoved = false;
        }
    }

    @Override
    public boolean shouldShowInteraction(Game game) {
        return game.getTurnState() instanceof Move && hasMoved;
    }

    @Override
    public void onToggleInteraction(Game game) {
        game.setTurnState(new Build(game));
    }

    @Override
    public void onMove(Player player, Worker worker, Point where, Game game) {
        super.onMove(player, worker, where, game);

        if (player.equals(this.player) && game.getMap().isPerimeter(where)) {
            if (!hasMoved) {
                hasMoved = true;
            }

            game.setTurnState(new Move(game));
        }
    }
}
