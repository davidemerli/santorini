package it.polimi.ingsw.PSP1.santorini.player.turn;

import it.polimi.ingsw.PSP1.santorini.Game;
import it.polimi.ingsw.PSP1.santorini.map.Worker;
import it.polimi.ingsw.PSP1.santorini.player.Player;

import java.util.List;
import java.awt.*;

public abstract class TurnState {

    private Player player;
    private Game game;
    private TurnState previousTurn;

    public TurnState(Player player, Game game, TurnState previousTurn) {
        this.player = player;
        this.game = game;
        this.previousTurn = previousTurn;
    }

    public abstract void selectSquare(Point position);

    public abstract void selectWorker(Worker worker);

    public abstract void toggleInteraction();

    public abstract boolean shouldShowInteraction();

    public abstract void endState();

    public void undo() {
        if (previousTurn == null) {
            throw new IllegalStateException("There is no previous move to return to");
        }

        player.setTurnState(previousTurn);
    }
}
