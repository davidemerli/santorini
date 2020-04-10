package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.game.Play;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Artemis;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import it.polimi.ingsw.psp1.santorini.model.turn.Move;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ArtemisTest {

    private Game game;
    private Player player;

    @Before
    public void setup() {
        this.game = new Game();
        this.player = new Player("p1");

        game.addPlayer(player);

        player.setPower(new Artemis());

        player.setGameState(new Play());
    }

    @After
    public void teardown() {
        for (int i = player.getWorkers().size() - 1; i >= 0; i--) {
            player.removeWorker(player.getWorkers().get(i));
        }
    }

    @Test
    public void onYourMove_normalBehaviour_shouldNotContainOldPosition() {
        Point startPosition = new Point(1, 1);
        Point firstMove = new Point(2, 2);
        Worker w = new Worker(startPosition);

        player.addWorker(w);

        game.startTurn();

        game.getTurnState().selectWorker(player, w);

        assertFalse(game.getTurnState().shouldShowInteraction(player));

        game.getTurnState().selectSquare(player, firstMove);

        assertTrue(game.getTurnState() instanceof Move);
        assertTrue(game.getTurnState().shouldShowInteraction(player));
        assertFalse(game.getTurnState().getValidMoves(player, w).contains(startPosition));
    }

    @Test
    public void onYourMove_normalBehaviour_shouldActivatePower() {
        Point startPosition = new Point(1, 1);
        Point firstMove = new Point(2, 2);
        Worker w = new Worker(startPosition);

        player.addWorker(w);

        game.startTurn();

        game.getTurnState().selectWorker(player, w);
        game.getTurnState().selectSquare(player, firstMove);
        game.getTurnState().toggleInteraction(player);

        assertTrue(game.getTurnState() instanceof Build);
    }
}
