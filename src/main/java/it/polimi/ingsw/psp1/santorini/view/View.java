package it.polimi.ingsw.psp1.santorini.view;

import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.observer.ConnectionObserver;
import it.polimi.ingsw.psp1.santorini.observer.ModelObserver;
import it.polimi.ingsw.psp1.santorini.observer.ViewObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class View implements ModelObserver {

    private final Player player;
    private final List<ViewObserver> observers;

    public View(Player player) {
        this.player = player;
        this.observers = new ArrayList<>();
    }

    public abstract void notifyError(String error);

    public Player getPlayer() {
        return player;
    }

    public void addObserver(ViewObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ViewObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Consumer<ViewObserver> lambda) {
        observers.forEach(lambda);
    }
}
