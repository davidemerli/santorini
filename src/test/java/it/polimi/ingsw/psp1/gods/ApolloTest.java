package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Apollo;
import it.polimi.ingsw.psp1.santorini.model.powers.Mortal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ApolloTest {

    private Game game;
    private Player player1, player2;

    @Before
    public void setup() {
        this.game = new Game(1,2);
        this.player1 = new Player("p1");
        this.player2 = new Player("p2");

        game.addPlayer(player1);
        game.addPlayer(player2);

        player1.setPower(new Apollo());
        player2.setPower(new Mortal());
    }

    @After
    public void teardown() {
        for (int i = player1.getWorkers().size() - 1; i >= 0; i--) {
            player1.removeWorker(player1.getWorkers().get(i));
        }
        for (int i = player2.getWorkers().size() - 1; i >= 0; i--) {
            player2.removeWorker(player2.getWorkers().get(i));
        }
    }

    @Test
    public void getValidMoves_normalBehaviour_shouldContainOtherWorkerPosition() {
        Worker w1 = new Worker(new Point(1, 1));
        Worker w2 = new Worker(new Point(2, 2));
        Worker w3 = new Worker(new Point(1, 2));

        player1.addWorker(w1);
        player2.addWorker(w2);
        player1.addWorker(w3);

        game.startTurn();

        game.getTurnState().selectWorker(player1, w1);

        assertTrue(game.getTurnState().getValidMoves(player1, w1).contains(w2.getPosition()));
        assertFalse(game.getTurnState().getValidMoves(player1, w1).contains(w3.getPosition()));
    }

    @Test
    public void onYourMove_normalBehaviour_shouldSwapWorkers() {
        Worker w1 = new Worker(new Point(1, 1));
        Worker w2 = new Worker(new Point(2, 2));

        player1.addWorker(w1);
        player2.addWorker(w2);

        game.startTurn();

        game.getTurnState().selectWorker(player1, w1);
        game.getTurnState().selectSquare(player1, w2.getPosition());

        assertEquals(new Point(2, 2), w1.getPosition());
        assertEquals(new Point(1, 1), w2.getPosition());
    }

    @Test
    public void getValidMoves_normalBehaviour_shouldNotContainOtherWorkerPosition() {
        Worker w1 = new Worker(new Point(1, 1));
        Worker w2 = new Worker(new Point(3, 3));

        player1.addWorker(w1);
        player2.addWorker(w2);

        game.startTurn();

        game.getTurnState().selectWorker(player1, w1);

        assertFalse(game.getTurnState().getValidMoves(player1, w1).contains(w2.getPosition()));
    }
}
