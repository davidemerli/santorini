package it.polimi.ingsw.psp1.santorini.model.turn;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;

import java.util.List;

public class WorkerPlacing extends TurnState {

    @Override
    public void init(Game game) {
        super.init(game);

        game.notifyObservers(o -> o.availableMovesUpdate(game.getCurrentPlayer(),
                getValidMoves(game, game.getCurrentPlayer(), null),
                getBlockedMoves(game, game.getCurrentPlayer(), null)));

        game.askRequest(game.getCurrentPlayer(), EnumRequestType.PLACE_WORKER);

        game.saveState();
    }

    @Override
    public boolean shouldShowInteraction(Game game, Player player) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void selectSquare(Game game, Player player, Point position) {
        if (game.getWorkerOn(position).isPresent()) {
            throw new IllegalArgumentException("Occupied square");
        }

        game.addWorker(player, position);

        boolean allDone = game.getPlayerList().stream().allMatch(p -> p.getWorkers().size() == 2);

        //if everyone has put down 2 workers, the game can begin with normal turns
        if (allDone) {
            game.nextTurn();
            return;
        }

        if (player.getWorkers().size() == 2) {
            game.shiftPlayers(-1);
        }

        game.setTurnState(new WorkerPlacing());
    }

    @Override
    public List<Point> getValidMoves(Game game, Player player, Worker worker) {
        return player.getPower().getValidMoves(worker, game);
    }

    @Override
    public void undo(Game game, Player player) {
        game.restoreSavedState();

        this.init(game);
    }
}
