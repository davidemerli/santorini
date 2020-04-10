package it.polimi.ingsw.psp1.santorini.model.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class WorkerPlacing extends TurnState {

    public WorkerPlacing(Game game) {
        super(game);
    }

    @Override
    public boolean shouldShowInteraction(Player player) {
        return false;
    }

    @Override
    public List<Point> getValidMoves(Player player, Worker worker) {
        return player.getPower().getValidMoves(worker, game);
    }
}
