package it.polimi.ingsw.psp1.santorini.model;

import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.turn.TurnState;

import java.util.Map;

public class GameState {
    private final GameMap previousMap;
    private final TurnState previousTurnState;
    private final Map<Player, Map<Worker, Point>> playerWorkerState;

    public GameState(GameMap previousMap, TurnState previousTurnState,
                     Map<Player, Map<Worker, Point>> playerWorkerState) {
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

    public Map<Player, Map<Worker, Point>> getPlayerWorkerState() {
        return playerWorkerState;
    }
}