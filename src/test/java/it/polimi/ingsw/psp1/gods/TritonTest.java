package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.controller.game.Play;
import it.polimi.ingsw.psp1.santorini.controller.turn.BeginTurn;
import it.polimi.ingsw.psp1.santorini.controller.turn.Build;
import it.polimi.ingsw.psp1.santorini.controller.turn.Move;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Triton;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TritonTest {

    private Game game;
    private Player player;

    @Before
    public void setup() {
        this.game = new Game();
        this.player = new Player("p1");

        game.addPlayer(player);

        player.setPower(new Triton(player));

        player.setGameState(new Play());
        player.newTurn(game);
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
        player.setSelectedWorker(w);

        player.getPower().onYourMove(w, firstMove, game);

        assertTrue(player.getTurnState() instanceof Move);
        player.getPower().onYourMove(w, startPosition, game);
        assertTrue(player.getTurnState() instanceof Build);
    }

    @Test
    public void onYourMove_normalBehaviour_shouldEndMove() {
        Point startPosition = new Point(1, 1);
        Point firstMove = new Point(0, 0);
        Worker w = new Worker(startPosition);

        player.addWorker(w);
        player.setSelectedWorker(w);

        assertFalse(player.getTurnState().shouldShowInteraction());

        player.getPower().onYourMove(w, firstMove, game);

        assertTrue(player.getTurnState().shouldShowInteraction());

        player.getTurnState().toggleInteraction();

        assertTrue(player.getTurnState() instanceof Build);
        assertFalse(player.getTurnState().shouldShowInteraction());
    }
}
