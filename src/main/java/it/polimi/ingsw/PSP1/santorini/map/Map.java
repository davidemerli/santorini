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
     * @return new map with a new worker on it
     */
    public Map addWorker(Worker worker) {

        if(workersList.contains(worker)) {
            throw new IllegalArgumentException("worker already in");
        }
        if(worker.getPosition().x < 0 || worker.getPosition().x > 6
                || worker.getPosition().y < 0  || worker.getPosition().y > 6) {
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
     * Used to move a worker on the map
     *
     * @param worker Contains worker's player and worker's position
     * @param newPosition Contains the new worker's position
     * @return new map with a worker in a different position
     */
    public Map moveWorker(Worker worker, Point newPosition) {

        if(newPosition.x < 0 || newPosition.x > 6 || newPosition.y < 0 || newPosition.y > 6) {
            throw new IndexOutOfBoundsException("illegal position");
        }
        if(!workersList.contains(worker)) {
            throw new IllegalArgumentException("worker not found");
        }
        Map newMap = new Map(this);

        int index = newMap.workersList.indexOf(worker);
        Worker newWorker = new Worker(worker.getPlayer(), newPosition);
        newMap.workersList.set(index, newWorker);
        return newMap;
    }

    public Map buildBlock(Point position, boolean buildDome) {
        //TODO

        return null;
    }

    public Map removeBlock(Point position) {
        //TODO

        return null;
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
