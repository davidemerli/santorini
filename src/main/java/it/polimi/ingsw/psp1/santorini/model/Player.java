package it.polimi.ingsw.psp1.santorini.model;

import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Defines a generic player who can join or create a game
 */
public class Player implements Cloneable {

    private final String name;
    private List<Worker> workerList;
    private Power power;
    private Worker selectedWorker;
    private boolean isWorkerLocked;
    private boolean hasWon;
    private boolean hasLost;

    /**
     * Generic constructor using the name of the player
     * Creates a new worker list
     *
     * @param name of the player
     */
    public Player(String name) {
        this.name = name;
        this.workerList = new ArrayList<>();
    }

    /**
     * Adds worker in worker list
     *
     * @param worker to add
     * @throws IllegalArgumentException if worker is already present in the list
     * @throws UnsupportedOperationException if worker list is full
     */
    public void addWorker(Worker worker) {
        if (workerList.contains(worker)) {
            throw new IllegalArgumentException("Worker already present");
        }

        if (workerList.size() >= 2) {
            throw new UnsupportedOperationException("Cannot add more than 2 workers per player");
        }

        workerList.add(worker);
    }

    /**
     * Removes worker from worker list
     *
     * @param worker to remove
     * @throws IllegalArgumentException if worker is not present
     */
    public void removeWorker(Worker worker) {
        if (!workerList.contains(worker)) {
            throw new IllegalArgumentException("Worker is not present");
        }

        workerList.remove(worker);
    }

    /**
     * Removes all workers from list
     */
    public void removeAllWorkers() {
        this.workerList.clear();
    }

    public List<Worker> getWorkers() {
        return workerList.stream()
                .map(Worker::copy)
                .collect(Collectors.toUnmodifiableList());
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

    /**
     * True if worker is locked after moved
     *
     * @return true if worker is locked
     */
    public boolean isWorkerLocked() {
        return isWorkerLocked;
    }

    /**
     * Used to lock a worker in game
     */
    public void lockWorker() {
        isWorkerLocked = true;
    }

    /**
     * Used to unlock a worker in game
     */
    public void unlockWorker() {
        isWorkerLocked = false;
    }

    /**
     * Used to know if a player has won
     *
     * @return true if the player has won
     */
    public boolean hasWon() {
        return hasWon;
    }

    public void setWinner(boolean hasWon) {
        this.hasWon = hasWon;
    }

    /**
     * Used to know if a player has lost
     * @return true if the player has lost
     */
    public boolean hasLost() {
        return hasLost;
    }

    public void setLoser(boolean hasLost) {
        this.hasLost = hasLost;
    }

    public String getName() {
        return name;
    }

    /**
     * Copies worker list
     *
     * @return worker list copy
     */
    public Player copy() {
        try {
            Player clone = (Player) super.clone();
            clone.workerList = workerList.stream()
                    .map(Worker::copy)
                    .collect(Collectors.toList());

            clone.power = power == null ? null : power.copy();

            if(clone.power != null) {
                clone.power.setPlayer(clone);
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Used to move a worker into a new position on the map
     *
     * @param worker      current worker
     * @param newPosition new position in the board
     */
    public void moveWorker(Worker worker, Point newPosition) {
        Optional<Worker> optWorker = workerList.stream()
                .filter(w -> w.equals(worker)).findFirst();

        if (optWorker.isEmpty()) {
            throw new IllegalArgumentException("Player does not own worker");
        }

        optWorker.get().setPosition(newPosition);
        setSelectedWorker(optWorker.get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String workers = workerList.stream()
                .map(Worker::getPosition)
                .map(p -> String.format("[%d, %d]", p.x, p.y))
                .collect(Collectors.joining(","));

        return String.format("Player [%s, %s, workers: %s]", name, power, workers);
    }
}
