package it.polimi.ingsw.PSP1.santorini.map;

import it.polimi.ingsw.PSP1.santorini.player.Player;

import java.awt.*;

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
}
