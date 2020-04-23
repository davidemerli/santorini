package it.polimi.ingsw.psp1.santorini.observer;

import it.polimi.ingsw.psp1.santorini.model.powers.Power;

import java.awt.*;
import java.util.List;

public interface ConnectionObserver {
    void processPowerList(List<Power> powerList);

    void processSquareSelection(Point square);

    void processToggleInteraction();

    void handlePlayerForfeit();

    void processKeepAlive();

    void processRequestGameData();

    void processWorkerSelection(Point workerPosition);

    void processStartingPlayerSelection(String name);
}