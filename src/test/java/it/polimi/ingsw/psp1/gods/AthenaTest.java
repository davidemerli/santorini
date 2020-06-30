package it.polimi.ingsw.psp1.gods;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Athena;
import it.polimi.ingsw.psp1.santorini.model.powers.Mortal;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class AthenaTest {

    private Game game;
    private Player player1, player2;

    @Before
    public void setup() {
        this.game = new Game("1", 2);
        this.player1 = new Player("p1");
        this.player2 = new Player("p2");

        game.addPlayer(player1);
        game.addPlayer(player2);

        player1.setPower(new Athena());
        player2.setPower(new Mortal());
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
    public void onYourMove_normalBehaviour_secondWorkerHasABlockedMove() {
        Point newPosition = new Point(0, 0);
        Point blockedPosition = new Point(2, 3);
        Worker w1 = new Worker(new Point(1, 1));
        Worker w2 = new Worker(new Point(2, 2));

        game.getMap().buildBlock(newPosition, false);
        game.getMap().buildBlock(blockedPosition, false);

        player1.addWorker(w1);
        player2.addWorker(w2);

        game.startTurn();

        game.getTurnState().selectWorker(game, player1, w1);
        game.getTurnState().selectSquare(game, player1, newPosition);

        game.getTurnState().selectSquare(game, player1, new Point(0, 1));

        while (!game.getCurrentPlayer().equals(player2)) ;

        game.getTurnState().selectWorker(game, player2, w2);

        assertTrue(game.getTurnState().isPositionBlocked(game,
                game.getTurnState().getBlockedMoves(game, player2, player2.getSelectedWorker().get()),
                blockedPosition));
    }

    @Test
    public void onBeginTurn_normalBehaviour_shouldLose() {
        Worker w1 = new Worker(new Point(2, 2));
        Worker w2 = new Worker(new Point(0, 0));
        Point newPosition = (new Point(2, 3));

        game.getMap().buildBlock(new Point(1, 0), true);
        game.getMap().buildBlock(new Point(1, 1), true);
        game.getMap().buildBlock(new Point(0, 1), false);
        game.getMap().buildBlock(newPosition, false);


        player1.addWorker(w1);
        player2.addWorker(w2);

        game.startTurn();

        game.getTurnState().selectWorker(game, player1, w1);
        game.getTurnState().selectSquare(game, player1, newPosition);
        game.getTurnState().selectSquare(game, player1, new Point (3,3));

        while (!game.getCurrentPlayer().equals(player2));

        game.getTurnState().selectWorker(game, player2, w2);

        List<Point> validMoves = game.getTurnState().getValidMoves(game, player2, w2);
        Map<Power, List<Point>> blockedMoves = game.getTurnState().getBlockedMoves(game, player2, w2);

        assertTrue(validMoves.stream().allMatch(p -> game.getTurnState().isPositionBlocked(game, blockedMoves, p)));
        assertTrue(player2.hasLost());
    }
}
