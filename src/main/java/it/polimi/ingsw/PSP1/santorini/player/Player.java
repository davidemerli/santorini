package it.polimi.ingsw.PSP1.santorini.player;

import it.polimi.ingsw.PSP1.santorini.player.game.GameState;
import it.polimi.ingsw.PSP1.santorini.player.turn.TurnState;

public class Player {

    private GameState gameState;
    private TurnState turnState;

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public TurnState getTurnState() {
        return turnState;
    }

    public void setTurnState(TurnState turnState) {
        this.turnState = turnState;
    }
}
