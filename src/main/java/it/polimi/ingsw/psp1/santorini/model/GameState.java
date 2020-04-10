package it.polimi.ingsw.psp1.santorini.model;

import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.turn.TurnState;

import java.awt.*;
import java.util.HashMap;

public class GameState {
    private final GameMap previousMap;
    private final TurnState previousTurnState;
    private final HashMap<Player, HashMap<Worker, Point>> playerWorkerState;

    public GameState(GameMap previousMap, TurnState previousTurnState,
                      HashMap<Player, HashMap<Worker, Point>> playerWorkerState) {
        this.previousMap = previousMap;
        this.previousTurnState = previousTurnState;
        this.playerWorkerState = playerWorkerState;
    }

    public GameMap getPreviousMap() {
        return previousMap;
    }

    public TurnState getPreviousTurnState() {
        return previousTurnState;
    }

    public HashMap<Player, HashMap<Worker, Point>> getPlayerWorkerState() {
        return playerWorkerState;
    }
}