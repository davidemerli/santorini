package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.map.Map;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.controller.game.Play;
import it.polimi.ingsw.psp1.santorini.controller.turn.BeginTurn;
import it.polimi.ingsw.psp1.santorini.model.powers.Apollo;
import it.polimi.ingsw.psp1.santorini.model.powers.Mortal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class ApolloTest {

    private Game game;
    private Player player1, player2;

    @Before
    public void setup() {
        this.game = new Game();
        this.player1 = new Player("p1");
        this.player2 = new Player("p2");

        game.addPlayer(player1);
        game.addPlayer(player2);

        player1.setPower(new Apollo(player1));
        player2.setPower(new Mortal(player2));

        player1.setGameState(new Play());
        player1.setTurnState(new BeginTurn(player1, game));
        player2.setGameState(new Play());
        player2.setTurnState(new BeginTurn(player1, game));
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

        player1.setSelectedWorker(w1);

        assertTrue(player1.getPower().getValidMoves(game).contains(w2.getPosition()));

        assertFalse(player1.getPower().getValidMoves(game).contains(w3.getPosition()));
    }

    @Test
    public void onYourMove_normalBehaviour_shouldSwapWorkers() {
        Worker w1 = new Worker(new Point(1, 1));
        Worker w2 = new Worker(new Point(2, 2));

        player1.addWorker(w1);
        player2.addWorker(w2);

        player1.setSelectedWorker(w1);

        player1.getPower().onYourMove(w1, w2.getPosition(), game);

        assertEquals(new Point(2, 2), w1.getPosition());
        assertEquals(new Point(1, 1), w2.getPosition());
    }

    @Test
    public void getValidMoves_normalBehaviour_shouldNotContainOtherWorkerPosition() {
        Worker w1 = new Worker(new Point(1, 1));
        Worker w2 = new Worker(new Point(3, 3));

        player1.addWorker(w1);
        player2.addWorker(w2);

        player1.setSelectedWorker(w1);

        assertFalse(player1.getPower().getValidMoves(game).contains(w2.getPosition()));
    }
}
