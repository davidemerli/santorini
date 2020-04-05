package it.polimi.ingsw.psp1.santorini.model.map;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Map {

    public static final int SIDE_LENGTH = 5;

    private final SquareData[][] blockMatrix;

    /**
     * Main constructor for the Map class
     * Used to instantiate an empty map with no workers and every square set to level 0
     */
    public Map() {
        this.blockMatrix = new SquareData[SIDE_LENGTH][SIDE_LENGTH];

        for (int i = 0; i < SIDE_LENGTH; i++) {
            for (int j = 0; j < SIDE_LENGTH; j++) {
                blockMatrix[i][j] = new SquareData(0, false);
            }
        }
    }

    /**
     * Creates a copy of the given map
     *
     * @param toCopy where the current state is copied from.
     */
    private Map(Map toCopy) {
        this.blockMatrix = toCopy.getMatrixCopy();
    }

    /**
     * Used to raise the level of a given square by 1, building a block and eventually adding a dome
     *
     * @param position  coordinates of the square where the player is building
     * @param forceDome whether the player is trying to build a dome on unnatural conditions
     * @throws ArrayIndexOutOfBoundsException if you try to build out of the map
     * @throws IllegalArgumentException       if you try to build in a position occupied by a dome
     */
    public void buildBlock(Point position, boolean forceDome) {
        if (isPositionOutOfMap(position)) {
            throw new ArrayIndexOutOfBoundsException("Given position is out of map");
        }

        if (hasDome(position)) {
            throw new IllegalArgumentException("Dome present in the square selected");
        }

        boolean shouldBuildDome = forceDome || getLevel(position) == 3;

        SquareData newSquareData = new SquareData(getLevel(position) + 1, shouldBuildDome);
        blockMatrix[position.x][position.y] = newSquareData;
    }

    /**
     * Used to lower the level of a given square by 1, removing a block
     *
     * @param position coordinates of the square where the player is removing
     * @throws ArrayIndexOutOfBoundsException if you try to remove a block out of the map
     * @throws IllegalArgumentException       if you try to remove a block occupied by another worker
     * @throws IllegalArgumentException       if you try to remove a block that doesn't exist
     */
    public void removeBlock(Point position) {
        if (isPositionOutOfMap(position)) {
            throw new ArrayIndexOutOfBoundsException("Given position is out of map");
        }

        if (getLevel(position) == 0) {
            throw new IllegalArgumentException("No blocks present in the square selected");
        }

        SquareData newSquareData = new SquareData(getLevel(position) - 1, false);
        blockMatrix[position.x][position.y] = newSquareData;
    }

    /**
     * Returns the SquareData corresponding to the given position
     *
     * @param position on the matrix
     * @return corresponding SquareData
     */
    private SquareData getSquareData(Point position) {
        return blockMatrix[position.x][position.y];
    }

    /**
     * Gets the level of the square at given position on the map
     *
     * @param position coordinates on the map
     * @return the level at given position
     */
    public int getLevel(Point position) {
        return getSquareData(position).getLevel();
    }

    /**
     * Checks if on the square at the given position is present a dome
     *
     * @param position coordinates on the map
     * @return true if there is a dome
     */
    public boolean hasDome(Point position) {
        return getSquareData(position).isDome();
    }

    /**
     * Checks if the given position is outside of the map matrix
     *
     * @param position the coordinates of the square
     * @return true if the square is outside the matrix
     */
    public boolean isPositionOutOfMap(Point position) {
        return position.x >= SIDE_LENGTH || position.y >= SIDE_LENGTH || position.x < 0 || position.y < 0;
    }

    /**
     * Checks if the given position is on the perimeter of the map
     *
     * @param position the coordinates of the square
     * @return true if the square is on the perimeter
     */
    public boolean isPerimeter(Point position) {
        return position.x == 0 || position.y == 0 || position.x == SIDE_LENGTH || position.y == SIDE_LENGTH;
    }

    /**
     * Instantiate a new matrix of cloned Squares, to keep everything immutable
     *
     * @return a perfect clone of the current SquareData matrix
     */
    private SquareData[][] getMatrixCopy() {
        SquareData[][] newMatrix = new SquareData[SIDE_LENGTH][SIDE_LENGTH];

        for (int i = 0; i < SIDE_LENGTH; i++) {
            for (int j = 0; j < SIDE_LENGTH; j++) {
                SquareData squareToCopy = blockMatrix[i][j];

                newMatrix[i][j] = new SquareData(squareToCopy.getLevel(), squareToCopy.isDome());
            }
        }

        return newMatrix;
    }

    /**
     * Gets the valid squares surrounding a given position
     *
     * @param point the position you need to get the neighbour blocks of
     * @return list of positions of valid surrounding squares
     * @throws ArrayIndexOutOfBoundsException if point is out of map
     */
    public List<Point> getNeighbors(Point point) {
        if (isPositionOutOfMap(point)) {
            throw new ArrayIndexOutOfBoundsException("Given point is not on map");
        }

        List<Point> list = new ArrayList<>();

        for (int i = Math.max(0, point.x - 1); i <= Math.min(SIDE_LENGTH - 1, point.x + 1); i++) {
            for (int j = Math.max(0, point.y - 1); j <= Math.min(SIDE_LENGTH - 1, point.y + 1); j++) {
                list.add(new Point(i, j));
            }
        }

        return list;
    }

    public Map copy() {
        return new Map(this);
    }

    public List<Point> getAllSquares() {
        List<Point> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                list.add(new Point(i, j));
            }
        }

        return list;
    }
}
