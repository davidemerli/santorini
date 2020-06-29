package it.polimi.ingsw.psp1.santorini.model.map;

import java.io.Serializable;

/**
 * Defines a standard block of the map containing
 * the level of the block and the presence of a dome
 */
public class SquareData implements Serializable {

    private final int level;
    private final boolean isDome;

    SquareData(int level, boolean isDome) {
        this.level = level;
        this.isDome = isDome;
    }

    public int getLevel() {
        return level;
    }

    public boolean isDome() {
        return isDome;
    }
}
