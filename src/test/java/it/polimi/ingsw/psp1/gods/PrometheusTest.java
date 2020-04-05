package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.controller.game.Play;
import it.polimi.ingsw.psp1.santorini.controller.turn.BeginTurn;
import it.polimi.ingsw.psp1.santorini.controller.turn.Build;
import it.polimi.ingsw.psp1.santorini.controller.turn.EndTurn;
import it.polimi.ingsw.psp1.santorini.controller.turn.Move;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Apollo;
import it.polimi.ingsw.psp1.santorini.model.powers.Mortal;
import it.polimi.ingsw.psp1.santorini.model.powers.Prometheus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class PrometheusTest {

    private Game game;
    private Player player1, player2;

    @Before
    public void setup() {
        this.game = new Game();
        this.player1 = new Player("p1");
        this.player2 = new Player("p2");

        game.addPlayer(player1);
        game.addPlayer(player2);

        player1.setPower(new Prometheus(player1));
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
    public void onYourBuild_normalBehaviour_shouldEndBuild() {
        Point position = new Point(1, 1);
        Worker w1 = new Worker(position);

        player1.addWorker(w1);
        player1.setSelectedWorker(w1);

        assertTrue(player1.getTurnState().shouldShowInteraction());

        player1.getTurnState().toggleInteraction();

        assertTrue(player1.getTurnState() instanceof Move);
    }

    @Test
    public void onYourBuild_normalBehaviour_shouldStopPlayerFromGoingUpIfBuilt() {
        Point oldPosition = new Point(1, 1);
        Point newPosition = new Point(2, 2);
        Point blockedPosition = new Point(2, 1);
        Worker w1 = new Worker(oldPosition);

        player1.addWorker(w1);
        player1.setSelectedWorker(w1);

        assertTrue(player1.getTurnState().shouldShowInteraction());

        game.getMap().buildBlock(blockedPosition, false);

        assertTrue(player1.getTurnState() instanceof Build);
        player1.getPower().onYourBuild(w1, newPosition, game);
        assertTrue(player1.getTurnState() instanceof Move);

        assertFalse(player1.getTurnState().getValidMoves().contains(blockedPosition));
    }
}
