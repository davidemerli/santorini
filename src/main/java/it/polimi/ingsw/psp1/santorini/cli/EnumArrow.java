package it.polimi.ingsw.psp1.santorini.cli;

import it.polimi.ingsw.psp1.santorini.model.map.Point;

import java.util.Arrays;

public enum EnumArrow {

    LEFT("\u2190", "\u2194", new Point(-1, 0)),
    LEFT_UP("\u2196", "\u2921", new Point(-1, -1)),
    UP("\u2191", "\u2195", new Point(0, -1)),
    RIGHT_UP("\u2197", "\u2922", new Point(1, -1)),
    RIGHT("\u2192", "\u2194", new Point(1, 0)),
    RIGHT_DOWN("\u2198", "\u2921", new Point(1, 1)),
    DOWN("\u2193", "\u2195", new Point(0, 1)),
    LEFT_DOWN("\u2199", "\u2922", new Point(-1, 1));

    private final String normal;
    private final String bidirectional;
    private final Point vector;

    EnumArrow(String normal, String bidirectional, Point vector) {
        this.normal = normal;
        this.bidirectional = bidirectional;
        this.vector = vector;
    }

    public static EnumArrow fromVector(Point vector) {
        return Arrays.stream(values())
                .filter(arrow -> arrow.getVector().equals(vector))
                .findFirst()
                .orElseThrow();
    }

    public String getNormal() {
        return normal;
    }

    public String getBidirectional() {
        return bidirectional;
    }

    public Point getVector() {
        return vector;
    }
}
