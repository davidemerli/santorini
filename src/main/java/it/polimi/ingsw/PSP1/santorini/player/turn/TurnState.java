package it.polimi.ingsw.PSP1.santorini.player.turn;

import it.polimi.ingsw.PSP1.santorini.Game;
import it.polimi.ingsw.PSP1.santorini.map.Map;
import it.polimi.ingsw.PSP1.santorini.map.Worker;
import it.polimi.ingsw.PSP1.santorini.player.Player;

import java.awt.*;
import java.util.List;

public abstract class TurnState {

    protected Player player;
    protected Game game;
    protected TurnState previousTurn;
    protected Map previousMap;

    public TurnState(Player player, Game game) {
        this.player = player;
        this.game = game;
        this.previousTurn = player.getTurnState();
        this.previousMap = game.getGameMap();
    }

    public abstract void selectSquare(Point position);

    public abstract void selectWorker(Worker worker);

    public abstract void toggleInteraction();

    public abstract boolean shouldShowInteraction();

    public abstract List<Point> getBlockedMoves();

    public abstract List<Point> getValidMoves();

    public void undo() {
        if (previousTurn == null) {
            throw new IllegalStateException("There is no previous move to return to");
        }

        game.setGameMap(previousMap);
        player.setTurnState(previousTurn);
    }
}
