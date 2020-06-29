package it.polimi.ingsw.psp1.santorini.model;

import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.turn.TurnState;

import java.util.List;
import java.util.Map;

public class GameState {
    private final GameMap previousMap;
    private final TurnState previousTurnState;
    private final List<Player> playersCopy;

    /**
     * Generic constructor
     *
     * @param previousMap       previous map
     * @param previousTurnState previous turn state
     * @param playersCopy       a copy of all players
     */
    public GameState(GameMap previousMap, TurnState previousTurnState,
                     List<Player> playersCopy) {
        this.previousMap = previousMap;
        this.previousTurnState = previousTurnState;
        this.playersCopy = playersCopy;
    }

    public GameMap getPreviousMap() {
        return previousMap;
    }

    public TurnState getPreviousTurnState() {
        return previousTurnState;
    }

    public List<Player> getPreviousPlayersState() {
        return playersCopy;
    }
}