package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.controller.game.Play;
import it.polimi.ingsw.psp1.santorini.controller.turn.EndTurn;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Minotaur;
import it.polimi.ingsw.psp1.santorini.model.powers.Mortal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class MinotaurTest {

    private Game game;
    private Player player1, player2;

    @Parameterized.Parameter
    public Point enemyPos;

    @Parameterized.Parameter(1)
    public Point enemyExpectedPosAfterPush;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new Point(1, 1), new Point(0, 0)},
                {new Point(2, 1), new Point(2, 0)},
                {new Point(3, 1), new Point(4, 0)},
                {new Point(3, 2), new Point(4, 2)},
                {new Point(3, 3), new Point(4, 4)},
                {new Point(2, 3), new Point(2, 4)},
                {new Point(1, 3), new Point(0, 4)},
                {new Point(1, 2), new Point(0, 2)}
        });
    }

    @Before
    public void setup() {
        this.game = new Game();
        this.player1 = new Player("p1");
        this.player2 = new Player("p2");

        game.addPlayer(player1);
        game.addPlayer(player2);

        player1.setPower(new Minotaur(player1));
        player2.setPower(new Mortal(player2));

        player1.setGameState(new Play());
        player1.newTurn(game);
        player2.setGameState(new Play());
        player2.setTurnState(new EndTurn(player2, game));
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
        Worker w1 = new Worker(new Point(2, 2));
        Point e = new Point(enemyPos);
        Worker w2 = new Worker(e);

        player1.addWorker(w1);
        player2.addWorker(w2);

        player1.setSelectedWorker(w1);

        player1.getPower().onYourMove(w1, e, game);

        assertEquals(e, w1.getPosition());
        assertEquals(enemyExpectedPosAfterPush, w2.getPosition());
    }

    @Test
    public void getValidMoves_normalBehaviour_shouldNotContainOtherWorkerPosition() {
        Worker w1 = new Worker(new Point(1, 1));
        Worker w2 = new Worker(new Point(2, 2));
        Worker w3 = new Worker(new Point(3, 3));

        player1.addWorker(w1);
        player2.addWorker(w2);
        player2.addWorker(w3);

        player1.setSelectedWorker(w1);

        assertFalse(player1.getPower().getValidMoves(game).contains(w2.getPosition()));
    }
}
