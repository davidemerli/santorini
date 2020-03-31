package it.polimi.ingsw.psp1.santorini.powers;

import it.polimi.ingsw.psp1.santorini.Game;
import it.polimi.ingsw.psp1.santorini.map.SquareData;
import it.polimi.ingsw.psp1.santorini.map.Worker;
import it.polimi.ingsw.psp1.santorini.player.Player;
import it.polimi.ingsw.psp1.santorini.player.turn.TurnState;

import java.awt.*;

public class Chronus extends Mortal {

    public Chronus(Player player) {
        super(player);
    }

    @Override
    public void onBeginTurn(Game game) {
        if (numberOfTowers(game) == 5) {
            player.setWinner();
        } else {
            super.onBeginTurn(game);
        }
    }

    private int numberOfTowers(Game game) {
        SquareData[][] map = game.getGameMap().getBlockMatrix();
        int completed = 0;

        for (int i = 0; i < game.getGameMap().SIDE_LENGTH; i++) {
            for (int j = 0; j < game.getGameMap().SIDE_LENGTH; j++) {
                if (map[i][j].getLevel() == 4) {
                    completed++;
                }
            }
        }

        return completed;
    }
}