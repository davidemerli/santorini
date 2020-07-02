package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Chronus;
import it.polimi.ingsw.psp1.santorini.model.powers.Mortal;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import it.polimi.ingsw.psp1.santorini.model.turn.Move;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ChronusTest {

    private Game game;
    private Player player1;
    private Player player2;

    @Before
    public void setup() {
        this.game = new Game("1",2);
        this.player1 = new Player("p1");
        this.player2 = new Player("p2");
        game.addPlayer(player1);
        game.addPlayer(player2);

        player1.setPower(new Chronus());
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
    public void onBuild_normalBehaviour_shouldWinWithFiveCompleteTowers() {
        Worker w1 = new Worker(new Point(1, 3));
        Worker w2 = new Worker(new Point(4, 4));
        Worker w3 = new Worker(new Point(3, 4));
        Worker w4 = new Worker(new Point(4, 3));

        player1.addWorker(w1);
        player1.addWorker(w2);
        player2.addWorker(w3);
        player2.addWorker(w4);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                game.getMap().buildBlock(new Point(0, i), false);
            }
        }

        for (int i = 0; i < 3; i++) {
            game.getMap().buildBlock(new Point(0, 4), false);
        }

        game.startTurn();

        assertTrue(game.getTurnState() instanceof Move);

        game.getTurnState().selectWorker(game, player1, w1);
        game.getTurnState().selectSquare(game, player1, new Point(1, 4));

        assertTrue(game.getTurnState() instanceof Build);

        game.getTurnState().selectSquare(game, player1, new Point(0, 4));

        assertTrue(player1.hasWon());
    }

    @Test
    public void onEnemyBuild_normalBehaviour_shouldWinWithFiveCompleteTowers() {
        Worker w1 = new Worker(new Point(1, 3));
        Worker w2 = new Worker(new Point(4, 4));
        Worker w3 = new Worker(new Point(3, 4));
        Worker w4 = new Worker(new Point(4, 3));

        player2.addWorker(w1);
        player2.addWorker(w2);
        player1.addWorker(w3);
        player1.addWorker(w4);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                game.getMap().buildBlock(new Point(0, i), false);
            }
        }

        for (int i = 0; i < 3; i++) {
            game.getMap().buildBlock(new Point(0, 4), false);
        }

        game.shiftPlayers(-1);

        game.startTurn();

        assertTrue(game.getTurnState() instanceof Move);

        game.getTurnState().selectWorker(game, player2, w1);
        game.getTurnState().selectSquare(game, player2, new Point(1, 4));

        assertTrue(game.getTurnState() instanceof Build);

        game.getTurnState().selectSquare(game, player2, new Point(0, 4));

        assertTrue(player1.hasWon());
    }
}
