package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Hephaestus;
import it.polimi.ingsw.psp1.santorini.model.powers.Mortal;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HephaestusTest {

    private Game game;
    private Player player, player2;

    @Before
    public void setup() {
        this.game = new Game("1",2);
        this.player = new Player("p1");
//        Player player2 = new Player("p2");

        game.addPlayer(player);
//        game.addPlayer(player2);

        player.setPower(new Hephaestus());
//        player2.setPower(new Mortal());
    }

    @After
    public void teardown() {
        for (int i = player.getWorkers().size() - 1; i >= 0; i--) {
            player.removeWorker(player.getWorkers().get(i));
        }
    }

    @Test
    public void onYourBuild_normalBehaviour_shouldBuildOnPreviousPosition() {
        Point firstBuild = new Point(2, 2);
        Worker w = new Worker(new Point(1, 1));
        Worker w2 = new Worker(new Point(4, 4));

        player.addWorker(w);
        player.addWorker(w2);

        game.startTurn();

        game.getTurnState().selectWorker(game, player, w);
        game.getTurnState().selectSquare(game, player, new Point(1, 2));

        assertFalse(game.getTurnState().shouldShowInteraction(game, player));

        game.getTurnState().selectSquare(game, player, firstBuild);

        assertTrue(game.getTurnState().shouldShowInteraction(game, player));
        assertTrue(game.getTurnState() instanceof Build);

        assertEquals(1, game.getTurnState().getValidMoves(game, player, w).size());
        assertTrue(game.getTurnState().getValidMoves(game, player, w).contains(firstBuild));
    }

    @Test
    public void onYourBuild_normalBehaviour_shouldNotRebuildIfDome() {
        Point firstBuild = new Point(2, 2);
        Worker w = new Worker(new Point(1, 1));
        Worker w2 = new Worker(new Point(4, 4));

        player.addWorker(w);
        player.addWorker(w2);

        game.startTurn();

        game.getTurnState().selectWorker(game, player, w);

        for (int i = 0; i < 2; i++) {
            game.getMap().buildBlock(firstBuild, false);
        }

        game.getTurnState().selectSquare(game, player, new Point(1, 2));
        game.getTurnState().selectSquare(game, player, firstBuild);

//        assertNotSame(game.getCurrentPlayer(), player);
    }

    @Test
    public void onYourBuild_normalBehaviour_shouldEndAfterBuild() {
        Point startPosition = new Point(1, 1);
        Point firstBuild = new Point(2, 2);
        Worker w = new Worker(startPosition);
        Worker w2 = new Worker(new Point(4, 4));

        player.addWorker(w);
        player.addWorker(w2);

        game.startTurn();

        game.getTurnState().selectWorker(game, player, w);

        game.getTurnState().selectSquare(game, player, new Point(2, 1));
        game.getTurnState().selectSquare(game, player, firstBuild);

        assertTrue(game.getTurnState().shouldShowInteraction(game, player));
        assertTrue(game.getTurnState() instanceof Build);
        assertTrue(game.getTurnState().getValidMoves(game, player, w).contains(firstBuild));

        game.getTurnState().toggleInteraction(game, player);

//        assertTrue(game.getTurnState() instanceof EndTurn);
    }
}