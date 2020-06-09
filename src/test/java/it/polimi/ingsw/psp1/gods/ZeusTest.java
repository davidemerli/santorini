package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Zeus;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ZeusTest {

    private Game game;
    private Player player;

    @Before
    public void setup() {
        this.game = new Game("1",2);
        this.player = new Player("p1");

        game.addPlayer(player);

        player.setPower(new Zeus());
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

        game.startTurn();

        game.getTurnState().selectWorker(game, player, w);

        game.getTurnState().selectSquare(game, player, new Point(2,2));

        assertTrue(game.getTurnState() instanceof Build);
        assertTrue(game.getTurnState().getValidMoves(game, player, w).contains(position));
    }
}