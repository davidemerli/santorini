package it.polimi.ingsw.PSP1.santorini.map;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Map {

    private final int SIDE_LENGTH = 5;

    private final SquareData[][] blockMatrix;
    private final List<Worker> workersList;

    /**
     * Main constructor for the Map class
     * Used to instantiate an empty map with no workers and every square set to level 0
     */
    public Map() {
        this.blockMatrix = new SquareData[SIDE_LENGTH][SIDE_LENGTH];
        this.workersList = new ArrayList<>();
        initMatrix();
    }

    /**
     * Copy constructor
     * Used to instantiate a new 'step' of the map, based on the previous one, since Map is immutable
     * Both workers and square data are copied.
     *
     * @param oldMap where the current state is copied from.
     */
    private Map(Map oldMap) {
        this.blockMatrix = oldMap.getMatrixCopy();
        this.workersList = oldMap.getWorkersCopy();
    }

    /**
     * Used to add a worker on the map.
     * If there is already a worker or the worker's position is out of matrix, an exception is thrown.
     *
     * @param worker Contains worker's player and worker's position
     * @return new map with a new worker on it.
     */
    public Map addWorker(Worker worker) {

        if(workersList.contains(worker)) {
            throw new IllegalArgumentException("worker already in");
        }
        if(!isPositionInMap(worker.getPosition())) {
            throw new IndexOutOfBoundsException("illegal position");
        }

        Map newMap = new Map(this);

        newMap.workersList.add(worker);
        return newMap;
    }

    /**
     * Used to remove a worker from map.
     * If there is not worker in the list, an exception is thrown.
     *
     * @param worker Contains worker's player and worker's position
     * @return new map without a worker on it
     */
    public Map removeWorker(Worker worker) {

        if(!workersList.contains(worker)) {
            throw new IllegalArgumentException("worker not found");
        }

        Map newMap = new Map(this);

        newMap.workersList.remove(worker);
        return newMap;
    }

    /**
     * Used to move a worker on the map.
     * If there is already a worker or the worker's position is out of matrix or occuped by another worker,
     * an exception is thrown
     *
     * @param worker Contains worker's player and worker's position
     * @param newPosition Contains the new worker's position
     * @return new map with a worker in a different position
     */
    public Map moveWorker(Worker worker, Point newPosition) {

        if(!isPositionInMap(newPosition)) {
            throw new IndexOutOfBoundsException("illegal position");
        }
        if(!workersList.contains(worker)) {
            throw new IllegalArgumentException("worker not found");
        }
        for (Worker worker1 : workersList) {
            if(worker.getPosition().equals(newPosition)) {
                throw new IllegalArgumentException("illegal position");
            }
        }

        Map newMap = new Map(this);

        int index = newMap.workersList.indexOf(worker);
        Worker newWorker = new Worker(worker.getPlayer(), newPosition);
        newMap.workersList.set(index, newWorker);
        return newMap;
    }

    /**
     * Used to raise the level of a given square by 1, building a block and eventually adding a dome
     * Exceptions made if the position given is out of the map or the player is building on square where the dome
     * is present
     *
     * @param position coordinates of the square where the player is building
     * @param buildDome is true if the block built is a dome
     * @return the updated map
     */
    public Map buildBlock(Point position, boolean buildDome) {
        if (!isPositionInMap(position)) {
            throw new ArrayIndexOutOfBoundsException("Given position is out of map");
        }
        Map newMap = new Map(this);
        SquareData oldSquareData = newMap.blockMatrix[position.x][position.y];
        if (oldSquareData.isDome()) {
            throw new IllegalArgumentException("Dome present in the square selected");
        }
        SquareData newSquareData = new SquareData(oldSquareData.getLevel() + 1, buildDome);
        newMap.blockMatrix[position.x][position.y] = newSquareData;
        return newMap;
    }

    /**
     * Used to lower the level of a given square by 1, removing a block
     * Exceptions made if the position given is out of map or the player is lowering the level below 0
     *
     * @param position coordinates of the square where the player is removing
     * @return the updated map
     */
    public Map removeBlock(Point position) {
        if (!isPositionInMap(position)) {
            throw new ArrayIndexOutOfBoundsException("Given position is out of map");
        }
        Map newMap = new Map(this);
        SquareData oldSquareData = newMap.blockMatrix[position.x][position.y];
        if (oldSquareData.getLevel() == 0) {
            throw new IllegalArgumentException("No blocks present in the square selected");
        }
        SquareData newSquareData = new SquareData(oldSquareData.getLevel() - 1, false);
        newMap.blockMatrix[position.x][position.y] = newSquareData;
        return newMap;
    }

    /**
     * Checks if the position given is inside the map matrix
     *
     * @param position coordinates of the square
     * @return true if the square is inside the matrix
     */
    private boolean isPositionInMap(Point position) {
        return position.x < SIDE_LENGTH && position.y < SIDE_LENGTH && position.x >= 0 && position.y >= 0;
    }

    /**
     * Instantiate a new ArrayList of cloned Workers, to keep everything immutable
     *
     * @return a perfect clone of the current array of Workers
     */
    private List<Worker> getWorkersCopy() {
        return workersList.stream()
                .map(w -> new Worker(w.getPlayer(), w.getPosition()))
                .collect(Collectors.toList());
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
     * Initializes the square data matrix with all level 0 values
     */
    private void initMatrix() {
        for (int i = 0; i < SIDE_LENGTH; i++) {
            for (int j = 0; j < SIDE_LENGTH; j++) {
                blockMatrix[i][j] = new SquareData(0, false);
            }
        }
    }
}
