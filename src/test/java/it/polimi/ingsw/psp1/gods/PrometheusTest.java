package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Prometheus;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import it.polimi.ingsw.psp1.santorini.model.turn.Move;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PrometheusTest {

    private Game game;
    private Player player;

    @Before
    public void setup() {
        this.game = new Game("1",2);
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
    public void onYourMove_toggleInteraction_shouldGoBuild() {
        Point position = new Point(1, 1);

        Worker w = new Worker(position);
        Worker w2 = new Worker(new Point(4,4));

        player.addWorker(w);
        player.addWorker(w2);

        game.startTurn();

        game.getTurnState().selectWorker(game, player, w);

        assertTrue(game.getTurnState().shouldShowInteraction(game, player));

        game.getTurnState().toggleInteraction(game, player);

        assertTrue(game.getTurnState() instanceof Build);
    }

    @Test
    public void onYourBuild_normalBehaviour_shouldStopPlayerFromGoingUpIfBuilt() {
        Point oldPosition = new Point(1, 1);
        Point newPosition = new Point(2, 2);
        Point blockedPosition = new Point(2, 1);

        Worker w = new Worker(oldPosition);
        Worker w2 = new Worker(new Point(4,4));

        game.getMap().buildBlock(blockedPosition, false);

        player.addWorker(w);
        player.addWorker(w2);

        game.startTurn();

        game.getTurnState().selectWorker(game, player, w);

        assertTrue(game.getTurnState().shouldShowInteraction(game, player));

        game.getTurnState().toggleInteraction(game, player);

        assertTrue(game.getTurnState() instanceof Build);

        game.getTurnState().selectSquare(game, player, newPosition);

        assertTrue(game.getTurnState() instanceof Move);

        assertFalse(game.getTurnState().getValidMoves(game, player, w).contains(blockedPosition));

        game.getTurnState().selectSquare(game, player, new Point(0, 0));

        assertTrue(game.getTurnState() instanceof Build);

        game.getTurnState().selectSquare(game, player, oldPosition);

        assertEquals(1, game.getMap().getLevel(oldPosition));
    }
}