package it.polimi.ingsw.psp1;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.game.ChoosePlayerPower;
import it.polimi.ingsw.psp1.santorini.model.game.Play;
import it.polimi.ingsw.psp1.santorini.model.game.SelectPowers;
import it.polimi.ingsw.psp1.santorini.model.game.Wait;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Athena;
import it.polimi.ingsw.psp1.santorini.model.powers.Minotaur;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.model.powers.Triton;
import it.polimi.ingsw.psp1.santorini.model.turn.WorkerPlacing;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Collections;
import java.util.List;
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
        Player challenger = player1;
        challenger.setGameState(new SelectPowers());

        game.getPlayerOpponents(challenger).forEach(p -> p.setGameState(new Wait()));

        challenger.getGameState().selectGod(game, new Athena());
        challenger.getGameState().selectGod(game, new Minotaur());
        challenger.getGameState().selectGod(game, new Triton());

        assertTrue(challenger.getGameState() instanceof Wait);

        player2.setGameState(new ChoosePlayerPower());

        List<Power> powers = game.getAvailablePowers();

        player2.getGameState().selectGod(game, powers.get(0));
        player2.setGameState(new Wait());

        player3.setGameState(new ChoosePlayerPower());
        player3.getGameState().selectGod(game, powers.get(0));

        player1.setPower(powers.get(0));
        powers.clear();
        game.getPlayerList().forEach(p -> p.setGameState(new Play()));

        game.shufflePlayers();
        game.removePlayer(player1);
        game.addPlayer(player1);

        game.setTurnState(new WorkerPlacing(game));

        int[][] ints = {{0, 0}, {0, 1}, {1, 0}, {0, 2}, {2, 0}, {3, 3}};

        for (int i = 0; i < 6; i++) {
            game.getTurnState().selectSquare(game.getPlayerList().get(0), new Point(ints[i][0], ints[i][1]));

            if (i % 2 == 0) {
                game.shiftPlayers(1);
            }
        }

        game.nextTurn();

        System.out.println();
    }
}
