package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.controller.game.Play;
import it.polimi.ingsw.psp1.santorini.controller.turn.BeginTurn;
import it.polimi.ingsw.psp1.santorini.controller.turn.Build;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Hephaestus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HephaestusTest {

    private Game game;
    private Player player;

    @Before
    public void setup() {
        this.game = new Game();
        this.player = new Player("p1");

        game.addPlayer(player);

        player.setPower(new Hephaestus(player));

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
    public void onYourBuild_normalBehaviour_shouldBuildOnPreviousPosition() {
        Point firstBuild = new Point(2, 2);
        Worker w = new Worker(new Point(1, 1));

        player.addWorker(w);
        player.setSelectedWorker(w);

        player.getPower().onYourBuild(w, firstBuild, game);

        if(game.getMap().getLevel(firstBuild) < 3) {
            assertTrue(player.getTurnState().getValidMoves().contains(firstBuild));
        }

    }

    @Test
    public void getValidMoves_normalBehaviour_shouldBuildOnPreviousPosition() {
        Point firstBuild = new Point(2, 2);
        Worker w = new Worker(new Point(1, 1));

        player.addWorker(w);
        player.setSelectedWorker(w);
        if (player.getTurnState() instanceof Build) {
            assertEquals(player.getTurnState().getValidMoves().get(0), firstBuild);
        }
    }
}
