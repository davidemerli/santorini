package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.controller.game.Play;
import it.polimi.ingsw.psp1.santorini.controller.turn.BeginTurn;
import it.polimi.ingsw.psp1.santorini.controller.turn.Build;
import it.polimi.ingsw.psp1.santorini.controller.turn.EndTurn;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Demeter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DemeterTest {

    private Game game;
    private Player player;

    @Before
    public void setup() {
        this.game = new Game();
        this.player = new Player("p1");

        game.addPlayer(player);

        player.setPower(new Demeter(player));

        player.setGameState(new Play());
        player.setTurnState(new BeginTurn(player, game));
    }

    @After
    public void teardown() {
        for (int i = player.getWorkers().size() - 1; i >= 0; i--) {
            player.removeWorker(player.getWorkers().get(i));
        }
    }

    @Test
    public void onYourBuild_normalBehaviour_shouldBuildNotInPreviousBlock() {
        Point startPosition = new Point(1, 1);
        Point firstBuild = new Point(2, 2);
        Worker w = new Worker(startPosition);

        player.addWorker(w);
        player.setSelectedWorker(w);

        player.getPower().onYourMove(w, new Point(2,1), game);

        assertFalse(player.getTurnState().shouldShowInteraction());

        player.getPower().onYourBuild(w, firstBuild, game);

        assertTrue(player.getTurnState().shouldShowInteraction());
        assertTrue(player.getTurnState() instanceof Build);
        assertFalse(player.getTurnState().getValidMoves().contains(firstBuild));
    }

    @Test
    public void onYourBuild_normalBehaviour_shouldEndAfterBuild() {
        Point startPosition = new Point(1, 1);
        Point firstBuild = new Point(2, 2);
        Worker w = new Worker(startPosition);

        player.addWorker(w);
        player.setSelectedWorker(w);

        player.getPower().onYourMove(w, new Point(2,1), game);
        player.getPower().onYourBuild(w, firstBuild, game);

        assertTrue(player.getTurnState().shouldShowInteraction());
        assertTrue(player.getTurnState() instanceof Build);
        assertFalse(player.getTurnState().getValidMoves().contains(firstBuild));

        player.getTurnState().toggleInteraction();

        assertTrue(player.getTurnState() instanceof EndTurn);

    }
}
