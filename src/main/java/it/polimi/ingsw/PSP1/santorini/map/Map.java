package it.polimi.ingsw.PSP1.santorini.map;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Map {

    private final SquareData[][] blockMatrix;
    private final List<Worker> workersList;

    public Map() {
        this.blockMatrix = new SquareData[5][5];
        this.workersList = new ArrayList<>();
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
}
