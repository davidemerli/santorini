package it.polimi.ingsw.psp1.santorini.model.map;

import java.io.Serializable;
import java.util.Objects;

public class Point implements Serializable {

    public final int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point toCopy) {
        this.x = toCopy.x;
        this.y = toCopy.y;
    }

    /**
     * Calculates distance between two points
     *
     * @param other variable point
     * @return distance between points
     */
    public double distance(Point other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }

    /**
     * Adds a value from the point's coordinates
     *
     * @param toAdd value to add
     * @return new point
     */
    public Point add(Point toAdd) {
        return new Point(this.x + toAdd.x, this.y + toAdd.y);
    }

    /**
     * Subtracts a value from the point's coordinates
     *
     * @param toSubtract value to subtract
     * @return new point
     */
    public Point subtract(Point toSubtract) {
        return new Point(this.x - toSubtract.x, this.y - toSubtract.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("Point [x:%d, y:%d]", x, y);
    }
}
