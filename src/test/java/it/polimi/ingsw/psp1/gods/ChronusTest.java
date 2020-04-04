package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.controller.game.Play;
import it.polimi.ingsw.psp1.santorini.controller.turn.BeginTurn;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Chronus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

public class ChronusTest {

    private Game game;
    private Player player;

    @Before
    public void setup() {
        this.game = new Game();
        this.player = new Player("p1");

        game.addPlayer(player);

        player.setPower(new Chronus(player));

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
    public void onBeginTurn_normalBehaviour_shouldWinWithFiveCompleteTowers() {
        Worker w = new Worker(new Point(1, 1));

        player.addWorker(w);
        player.setSelectedWorker(w);

        // da fixare onBeginTurn in mortal
    }

    private boolean customWinCondition(Game game) {
        return game.getMap().getAllSquares().stream()
                .filter(p -> game.getMap().getLevel(p) == 4)
                .count() >= 5;
    }
}
