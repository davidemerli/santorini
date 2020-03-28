package it.polimi.ingsw.PSP1;

import it.polimi.ingsw.PSP1.santorini.map.Map;
import it.polimi.ingsw.PSP1.santorini.map.Worker;
import it.polimi.ingsw.PSP1.santorini.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertTrue;

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
}
