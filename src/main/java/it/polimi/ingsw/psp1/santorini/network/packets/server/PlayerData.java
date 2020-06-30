package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages the main information about player
 */
public class PlayerData implements Serializable {

    private final String name;
    private final Power power;
    private final List<Worker> workers;

    /**
     * Generic constructor
     *
     * @param name    player name
     * @param power   player power
     * @param workers workers list
     */
    public PlayerData(String name, Power power, List<Worker> workers) {
        this.name = name;
        this.power = power;
        this.workers = workers;
    }

    public String getName() {
        return name;
    }

    public Power getPower() {
        return power;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    @Override
    public String toString() {
        String workerList = workers.stream()
                .map(Worker::getPosition)
                .map(p -> String.format("[%d, %d]", p.x, p.y))
                .collect(Collectors.joining(","));

        return String.format("Player [%s, %s, workers: %s]", name, power, workerList);
    }
}
