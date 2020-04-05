package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.controller.game.Play;
import it.polimi.ingsw.psp1.santorini.controller.turn.*;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Apollo;
import it.polimi.ingsw.psp1.santorini.model.powers.Hestia;
import it.polimi.ingsw.psp1.santorini.model.powers.Mortal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class HestiaTest {

    private Game game;
    private Player player1, player2;

    @Before
    public void setup() {
        this.game = new Game();
        this.player1 = new Player("p1");
        this.player2 = new Player("p2");

        game.addPlayer(player1);
        game.addPlayer(player2);

        player1.setPower(new Hestia(player1));
        player2.setPower(new Mortal(player2));

        player1.setGameState(new Play());
        player1.newTurn(game);
        player2.setGameState(new Play());
        player2.setTurnState(new EndTurn(player2, game));
    }

    @After
    public void teardown() {
        for (int i = player1.getWorkers().size() - 1; i >= 0; i--) {
            player1.removeWorker(player1.getWorkers().get(i));
        }
        for (int i = player2.getWorkers().size() - 1; i >= 0; i--) {
            player2.removeWorker(player2.getWorkers().get(i));
        }
    }

    @Test
    public void getValidMoves_normalBehaviour_shouldNotBuildOnPerimeter() {
        Point oldPosition = new Point(1, 1);
        Point newPosition = new Point(1, 2);

        Worker w1 = new Worker(oldPosition);

        player1.addWorker(w1);

        player1.setSelectedWorker(w1);

        player1.getPower().onYourMove(w1, newPosition, game);
        player1.getPower().onYourBuild(w1, oldPosition, game);

        assertTrue(player1.getTurnState().shouldShowInteraction());
        assertTrue(player1.getTurnState() instanceof Build);
        assertTrue(player1.getPower().getValidMoves(game).stream()
                .noneMatch(point -> game.getMap().isPerimeter(point)));
    }

    @Test
    public void onYourBuild_normalBehaviour_shouldEndBuild() {
        Point oldPosition = new Point(1, 1);
        Point newPosition = new Point(2, 2);
        Worker w1 = new Worker(oldPosition);

        player1.addWorker(w1);
        player1.setSelectedWorker(w1);

        player1.getPower().onYourMove(w1, newPosition, game);
        player1.getPower().onYourBuild(w1, oldPosition, game);

        assertTrue(player1.getTurnState().shouldShowInteraction());

        player1.getTurnState().toggleInteraction();

        assertTrue(player1.getTurnState() instanceof EndTurn);
    }

}
