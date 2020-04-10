package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.game.Play;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Atlas;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AtlasTest {

    private Game game;
    private Player player;

    @Before
    public void setup() {
        this.game = new Game();
        this.player = new Player("p1");

        game.addPlayer(player);

        player.setPower(new Atlas());

        player.setGameState(new Play());
    }

    @After
    public void teardown() {
        for (int i = player.getWorkers().size() - 1; i >= 0; i--) {
            player.removeWorker(player.getWorkers().get(i));
        }
    }

    @Test
    public void onYourBuild_normalBehaviourBuild_shouldBuildDome() {
        Worker w = new Worker(new Point(1, 1));
        Point position = new Point(2, 2);

        player.addWorker(w);

        game.startTurn();

        player.setSelectedWorker(w);

        assertFalse(game.getTurnState().shouldShowInteraction(player));

        game.getTurnState().selectSquare(player, new Point(1, 2));

        assertTrue(game.getTurnState().shouldShowInteraction(player));

        game.getTurnState().toggleInteraction(player);
        game.getTurnState().selectSquare(player, position);

        assertTrue(game.getMap().hasDome(position));
    }
}

