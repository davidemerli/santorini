package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Triton;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import it.polimi.ingsw.psp1.santorini.model.turn.Move;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TritonTest {

    private Game game;
    private Player player;

    @Before
    public void setup() {
        this.game = new Game("1",2);
        this.player = new Player("p1");

        game.addPlayer(player);

        player.setPower(new Triton());
    }

    @After
    public void teardown() {
        for (int i = player.getWorkers().size() - 1; i >= 0; i--) {
            player.removeWorker(player.getWorkers().get(i));
        }
    }

    @Test
    public void onYourMove_normalBehaviour_shouldMoveAgain() {
        Point startPosition = new Point(1, 1);
        Point firstMove = new Point(0, 0);
        Worker w = new Worker(startPosition);

        player.addWorker(w);

        game.startTurn();

        game.getTurnState().selectWorker(game, player, w);

        game.getTurnState().selectSquare(game, player, firstMove);

        assertTrue(game.getTurnState() instanceof Move);

        game.getTurnState().selectSquare(game, player, startPosition);

        assertTrue(game.getTurnState() instanceof Build);
    }

    @Test
    public void onYourMove_normalBehaviour_shouldEndMove() {
        Point startPosition = new Point(1, 1);
        Point firstMove = new Point(0, 0);
        Worker w = new Worker(startPosition);

        player.addWorker(w);

        game.startTurn();

        game.getTurnState().selectWorker(game, player, w);

        assertFalse(game.getTurnState().shouldShowInteraction(game, player));

        game.getTurnState().selectSquare(game, player, firstMove);

        assertTrue(game.getTurnState().shouldShowInteraction(game, player));

        game.getTurnState().toggleInteraction(game, player);

        assertTrue(game.getTurnState() instanceof Build);
        assertFalse(game.getTurnState().shouldShowInteraction(game, player));
    }
}