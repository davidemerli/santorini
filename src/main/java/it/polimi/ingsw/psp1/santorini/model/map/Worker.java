package it.polimi.ingsw.psp1.santorini.model.map;

import java.awt.*;

public class Worker {

    private Point position;

    public Worker(Point position) {
        this.position = position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return new Point(position);
    }
}
