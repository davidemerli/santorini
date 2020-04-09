package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.model.game.Play;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import it.polimi.ingsw.psp1.santorini.model.turn.Move;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Artemis;
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

        player.setPower(new Artemis(player));

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
    public void onYourMove_normalBehaviour_shouldNotContainOldPosition() {
        Point startPosition = new Point(1, 1);
        Point firstMove = new Point(2, 2);
        Worker w = new Worker(startPosition);

        player.addWorker(w);
        player.setSelectedWorker(w);

        assertFalse(player.getTurnState().shouldShowInteraction());

        player.getPower().onYourMove(w, firstMove, game);

        assertTrue(player.getTurnState() instanceof Move);
        assertTrue(player.getTurnState().shouldShowInteraction());
        assertFalse(player.getTurnState().getValidMoves().contains(startPosition));
    }

    @Test
    public void onYourMove_normalBehaviour_shouldActivatePower() {
        Point startPosition = new Point(1, 1);
        Point firstMove = new Point(2, 2);
        Worker w = new Worker(startPosition);

        player.addWorker(w);
        player.setSelectedWorker(w);

        player.getPower().onYourMove(w, firstMove, game);
        player.getTurnState().toggleInteraction();

        assertTrue(player.getTurnState() instanceof Build);
    }
}
