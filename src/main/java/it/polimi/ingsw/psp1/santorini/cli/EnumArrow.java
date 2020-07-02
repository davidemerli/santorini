package it.polimi.ingsw.psp1.santorini.cli;

import it.polimi.ingsw.psp1.santorini.model.map.Point;

import java.util.Arrays;

public enum EnumArrow {

    LEFT("\u2190", new Point(-1, 0)),
    LEFT_UP("\u2196", new Point(-1, -1)),
    UP("\u2191", new Point(0, -1)),
    RIGHT_UP("\u2197", new Point(1, -1)),
    RIGHT("\u2192", new Point(1, 0)),
    RIGHT_DOWN("\u2198", new Point(1, 1)),
    DOWN("\u2193", new Point(0, 1)),
    LEFT_DOWN("\u2199", new Point(-1, 1));

    private final String normal;
    private final Point vector;

    EnumArrow(String normal, Point vector) {
        this.normal = normal;
        this.vector = vector;
    }

    /**
     * Gets the arrow associated with a movement vector
     *
     * @param vector the move
     * @return the corresponding arrow
     */
    public static EnumArrow fromVector(Point vector) {
        return Arrays.stream(values())
                .filter(arrow -> arrow.getVector().equals(vector))
                .findFirst()
                .orElseThrow();
    }

    /**
     * @return unicode arrow character value
     */
    public String toUnicode() {
        return normal;
    }

    /**
     * @return the arrow vector as a vector Point
     */
    public Point getVector() {
        return vector;
    }
}
