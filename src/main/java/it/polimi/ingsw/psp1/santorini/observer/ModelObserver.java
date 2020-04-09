package it.polimi.ingsw.psp1.santorini.observer;

import it.polimi.ingsw.psp1.santorini.model.EnumMoveType;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Map;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;

import java.awt.*;

public interface ModelObserver {

    void mapChange(Map map);

    void playerMove(Player player, EnumMoveType moveType, Worker worker, Point where);

    void playerBuild(Player player, EnumMoveType moveType, Worker worker, Point where);

    void playerWinner(Player player);

    void playerLoser(Player player);

}