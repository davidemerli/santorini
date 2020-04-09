package it.polimi.ingsw.psp1.santorini.view;

import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.observer.ModelObserver;

public abstract class View implements ModelObserver {

    private final Player player;

    public View(Player player) {
        this.player = player;
    }

    public abstract void notifyError(String error);

    public Player getPlayer() {
        return player;
    }

}
