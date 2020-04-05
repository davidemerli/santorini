package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.controller.game.Play;
import it.polimi.ingsw.psp1.santorini.controller.turn.BeginTurn;
import it.polimi.ingsw.psp1.santorini.controller.turn.Build;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Zeus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ZeusTest {

    private Game game;
    private Player player;

    @Before
    public void setup() {
        this.game = new Game();
        this.player = new Player("p1");

        game.addPlayer(player);

        player.setPower(new Zeus(player));

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
    public void getValidMoves_normalBehaviour_shouldBuildBelow() {
        Point position = new Point(1, 1);
        Worker w = new Worker(position);

        player.addWorker(w);
        player.setSelectedWorker(w);

        player.getPower().onYourMove(w, new Point(2, 2), game);

        assertTrue(player.getTurnState() instanceof Build);
        assertTrue(player.getTurnState().getValidMoves().contains(position));
    }
}
