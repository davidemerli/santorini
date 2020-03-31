package it.polimi.ingsw.psp1.santorini.player;

import it.polimi.ingsw.psp1.santorini.map.Worker;
import it.polimi.ingsw.psp1.santorini.player.game.GameState;
import it.polimi.ingsw.psp1.santorini.player.turn.TurnState;
import it.polimi.ingsw.psp1.santorini.powers.Power;

public class Player {

    private GameState gameState;
    private TurnState turnState;
    private Power power;

    private Worker selectedWorker;
    private boolean isWorkerLocked;
    private boolean hasWon;
    private boolean hasLost;

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

    public Power getPower() {
        return power;
    }

    public void setPower(Power power) {
        this.power = power;
    }

    public Worker getSelectedWorker() {
        return selectedWorker;
    }

    public void setSelectedWorker(Worker selectedWorker) {
        this.selectedWorker = selectedWorker;
    }

    public boolean isWorkerSelected() {
        return selectedWorker != null;
    }

    public boolean isWorkerLocked() {
        return isWorkerLocked;
    }

    public void lockWorker() {
        isWorkerLocked = true;
    }

    public boolean hasWon() {
        return hasWon;
    }

    public void setWinner() {
        this.hasWon = true;
    }

    public boolean hasLost() {
        return hasLost;
    }

    public void setLost() {
        this.hasLost = true;
    }
}
