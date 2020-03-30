package it.polimi.ingsw.PSP1.santorini.powers;

import it.polimi.ingsw.PSP1.santorini.Game;
import it.polimi.ingsw.PSP1.santorini.map.Worker;
import it.polimi.ingsw.PSP1.santorini.player.Player;
import it.polimi.ingsw.PSP1.santorini.player.turn.TurnState;

import java.awt.*;
import java.util.List;

public interface Power {

    void onBeginTurn(Game game);

    void onEndTurn(Game game);

    TurnState onYourBuild(Worker worker, Point where, Game game);

    TurnState onYourMove(Worker worker, Point where, Game game);

    void onOpponentsBuild(Player player, Worker worker, Point where, Game game);

    void onOpponentsMove(Player player, Worker worker, Point oldPosition, Point where, Game game);

    void onToggleInteraction(Game game);

    boolean shouldShowInteraction();

    public List<Point> getBlockedMoves(Worker worker, List<Point> validMoves, TurnState playerState, Game game);

    List<Point> getValidMoves(Game game);
}
