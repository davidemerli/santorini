package it.polimi.ingsw.psp1.santorini.observer;

import it.polimi.ingsw.psp1.santorini.model.EnumMoveType;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;

import java.awt.*;

public interface ModelObserver {

    void mapChange(GameMap map);

    void playerMove(Player player, EnumMoveType moveType, Worker worker, Point where);

    void playerBuild(Player player, EnumMoveType moveType, Worker worker, Point where);

    void playerWinner(Player player);

    void playerLoser(Player player);

}