package it.polimi.ingsw.psp1.santorini.observer;

import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.view.View;

import java.awt.*;
import java.util.List;

public interface ViewObserver {

    void selectSquare(View view, Player player, Point location);

    void selectWorker(View view, Player player, Worker worker);

    void toggleInteraction(View view, Player player);

    void selectPowers(View view, Player player, List<Power> powerList);

    void undo(View view, Player player);

}
