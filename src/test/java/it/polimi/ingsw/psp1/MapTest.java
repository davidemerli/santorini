package it.polimi.ingsw.psp1;

import it.polimi.ingsw.psp1.santorini.model.map.Map;
import it.polimi.ingsw.psp1.santorini.model.map.SquareData;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.Player;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class MapTest {

    private Map map;

    @Before
    public void setup() {
        this.map = new Map();
    }

    @Test
    public void buildBlock_normalBehaviour_shouldBuildBlock() {
        Point noDome = new Point(2, 2);
        int level = map.getLevel(noDome);

        map.buildBlock(noDome, false);

        assertEquals(map.getLevel(noDome), level + 1);
        assertFalse(map.hasDome(noDome));

        Point dome = new Point(3, 3);
        level = map.getLevel(dome);

        map.buildBlock(dome, true);

        assertEquals(map.getLevel(dome), level + 1);
        assertTrue(map.hasDome(dome));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void buildBlock_blockBuiltOutOfMap_shouldThrowIndexOutOfBounds() {
        Point position = new Point(-1, 5);
        map.buildBlock(position, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildBlock_domePresentInSquare_shouldThrowIllegalArgument() {
        Point position = new Point(2, 2);
        map.buildBlock(position, true);
        map.buildBlock(position, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildBlock_levelInSquareIs4_shouldThrowIllegalArgument() {
        Point position = new Point(2, 2);
        map.buildBlock(position, false);
        map.buildBlock(position, false);
        map.buildBlock(position, false);
        map.buildBlock(position, false);
        map.buildBlock(position, false);
    }

    @Test
    public void removeBlock_normalBehaviour_shouldRemoveBlock() {
        Point position = new Point(2, 2);
        map.buildBlock(position, false);
        int level = map.getLevel(position);
        map.removeBlock(position);
        assertEquals(map.getLevel(position), level - 1);
        assertFalse(map.hasDome(position));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeBlock_blockRemovedOutOfMap_shouldThrowIndexOutOfBounds() {
        Point position = new Point(-1, 5);
        map.removeBlock(position);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeBlock_levelInSquareIs0_shouldThrowIllegalArgument() {
        Point position = new Point(2, 2);
        map.removeBlock(position);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getNeighbors_squareOutOfMap_shouldThrowIndexOutOfBounds() {
        Point position = new Point(-1, 4);
        map.getNeighbors(position);
    }
}
