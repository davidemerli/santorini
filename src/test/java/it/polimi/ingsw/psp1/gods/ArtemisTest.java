package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.controller.game.Play;
import it.polimi.ingsw.psp1.santorini.controller.turn.BeginTurn;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Artemis;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.List;

import static org.junit.Assert.assertFalse;

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
        player.setTurnState(new BeginTurn(player, game));
    }

    @After
    public void teardown() {
        for (int i = player.getWorkers().size() - 1; i >= 0; i--) {
            player.removeWorker(player.getWorkers().get(i));
        }
    }

    @Test
    public void onYourMove_normalBehaviour_shouldNotContainOldPosition() {
        List<Point> list;
        Point startPosition = new Point(1, 1);
        Point firstMove = new Point(2, 2);
        Point secondMove = new Point(3, 3);
        Worker w = new Worker(startPosition);

        player.addWorker(w);
        player.setSelectedWorker(w);

        player.getPower().onYourMove(w, firstMove, game);
        player.getPower().onYourMove(w, secondMove, game);

        assertFalse(player.getTurnState().getValidMoves().contains(startPosition));
    }

    @Test
    public void getValidMoves_normalBehaviour_shouldRemoveOldPosition() {
        Point nextPosition = new Point(2, 2);
        Point startPosition = new Point(1, 1);
        Worker w = new Worker(startPosition);

        player.addWorker(w);
        player.setSelectedWorker(w);

        assertFalse(player.getTurnState().getValidMoves().contains(startPosition));
    }

}
