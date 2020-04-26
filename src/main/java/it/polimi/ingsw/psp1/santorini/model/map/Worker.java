package it.polimi.ingsw.psp1.santorini.model.map;

import it.polimi.ingsw.psp1.santorini.model.Player;

import java.awt.*;
import java.io.Serializable;

public class Worker implements Serializable {

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

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != this.getClass()) {
            return false;
        }

        return this.getPosition().equals(((Worker)obj).getPosition());
    }
}
