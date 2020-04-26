package it.polimi.ingsw.psp1.santorini.model.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class WorkerPlacing extends TurnState {

    public WorkerPlacing(Game game) {
        super(game);
    }

    @Override
    public void init() {
        game.notifyObservers(o -> o.availableMovesUpdate(game.getCurrentPlayer(),
                getValidMoves(game.getCurrentPlayer(), null),
                getBlockedMoves(game.getCurrentPlayer(), null)));

        game.askRequest(game.getCurrentPlayer(), EnumRequestType.PLACE_WORKER);
    }

    @Override
    public boolean shouldShowInteraction(Player player) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void selectSquare(Player player, Point position) {
        if (game.getWorkerOn(position).isPresent()) {
            throw new IllegalArgumentException("Occupied square");
        }

        //TODO: move add worker in game class?
        //TODO: notify move in EnumMoveType?
        player.addWorker(new Worker(position));

        game.notifyObservers(o -> o.playerUpdate(game, player));

        boolean allDone = game.getPlayerList().stream().allMatch(p -> p.getWorkers().size() == 2);

        //if everyone has put down 2 workers, the game can begin with normal turns
        if (allDone) {
            game.nextTurn();
            return;
        }

        if (player.getWorkers().size() == 2) {
            game.shiftPlayers(-1);
        }

        game.setTurnState(new WorkerPlacing(game));
    }

    @Override
    public List<Point> getValidMoves(Player player, Worker worker) {
        return player.getPower().getValidMoves(worker, game);
    }
}
