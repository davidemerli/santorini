package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.model.game.Play;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import it.polimi.ingsw.psp1.santorini.model.turn.EndTurn;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Poseidon;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class PoseidonTest {

    private Game game;
    private Player player;

    @Before
    public void setup() {
        this.game = new Game();
        this.player = new Player("p1");

        game.addPlayer(player);

        player.setPower(new Poseidon(player));

        player.setGameState(new Play());
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

        player.setSelectedWorker(w1);

        assertFalse(game.getTurnState().shouldShowInteraction(player));

        game.getTurnState().selectSquare(player, new Point(1,2));

        assertFalse(game.getTurnState().shouldShowInteraction(player));

        game.getTurnState().selectSquare(player, new Point(1,1));

        assertTrue(game.getTurnState() instanceof Build);
        assertTrue(game.getTurnState().shouldShowInteraction(player));
        assertTrue(player.isWorkerLocked());
        assertEquals(player.getSelectedWorker(), w2);

        assertTrue(game.getTurnState().getValidMoves(player, w1).containsAll(game.getMap().getNeighbors(w2.getPosition())));

        player.setSelectedWorker();

        game.getTurnState().selectSquare(player, new Point(3,4));
        game.getTurnState().selectSquare(player, new Point(4,3));
        game.getTurnState().selectSquare(player, new Point(4,4));

        assertTrue(game.getTurnState() instanceof EndTurn);
    }

    @Test
    public void onYourBuild_normalBehaviourBuild_shouldBuild1MoreTimeThenStop() {
        Worker w1 = new Worker(new Point(1, 1));
        Worker w2 = new Worker(new Point(3, 3));

        game.getMap().buildBlock(new Point(1, 2), false);

        player.addWorker(w1);
        player.addWorker(w2);

        game.startTurn();

        player.setSelectedWorker(w1);

        game.getTurnState().selectSquare(player, new Point(1,2));
        game.getTurnState().selectSquare(player, new Point(1,1));

        game.getTurnState().selectSquare(player, new Point(3,4));
        game.getTurnState().toggleInteraction(player);

        assertTrue(game.getTurnState() instanceof EndTurn);
    }

    @Test
    public void onYourBuild_normalBehaviourBuild_shouldEndTurnAfterBuild() {
        game.getMap().buildBlock(new Point(1, 1), false);

        Worker w1 = new Worker(new Point(1, 1));
        Worker w2 = new Worker(new Point(3, 3));

        player.addWorker(w1);
        player.addWorker(w2);

        game.startTurn();

        player.setSelectedWorker(w2);

        game.getTurnState().selectSquare(player, new Point(1,2));
        game.getTurnState().selectSquare(player, new Point(1,1));

        assertTrue(player.getTurnState() instanceof EndTurn);
    }
}