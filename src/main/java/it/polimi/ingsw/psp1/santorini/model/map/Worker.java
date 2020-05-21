package it.polimi.ingsw.psp1.santorini.model.map;

import java.io.Serializable;
import java.util.Objects;

public class Worker implements Serializable {

    private Point position;

    public Worker(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return new Point(position);
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Worker worker = (Worker) o;
        return position.equals(worker.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }
}
