package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Prometheus;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import it.polimi.ingsw.psp1.santorini.model.turn.Move;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PrometheusTest {

    private Game game;
    private Player player;

    @Before
    public void setup() {
        this.game = new Game(2);
        this.player = new Player("p1");

        game.addPlayer(player);

        player.setPower(new Prometheus());
    }

    @After
    public void teardown() {
        for (int i = player.getWorkers().size() - 1; i >= 0; i--) {
            player.removeWorker(player.getWorkers().get(i));
        }
    }

    @Test
    public void onYourBuild_normalBehaviour_shouldEndBuild() {
        Point position = new Point(1, 1);
        Worker w = new Worker(position);

        player.addWorker(w);

        game.startTurn();

        game.getTurnState().selectWorker(player, w);

        assertTrue(game.getTurnState().shouldShowInteraction(player));

        game.getTurnState().toggleInteraction(player);

        assertTrue(game.getTurnState() instanceof Move);
    }

    @Test
    public void onYourBuild_normalBehaviour_shouldStopPlayerFromGoingUpIfBuilt() {
        Point oldPosition = new Point(1, 1);
        Point newPosition = new Point(2, 2);
        Point blockedPosition = new Point(2, 1);
        Worker w = new Worker(oldPosition);

        player.addWorker(w);

        game.startTurn();

        game.getTurnState().selectWorker(player, w);

        assertTrue(game.getTurnState().shouldShowInteraction(player));

        game.getMap().buildBlock(blockedPosition, false);

        assertTrue(game.getTurnState() instanceof Build);

        game.getTurnState().selectSquare(player, newPosition);

        assertTrue(game.getTurnState() instanceof Move);

        assertFalse(game.getTurnState().getValidMoves(player, w).contains(blockedPosition));
    }
}