package it.polimi.ingsw.psp1.santorini.controller;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.observer.ViewObserver;
import it.polimi.ingsw.psp1.santorini.view.View;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class Controller implements ViewObserver {

    private final Game model;

    public Controller(Game model) {
        this.model = model;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Used to select a square on the map
     *
     * @param view     the view where the even is coming from
     * @param player   the player associated with the view
     * @param location of the square
     * @throws UnsupportedOperationException  if operation is not valid
     * @throws ArrayIndexOutOfBoundsException if point is out of map
     * @throws IllegalArgumentException       if at least one argument is not valid
     */
    @Override
    public void selectSquare(View view, Player player, Point location) {
        player = getGamePlayerInstance(player);

        try {
            if (!player.equals(model.getCurrentPlayer())) {
                view.notifyError("Not your turn");
                return;
            }

            model.getTurnState().selectSquare(model, player, location);
        } catch (UnsupportedOperationException | ArrayIndexOutOfBoundsException |
                IllegalArgumentException ex) {
            view.notifyError(ex.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Used to select worker on the map
     *
     * @param view           the view where the even is coming from
     * @param player         the player associated with the view
     * @param workerPosition on the map
     * @throws UnsupportedOperationException
     * @throws ArrayIndexOutOfBoundsException
     * @throws IllegalArgumentException
     * @throws NoSuchElementException
     */
    @Override
    public void selectWorker(View view, Player player, Point workerPosition) {
        player = getGamePlayerInstance(player);

        try {
            if (!player.equals(model.getCurrentPlayer())) {
                view.notifyError("Not your turn");
                return;
            }

            Optional<Worker> worker = model.getWorkerOn(workerPosition);

            if (worker.isEmpty()) {
                view.notifyError("There is no worker at given position");
                return;
            }

            if (!player.getWorkers().contains(worker.get())) {
                view.notifyError("Not your worker");
                return;
            }

            model.getTurnState().selectWorker(model, player, worker.get());
        } catch (UnsupportedOperationException | ArrayIndexOutOfBoundsException |
                IllegalArgumentException | NoSuchElementException ex) {
            view.notifyError(ex.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Used to set the power activation button correctly
     *
     * @param view   the view where the even is coming from
     * @param player the player associated with the view
     * @throws UnsupportedOperationException
     * @throws ArrayIndexOutOfBoundsException
     * @throws IllegalArgumentException
     */
    @Override
    public void toggleInteraction(View view, Player player) {
        player = getGamePlayerInstance(player);

        try {
            if (!player.equals(model.getCurrentPlayer())) {
                view.notifyError("Not your turn");
                return;
            }

            if (!model.getTurnState().shouldShowInteraction(model, player)) {
                view.notifyError("You cannot interact now");
                return;
            }

            model.getTurnState().toggleInteraction(model, player);
        } catch (UnsupportedOperationException | ArrayIndexOutOfBoundsException |
                IllegalArgumentException ex) {
            view.notifyError(ex.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Used to select the gods
     *
     * @param view      the view where the even is coming from
     * @param player    the player associated with the view
     * @param powerList with the select powers (singletonList or 2 to 3 power list)
     * @throws UnsupportedOperationException
     */
    @Override
    public void selectPowers(View view, Player player, List<Power> powerList) {
        Player playerInstance = getGamePlayerInstance(player);

        try {
            if (!playerInstance.equals(model.getCurrentPlayer())) {
                view.notifyError("Not your turn");
                return;
            }

            powerList.forEach(p -> model.getTurnState().selectGod(model, playerInstance, p));
        } catch (UnsupportedOperationException ex) {
            view.notifyError(ex.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Used to select the starting player
     *
     * @param view             the view where the even is coming from
     * @param player           the player associated with the view
     * @param chosenPlayerName starting player name
     * @throws UnsupportedOperationException
     */
    @Override
    public void selectStartingPlayer(View view, Player player, String chosenPlayerName) {
        player = getGamePlayerInstance(player);

        try {
            if (!player.equals(model.getCurrentPlayer())) {
                view.notifyError("Not your turn");
                return;
            }

            model.getTurnState().selectStartingPlayer(model, player, chosenPlayerName);
        } catch (UnsupportedOperationException ex) {
            view.notifyError(ex.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Used if a player wants to cancel the move
     *
     * @param view   the view where the even is coming from
     * @param player the player associated with the view
     * @throws UnsupportedOperationException
     */
    @Override
    public void undo(View view, Player player) {
        player = getGamePlayerInstance(player);

        try {
            if (!player.equals(model.getCurrentPlayer())) {
                view.notifyError("Not your turn");
                return;
            }

            model.getTurnState().undo(model, player);
        } catch (UnsupportedOperationException ex) {
            view.notifyError(ex.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     *
     * @param view the view where the event is coming from
     */
    @Override
    public void leaveGame(View view, Player player) {
        player = getGamePlayerInstance(player);

        if (player.hasLost()) {
            model.removeObserver(view);
        } else if (!model.hasEnded() && model.hasStarted()) {
            model.forceEndGame();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Used if a player wants to surrender
     *
     * @param view   the view where the even is coming from
     * @param player the player associated with the view
     * @throws UnsupportedOperationException
     */
    @Override
    public void playerSurrender(View view, Player player) {
        player = getGamePlayerInstance(player);

        try {
            if (!player.equals(model.getCurrentPlayer())) {
                view.notifyError("Not your turn");
                return;
            }

            player.setLoser(true);
            model.nextTurn();
        } catch (UnsupportedOperationException ex) {
            view.notifyError(ex.getMessage());
        }
    }

    private Player getGamePlayerInstance(Player player) {
        Optional<Player> optPlayer = model.getPlayerList().stream()
                .filter(p -> p.equals(player)).findFirst();

        if(optPlayer.isEmpty()) {
            throw new IllegalStateException("Given player not found in game");
        }

        return optPlayer.get();
    }
}
