package it.polimi.ingsw.PSP1.santorini.map;

import it.polimi.ingsw.PSP1.santorini.player.Player;

import java.awt.*;
import java.util.Objects;

public class Worker {

    private final Player player;
    private final Point position;

    public Worker(Player player, Point position) {
        this.player = player;
        this.position = position;
    }

    public Player getPlayer() {
        return player;
    }

    public Point getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Worker worker = (Worker) o;

        if (!Objects.equals(player, worker.player)) return false;

        return position.equals(worker.position);
    }


}
