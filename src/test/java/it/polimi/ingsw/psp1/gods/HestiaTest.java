package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.game.Play;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Hestia;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import it.polimi.ingsw.psp1.santorini.model.turn.EndTurn;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertTrue;

public class HestiaTest {

    private Game game;
    private Player player;

    @Before
    public void setup() {
        this.game = new Game();
        this.player = new Player("p1");

        game.addPlayer(player);

        player.setPower(new Hestia());

        player.setGameState(new Play());
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

        player.addWorker(w);

        game.startTurn();

        game.getTurnState().selectWorker(player, w);
        game.getTurnState().selectSquare(player, newPosition);
        game.getTurnState().selectSquare(player, oldPosition);

        assertTrue(game.getTurnState().shouldShowInteraction(player));
        assertTrue(game.getTurnState() instanceof Build);
        assertTrue(game.getTurnState().getValidMoves(player, w).stream()
                .noneMatch(point -> game.getMap().isPerimeter(point)));
    }

    @Test
    public void onYourBuild_normalBehaviour_shouldEndBuild() {
        Point oldPosition = new Point(1, 1);
        Point newPosition = new Point(2, 2);
        Worker w = new Worker(oldPosition);

        player.addWorker(w);

        game.startTurn();

        game.getTurnState().selectWorker(player, w);
        game.getTurnState().selectSquare(player, newPosition);
        game.getTurnState().selectSquare(player, oldPosition);

        assertTrue(game.getTurnState().shouldShowInteraction(player));

        game.getTurnState().toggleInteraction(player);

        assertTrue(game.getTurnState() instanceof EndTurn);
    }
}