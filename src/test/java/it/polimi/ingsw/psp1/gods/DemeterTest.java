package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Demeter;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DemeterTest {

    private Game game;
    private Player player;

    @Before
    public void setup() {
        this.game = new Game(1,2);
        this.player = new Player("p1");

        game.addPlayer(player);

        player.setPower(new Demeter());
    }

    @After
    public void teardown() {
        for (int i = player.getWorkers().size() - 1; i >= 0; i--) {
            player.removeWorker(player.getWorkers().get(i));
        }
    }

    @Test
    public void onYourBuild_normalBehaviour_shouldBuildNotInPreviousBlock() {
        Point startPosition = new Point(1, 1);
        Point firstBuild = new Point(2, 2);
        Worker w = new Worker(startPosition);

        player.addWorker(w);

        game.startTurn();

        game.getTurnState().selectWorker(player, w);
        game.getTurnState().selectSquare(player, new Point(2, 1));

        assertFalse(game.getTurnState().shouldShowInteraction(player));

        game.getTurnState().selectSquare(player, firstBuild);

        assertTrue(game.getTurnState().shouldShowInteraction(player));
        assertTrue(game.getTurnState() instanceof Build);
        assertFalse(game.getTurnState().getValidMoves(player, w).contains(firstBuild));
    }

    @Test
    public void onYourBuild_normalBehaviour_shouldEndAfterBuild() {
        Point startPosition = new Point(1, 1);
        Point firstBuild = new Point(2, 2);
        Worker w = new Worker(startPosition);

        player.addWorker(w);

        game.startTurn();

        game.getTurnState().selectWorker(player, w);
        game.getTurnState().selectSquare(player, new Point(2, 1));
        game.getTurnState().selectSquare(player, firstBuild);

        assertTrue(game.getTurnState().shouldShowInteraction(player));
        assertTrue(game.getTurnState() instanceof Build);
        assertFalse(game.getTurnState().getValidMoves(player, w).contains(firstBuild));

        game.getTurnState().toggleInteraction(player);

//        assertTrue(game.getTurnState() instanceof EndTurn);
    }
}
