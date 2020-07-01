package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Hestia;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class HestiaTest {

    private Game game;
    private Player player;

    @Before
    public void setup() {
        this.game = new Game("1",2);
        this.player = new Player("p1");

        game.addPlayer(player);

        player.setPower(new Hestia());
    }

    @After
    public void teardown() {
        for (int i = player.getWorkers().size() - 1; i >= 0; i--) {
            player.removeWorker(player.getWorkers().get(i));
        }
    }

    @Test
    public void getValidMoves_normalBehaviour_shouldNotBuildOnPerimeter() {
        Point oldPosition = new Point(1, 1);
        Point newPosition = new Point(1, 2);

        Worker w = new Worker(oldPosition);
        Worker w2 = new Worker(new Point(4,4));

        player.addWorker(w);
        player.addWorker(w2);

        game.startTurn();

        game.getTurnState().selectWorker(game, player, w);
        game.getTurnState().selectSquare(game, player, newPosition);
        game.getTurnState().selectSquare(game, player, oldPosition);

        assertTrue(game.getTurnState().shouldShowInteraction(game, player));
        assertTrue(game.getTurnState() instanceof Build);
        assertTrue(game.getTurnState().getValidMoves(game, player, w).stream()
                .noneMatch(point -> game.getMap().isPerimeter(point)));
    }

    @Test
    public void onYourBuild_normalBehaviour_shouldEndBuild() {
        Point oldPosition = new Point(1, 1);
        Point newPosition = new Point(2, 2);
        Worker w = new Worker(oldPosition);
        Worker w2 = new Worker(new Point(4,4));

        player.addWorker(w);
        player.addWorker(w2);

        game.startTurn();

        game.getTurnState().selectWorker(game, player, w);
        game.getTurnState().selectSquare(game, player, newPosition);
        game.getTurnState().selectSquare(game, player, oldPosition);

        assertTrue(game.getTurnState().shouldShowInteraction(game, player));

        game.getTurnState().toggleInteraction(game, player);

//        assertTrue(game.getTurnState() instanceof EndTurn);
    }
}