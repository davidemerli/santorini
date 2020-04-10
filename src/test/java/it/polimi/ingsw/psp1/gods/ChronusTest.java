//package it.polimi.ingsw.psp1.gods;
//
//import it.polimi.ingsw.psp1.santorini.model.game.Play;
//import it.polimi.ingsw.psp1.santorini.model.turn.BeginTurn;
//import it.polimi.ingsw.psp1.santorini.model.Game;
//import it.polimi.ingsw.psp1.santorini.model.Player;
//import it.polimi.ingsw.psp1.santorini.model.powers.Chronus;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.awt.*;
//
//import static org.junit.Assert.*;
//
//public class ChronusTest {
//
//    private Game game;
//    private Player player;
//
//    @Before
//    public void setup() {
//        this.game = new Game();
//        this.player = new Player("p1");
//
//        game.addPlayer(player);
//
//        player.setPower(new Chronus(player));
//
//        player.setGameState(new Play());
//        player.newTurn(game);
//    }
//
//    @After
//    public void teardown() {
//        for (int i = player.getWorkers().size() - 1; i >= 0; i--) {
//            player.removeWorker(player.getWorkers().get(i));
//        }
//    }
//
//    @Test
//    public void onBeginTurn_normalBehaviour_shouldWinWithFiveCompleteTowers() {
//        for (int i = 0; i < 5; i++) {
//            for (int j = 0; j < 4; j++) {
//                game.getMap().buildBlock(new Point(0, i), false);
//            }
//        }
//
//        player.setTurnState(new BeginTurn(player, game));
//
//        assertTrue(player.hasWon());
//    }
//}
