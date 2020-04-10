package it.polimi.ingsw.psp1;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.game.SelectPowers;
import it.polimi.ingsw.psp1.santorini.model.game.Wait;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Athena;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class GameTest {

    private Game game;
    private Player player1, player2, player3;

    @Before
    public void setup() {
        this.game = new Game();
        this.player1 = new Player("p1");
        this.player2 = new Player("p2");
        this.player3 = new Player("p3");

        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
    }

    @After
    public void teardown() {
        for (int i = player1.getWorkers().size() - 1; i >= 0; i--) {
            player1.removeWorker(player1.getWorkers().get(i));
        }
        for (int i = player2.getWorkers().size() - 1; i >= 0; i--) {
            player2.removeWorker(player2.getWorkers().get(i));
        }
        for (int i = player3.getWorkers().size() - 1; i >= 0; i--) {
            player3.removeWorker(player3.getWorkers().get(i));
        }
    }

    @Test
    public void getWorkerOn_normalBehaviour_shouldGiveWorkerOnPosition() {
        Point position = new Point(1, 1);
        Worker w1 = new Worker(position);

        player1.addWorker(w1);

        assertTrue(game.getWorkerOn(position).isPresent());
        assertEquals(w1, game.getWorkerOn(position).get());
    }

    @Test
    public void getPlayerOf_normalBehaviour_shouldGiveOwnerOfWorker() {
        Point position = new Point(1, 1);
        Worker w1 = new Worker(position);

        player1.addWorker(w1);

        assertTrue(game.getPlayerOf(w1).isPresent());
        assertEquals(player1, game.getPlayerOf(w1).get());
    }

    @Test
    public void moveWorker_normalBehaviour_shouldMoveWorker() {
        Point oldPosition = new Point(1, 1);
        Point newPosition = new Point(1, 2);
        Worker w1 = new Worker(oldPosition);

        player1.addWorker(w1);
        game.moveWorker(player1, w1, newPosition);

        assertFalse(game.getWorkerOn(oldPosition).isPresent());
        assertTrue(game.getWorkerOn(newPosition).isPresent());
        assertEquals(w1, game.getWorkerOn(newPosition).get());

    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void moveWorker_workerMovedOutOfMap_shouldThrowIndexOutOfBoundsException() {
        Point oldPosition = new Point(0, 0);
        Point newPosition = new Point(-1, 0);
        Worker w1 = new Worker(oldPosition);

        player1.addWorker(w1);
        game.moveWorker(player1, w1, newPosition);
    }

    @Test(expected = NoSuchElementException.class)
    public void moveWorker_workerNotPresent_shouldThrowNoSuchElementException() {
        Point position = new Point(1, 1);
        Worker w1 = new Worker(position);

        game.moveWorker(player1, w1, position);
    }

    @Test
    public void getPlayerOpponents_normalBehaviour_shouldListOpponentsOfPlayer() {
        assertTrue(game.getPlayerOpponents(player1).contains(player2));
        assertTrue(game.getPlayerOpponents(player1).contains(player3));
    }

    @Test
    public void preGameStates_normalBehaviour_shouldStartGame() {
        Player challenger = game.getPlayerList().get(1);
        challenger.setGameState(new SelectPowers());

        game.getPlayerOpponents(challenger).forEach(p -> p.setGameState(new Wait()));

        challenger.getGameState().selectGod(game, challenger, new Athena());

    }
}
