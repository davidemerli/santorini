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

    public Map addWorker(Worker worker) {
        //TODO

        return null;
    }

    public Map removeWorker(Worker worker) {
        //TODO

        return null;
    }

    public Map moveWorker(Worker worker, Point newPosition) {
        //TODO

        return null;
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
