package it.polimi.ingsw.PSP1.santorini.powers;

import it.polimi.ingsw.PSP1.santorini.Game;
import it.polimi.ingsw.PSP1.santorini.map.SquareData;
import it.polimi.ingsw.PSP1.santorini.map.Worker;
import it.polimi.ingsw.PSP1.santorini.player.Player;
import it.polimi.ingsw.PSP1.santorini.player.turn.Build;
import it.polimi.ingsw.PSP1.santorini.player.turn.EndTurn;
import it.polimi.ingsw.PSP1.santorini.player.turn.Move;
import it.polimi.ingsw.PSP1.santorini.player.turn.TurnState;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Mortal implements Power {

    protected final Player player;

    public Mortal(Player player) {
        this.player = player;
    }

    @Override
    public TurnState onYourBuild(Worker worker, Point where, Game game) {
        SquareData squareData = game.getGameMap().getSquareDataAt(where);
        boolean shouldBuildDome = squareData.getLevel() == 3;

        game.buildBlock(where, shouldBuildDome);

        return new EndTurn(player, game);
    }

    @Override
    public TurnState onYourMove(Worker worker, Point where, Game game) {
        game.moveWorker(worker, where);

        player.setSelectedWorker(worker);
        player.lockWorker();

        return new Build(player, game);
    }

    @Override
    public boolean shouldShowInteraction() {
        return false;
    }

    @Override
    public List<Point> getBlockedMoves(Game game) {
        return Collections.emptyList();
    }

    /* getSelectedWorker is always != null */
    @Override
    public List<Point> getValidMoves(Game game) {
        Point workerPosition = player.getSelectedWorker().getPosition();

        int currentLevel = getLevelAtPosition(game, workerPosition);

        List<Point> neighbors = game.getGameMap().getNeighbors(workerPosition);

        Predicate<Point> domeCheck = p -> !game.getGameMap().getSquareDataAt(p).isDome();

        Predicate<Point> canMoveTo = p -> getLevelAtPosition(game, p) <= currentLevel ||
                getLevelAtPosition(game, p) == currentLevel + 1;

        Predicate<Point> workerCheck = p -> !game.getGameMap().isWorkerOn(p);

        if (player.getTurnState() instanceof Move) {
            return neighbors.stream()
                    .filter(domeCheck.and(canMoveTo).and(workerCheck))
                    .collect(Collectors.toList());
        } else if (player.getTurnState() instanceof Build) {
            return neighbors.stream()
                    .filter(domeCheck.and(workerCheck))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    protected int getLevelAtPosition(Game game, Point point) {
        return game.getGameMap().getSquareDataAt(point).getLevel();
    }

    @Override
    public void onBeginTurn(Game game) {
    }

    @Override
    public void onEndTurn(Game game) {
    }

    @Override
    public void onOpponentsBuild(Player player, Worker worker, Point where, Game game) {
    }

    @Override
    public void onOpponentsMove(Player player, Worker worker, Point oldPosition, Point where, Game game) {
    }

    @Override
    public void onToggleInteraction(Game game) {
    }
}
