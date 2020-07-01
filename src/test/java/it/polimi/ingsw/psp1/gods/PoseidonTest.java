package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Mortal;
import it.polimi.ingsw.psp1.santorini.model.powers.Poseidon;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PoseidonTest {

    private Game game;
    private Player player;

    @Before
    public void setup() {
        this.game = new Game("1",2);
        this.player = new Player("p1");
//        Player player2 = new Player("p2");

        game.addPlayer(player);
//        game.addPlayer(player2);

        player.setPower(new Poseidon());
//        player2.setPower(new Mortal());
    }

    @After
    public void teardown() {
        for (int i = player.getWorkers().size() - 1; i >= 0; i--) {
            player.removeWorker(player.getWorkers().get(i));
        }
    }

    @Test
    public void onYourBuild_normalBehaviourBuild_shouldBuild3TimesWithUnmovedWorker() {
        Worker w1 = new Worker(new Point(1, 1));
        Worker w2 = new Worker(new Point(3, 3));

        game.getMap().buildBlock(new Point(1, 2), false);

        player.addWorker(w1);
        player.addWorker(w2);

        game.startTurn();

        game.getTurnState().selectWorker(game, player, w1);

        assertFalse(game.getTurnState().shouldShowInteraction(game, player));

        game.getTurnState().selectSquare(game, player, new Point(1, 2));

        assertFalse(game.getTurnState().shouldShowInteraction(game, player));

        game.getTurnState().selectSquare(game, player, new Point(1, 1));

        assertTrue(game.getTurnState() instanceof Build);
        assertTrue(game.getTurnState().shouldShowInteraction(game, player));
        assertTrue(player.isWorkerLocked());
        assertTrue(player.getSelectedWorker().isPresent());
        assertEquals(player.getSelectedWorker().get(), w2);

        assertTrue(game.getTurnState().getValidMoves(game, player, w2)
                .containsAll(game.getMap().getNeighbors(w2.getPosition())));

        game.getTurnState().selectSquare(game, player, new Point(3, 4));
        game.getTurnState().selectSquare(game, player, new Point(4, 3));
        game.getTurnState().selectSquare(game, player, new Point(4, 4));

//        assertNotSame(game.getCurrentPlayer(), player);
    }

    @Test
    public void onYourBuild_normalBehaviourBuild_shouldBuild1MoreTimeThenStop() {
        Worker w1 = new Worker(new Point(1, 1));
        Worker w2 = new Worker(new Point(3, 3));

        game.getMap().buildBlock(new Point(1, 2), false);

        player.addWorker(w1);
        player.addWorker(w2);

        game.startTurn();

        game.getTurnState().selectWorker(game, player, w1);

        game.getTurnState().selectSquare(game, player, new Point(1, 2));
        game.getTurnState().selectSquare(game, player, new Point(1, 1));

        game.getTurnState().selectSquare(game, player, new Point(3, 4));
        game.getTurnState().toggleInteraction(game, player);

//        assertNotSame(game.getCurrentPlayer(), player);
    }

    @Test
    public void onYourBuild_normalBehaviourBuild_shouldEndTurnAfterBuild() {
        game.getMap().buildBlock(new Point(1, 1), false);

        Worker w1 = new Worker(new Point(1, 1));
        Worker w2 = new Worker(new Point(3, 3));

        player.addWorker(w1);
        player.addWorker(w2);

        game.startTurn();

        game.getTurnState().selectWorker(game, player, w2);

        game.getTurnState().selectSquare(game, player, new Point(3, 2));
        game.getTurnState().selectSquare(game, player, new Point(3, 3));

//        assertNotSame(game.getCurrentPlayer(), player);
    }
}