package it.polimi.ingsw.psp1.santorini.model;

import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Player {

    private final String name;
    private final List<Worker> workerList;
    private Power power;
    private Worker selectedWorker;
    private boolean isWorkerLocked;
    private boolean hasWon;
    private boolean hasLost;

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

    public void removeAllWorkers() {
        this.workerList.clear();
    }

    public List<Worker> getWorkers() {
        return Collections.unmodifiableList(workerList);
    }

    public Power getPower() {
        return power;
    }

    public void setPower(Power power) {
        this.power = power;
        this.power.setPlayer(this);
    }

    public Optional<Worker> getSelectedWorker() {
        return Optional.ofNullable(selectedWorker);
    }

    public void setSelectedWorker(Worker selectedWorker) {
        this.selectedWorker = selectedWorker;
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

    public void setLoser(boolean hasLost) {
        this.hasLost = hasLost;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }

        return this.getName().equals(((Player) obj).getName());
    }
}
