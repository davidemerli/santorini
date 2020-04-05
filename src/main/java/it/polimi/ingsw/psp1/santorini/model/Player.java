package it.polimi.ingsw.psp1.santorini.model;

import it.polimi.ingsw.psp1.santorini.controller.turn.BeginTurn;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.controller.game.GameState;
import it.polimi.ingsw.psp1.santorini.controller.turn.TurnState;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {

    private String name;

    private GameState gameState;
    private TurnState turnState;
    private Power power;

    private Worker selectedWorker;
    private boolean isWorkerLocked;
    private boolean hasWon;
    private boolean hasLost;

    private List<Worker> workerList;

    public Player(String name) {
        this.name = name;
        this.workerList = new ArrayList<>();
    }

    public void addWorker(Worker worker) {
        if (workerList.contains(worker)) {
            throw new IllegalArgumentException("Worker already present");
        }

        if (workerList.size() >= 2) {
            throw new UnsupportedOperationException("Cannot add more than 2 workers per player");
        }

        workerList.add(worker);
    }

    public void removeWorker(Worker worker) {
        if (!workerList.contains(worker)) {
            throw new IllegalArgumentException("Worker is not present");
        }

        workerList.remove(worker);
    }

    public void newTurn(Game game) {
        new BeginTurn(this, game);
    }

    public List<Worker> getWorkers() {
        return Collections.unmodifiableList(workerList);
    }

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

    public void unlockWorker() {
        isWorkerLocked = false;
    }

    public boolean hasWon() {
        return hasWon;
    }

    public void setWinner(boolean hasWon) {
        this.hasWon = hasWon;
    }

    public boolean hasLost() {
        return hasLost;
    }

    public void setLost(boolean hasLost) {
        this.hasLost = hasLost;
    }
}
