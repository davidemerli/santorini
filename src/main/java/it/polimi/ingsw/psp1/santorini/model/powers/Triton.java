package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import it.polimi.ingsw.psp1.santorini.model.turn.Move;

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
        game.setTurnState(new Build());
    }

    @Override
    public void onMove(Player player, Worker worker, Point where, Game game) {
        if (player.equals(this.player)) {
            int oldLevel = game.getMap().getLevel(worker.getPosition());
            int newLevel = game.getMap().getLevel(where);

            game.moveWorker(player, worker, where);

            if (newLevel == 3 && oldLevel == 2) {
                player.setWinner(true);
            }

            player.lockWorker();

            if (game.getMap().isPerimeter(where)) {
                if (!hasMoved) {
                    hasMoved = true;
                }

                game.setTurnState(new Move());
            } else {
                game.setTurnState(new Build());
            }
        }
    }
}
