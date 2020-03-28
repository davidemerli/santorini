package it.polimi.ingsw.PSP1;

import it.polimi.ingsw.PSP1.santorini.map.Map;
import it.polimi.ingsw.PSP1.santorini.map.Worker;
import it.polimi.ingsw.PSP1.santorini.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class MapTest {

    private Map map;
    private Player player;

    @Before
    public void setup() {
        this.map = new Map();
        this.player = new Player();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addWorker_workerInsertedTwice_shouldThrowUnsupportedOperation() {
        Worker worker = new Worker(player, new Point(1, 1));
        Map newMap = map.addWorker(worker);

        newMap.addWorker(worker);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addWorker_workerInsertedOutOfMap_shouldThrowIndexOutOfBoundsOperation() {
        Worker worker = new Worker(player, new Point(-1, 1));
        map.addWorker(worker);
    }

    @Test
    public void addWorker_normalInsertion_shouldHaveAddedWorker() {
        Worker worker = new Worker(player, new Point(1, 1));
        Map newMap = map.addWorker(worker);

        assertTrue(newMap.getWorkersList().contains(worker));
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeWorker_workerNotFound_shouldThrowIllegalArgument() {
        Worker worker = new Worker(player, new Point(1,1));
        map.removeWorker(worker);
    }

    @Test
    public void removeWorker_normalRemoval_shouldHaveRemovedWorker() {
        Worker worker = new Worker(player, new Point(1,1));
        Map newMap = map.addWorker(worker);
        newMap = newMap.removeWorker(worker);

        assertFalse(newMap.getWorkersList().contains(worker));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void moveWorker_workerMovedOutOfMap_shouldThrowIndexOutOfBounds() {
        Worker worker = new Worker(player, new Point(4,4));
        Map newMap = map.addWorker(worker);

        newMap.moveWorker(worker, new Point(5,5));
    }

    @Test(expected = NoSuchElementException.class)
    public void moveWorker_workerNotFound_shouldThrownNoSuchElement() {
        Worker worker = new Worker(player, new Point(1,1));
        map.moveWorker(worker, new Point(1,2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void moveWorker_workerMovedIntoAnotherWorker_shouldThrownIllegalArgument() {
        Worker worker1 = new Worker(player, new Point(1,1));
        Worker worker2 = new Worker(player, new Point(1,2));
        Map newMap = map.addWorker(worker1);
        newMap = newMap.addWorker(worker2);
        newMap.moveWorker(worker1, worker2.getPosition());
    }

    @Test(expected = IllegalArgumentException.class)
    public void moveWorker_workerMovedTooFar_shouldThrowIllegalArgument() {
        Worker worker = new Worker(player, new Point(1,1));
        Map newMap = map.addWorker(worker);
        newMap.moveWorker(worker, new Point(2,3));
    }

    @Test
    public void moveWorker_normalMoved_shouldHaveMovedWorker() {
        Point point1 = new Point(2,2);
        Point point2 = new Point(2,3);
        Point point3 = new Point(3,3);
        Point point4 = new Point(3,2);

        Worker worker = new Worker(player, point1);
        Map newMap = map.addWorker(worker);
        newMap = newMap.moveWorker(worker, point2);
        worker = newMap.getWorkersList().get(0);

        assertEquals(point2, worker.getPosition());

        newMap = newMap.moveWorker(worker, point3);
        worker = newMap.getWorkersList().get(0);

        assertEquals(point3, worker.getPosition());

        newMap = newMap.moveWorker(worker, point4);
        worker = newMap.getWorkersList().get(0);

        assertEquals(point4, worker.getPosition());

        newMap = newMap.moveWorker(worker, point1);
        worker = newMap.getWorkersList().get(0);

        assertEquals(point1, worker.getPosition());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeBlock_workerOn_shouldThrownIllegalArgument() {
        Point position = new Point(1,1);
        Worker worker = new Worker(player, position);
        Map newMap = map.addWorker(worker);
        newMap.removeBlock(position);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildBlock_workerOn_shouldThrowIllegalArgument() {
        Point position = new Point(1,1);
        Worker worker = new Worker(player, position);
        Map newMap = map.addWorker(worker);
        newMap.buildBlock(position, false);
    }
}
