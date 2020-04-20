package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;

import java.util.List;

public class PlayerData {

    private final String name;
    private final Power power;
    private final List<Worker> workers;

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
}
