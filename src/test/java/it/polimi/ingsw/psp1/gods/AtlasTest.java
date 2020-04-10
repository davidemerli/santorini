//package it.polimi.ingsw.psp1.gods;
//
//import it.polimi.ingsw.psp1.santorini.model.game.Play;
//import it.polimi.ingsw.psp1.santorini.model.Game;
//import it.polimi.ingsw.psp1.santorini.model.Player;
//import it.polimi.ingsw.psp1.santorini.model.map.Worker;
//import it.polimi.ingsw.psp1.santorini.model.powers.Atlas;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.awt.*;
//
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
//public class AtlasTest {
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
//        player.setPower(new Atlas(player));
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
//    public void onYourBuild_normalBehaviourBuild_shouldBuildDome() {
//        Worker w = new Worker(new Point(1, 1));
//        Point position = new Point(2, 2);
//
//        player.addWorker(w);
//        player.setSelectedWorker(w);
//
//        assertFalse(player.getTurnState().shouldShowInteraction());
//
//        player.getPower().onMove(w, new Point(1, 2), game);
//
//        assertTrue(player.getTurnState().shouldShowInteraction());
//
//        player.getTurnState().toggleInteraction();
//        player.getPower().onBuild(w, position, game);
//
//        assertTrue(game.getMap().hasDome(position));
//    }
//
//}
