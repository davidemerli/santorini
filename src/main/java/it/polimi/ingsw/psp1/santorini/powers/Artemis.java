package it.polimi.ingsw.psp1.santorini.powers;

import it.polimi.ingsw.psp1.santorini.Game;
import it.polimi.ingsw.psp1.santorini.map.Worker;
import it.polimi.ingsw.psp1.santorini.player.Player;
import it.polimi.ingsw.psp1.santorini.player.turn.Move;
import it.polimi.ingsw.psp1.santorini.player.turn.TurnState;

import java.awt.*;
import java.util.List;

public class Artemis extends Mortal {
    
    private boolean abilityToggled;
    private boolean firstMove;
    private Point oldPosition;

    public Artemis(Player player) {
        super(player);
    }

    @Override
    public void onBeginTurn(Game game) {
        abilityToggled = true;
        firstMove = true;
    }

    @Override
    public boolean shouldShowInteraction() {
        return firstMove;
    }

    @Override
    public void onToggleInteraction(Game game) {
        abilityToggled = !abilityToggled;
    }

    @Override
    public List<Point> getValidMoves(Game game) {
        List<Point> list = super.getValidMoves(game);

        if (player.getTurnState() instanceof Move && !firstMove) {
            list.remove(oldPosition);
        }

        return list;
    }

    @Override
    public TurnState onYourMove(Worker worker, Point where, Game game) {
        TurnState next = super.onYourMove(worker, where, game);

        if (abilityToggled && firstMove) {
            oldPosition = new Point(where);
            firstMove = false;

            return new Move(player, game);
        }

        return next;
    }
}
