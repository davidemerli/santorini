package it.polimi.ingsw.psp1.santorini.controller;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.observer.ViewObserver;
import it.polimi.ingsw.psp1.santorini.view.View;

import java.awt.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class Controller implements ViewObserver {

    private final Game model;

    public Controller(Game model) {
        this.model = model;
    }

    @Override
    public void selectSquare(View view, Player player, Point location) {
        try {
            if (!player.equals(model.getCurrentPlayer())) {
                view.notifyError("Not your turn");
                return;
            }

            model.getTurnState().selectSquare(player, location);
        } catch (UnsupportedOperationException | ArrayIndexOutOfBoundsException |
                IllegalArgumentException ex) {
            view.notifyError(ex.getMessage());
        }
    }

    @Override
    public void selectWorker(View view, Player player, Point workerPosition) {
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

            model.getTurnState().selectWorker(player, worker.get());
        } catch (UnsupportedOperationException | ArrayIndexOutOfBoundsException |
                IllegalArgumentException | NoSuchElementException ex) {
            view.notifyError(ex.getMessage());
        }
    }

    @Override
    public void toggleInteraction(View view, Player player) {
        try {
            if (!player.equals(model.getCurrentPlayer())) {
                view.notifyError("Not your turn");
                return;
            }

            model.getTurnState().toggleInteraction(player);
        } catch (UnsupportedOperationException | ArrayIndexOutOfBoundsException |
                IllegalArgumentException ex) {
            view.notifyError(ex.getMessage());
        }
    }

    @Override
    public void selectPowers(View view, Player player, List<Power> powerList) {
        try {
            if (!player.equals(model.getCurrentPlayer())) {
                view.notifyError("Not your turn");
                return;
            }

            powerList.forEach(p -> model.getTurnState().selectGod(model, player, p));
        } catch (UnsupportedOperationException ex) {
            view.notifyError(ex.getMessage());
        }
    }

    @Override
    public void selectStartingPlayer(View view, Player player, String chosenPlayerName) {
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

    @Override
    public void undo(View view, Player player) {
        try {
            if (!player.equals(model.getCurrentPlayer())) {
                view.notifyError("Not your turn");
                return;
            }

            model.getTurnState().undo(player);
        } catch (UnsupportedOperationException ex) {
            view.notifyError(ex.getMessage());
        }
    }

    @Override
    public void leaveGame(View view) {
        if (view.getPlayer().hasLost()) {
            model.removeObserver(view);
            view.removeObserver(this);
        } else if(model.isRunning()){
            model.forceEndGame();
        }
    }
}
