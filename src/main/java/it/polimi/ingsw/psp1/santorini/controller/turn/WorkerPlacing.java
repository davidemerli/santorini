package it.polimi.ingsw.psp1.santorini.controller.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class WorkerPlacing extends TurnState {

    public WorkerPlacing(Player player, Game game) {
        super(player, game);
    }

    @Override
    public boolean shouldShowInteraction() {
        return false;
    }

    @Override
    public List<Point> getValidMoves() {
        return game.getMap().getAllSquares().stream()
                .filter(p -> game.getWorkerOn(p).isEmpty())
                .collect(Collectors.toUnmodifiableList());
    }
}
