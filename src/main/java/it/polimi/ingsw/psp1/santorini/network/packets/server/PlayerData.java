package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.model.powers.Power;

public class PlayerData {

    private final String name;
    private final Power power;

    public PlayerData(String name, Power power) {
        this.name = name;
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public Power getPower() {
        return power;
    }
}
