package it.polimi.ingsw.psp1.santorini.observer;

import it.polimi.ingsw.psp1.santorini.model.EnumMoveType;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;

import java.awt.*;
import java.util.List;
import java.util.Map;

public interface ModelObserver {

    void playerMove(Player player, EnumMoveType moveType, Worker worker, Point from, Point where);

    void playerBuild(Player player, EnumMoveType moveType, Worker worker, Point where);

    void playerUpdate(Game game, Player player);

    void gameUpdate(Game game);

    void availableMovesUpdate(Player player, List<Point> validMoves, Map<Power, List<Point>> blockedMoves);

    void requestToPlayer(Player player, EnumRequestType requestType);

    void sendPowerList(Player player, List<Power> availablePowers);
}