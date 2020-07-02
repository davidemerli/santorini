package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Atlas;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AtlasTest {

    private Game game;
    private Player player;

    @Before
    public void setup() {
        this.game = new Game("1",2);
        this.player = new Player("p1");

        game.addPlayer(player);

        player.setPower(new Atlas());
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
        Worker w2 = new Worker(new Point(4, 4));
        Point position = new Point(2, 2);

        player.addWorker(w);
        player.addWorker(w2);

        game.startTurn();

        game.getTurnState().selectWorker(game, player, w);

        assertFalse(game.getTurnState().shouldShowInteraction(game, player));

        game.getTurnState().selectSquare(game, player, new Point(1, 2));

        assertTrue(game.getTurnState().shouldShowInteraction(game, player));

        game.getTurnState().toggleInteraction(game, player);
        game.getTurnState().selectSquare(game, player, position);

        assertTrue(game.getMap().hasDome(position));
    }
}

