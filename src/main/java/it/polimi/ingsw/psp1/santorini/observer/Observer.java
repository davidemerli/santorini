package it.polimi.ingsw.psp1.santorini.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class Observer<T> {

    private final List<T> observers = new ArrayList<>();

    /**
     * Adds an observer to the list to notify when needed
     *
     * @param observer to be added
     */
    public void addObserver(T observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Removes an observer from the list, it wont receive notifications anymore
     *
     * @param observer to be removed
     */
    public void removeObserver(T observer) {
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * Calls a notification function on the observer
     *
     * @param lambda function to be applied on the observer
     */
    public void notifyObservers(Consumer<T> lambda) {
        synchronized (observers) {
            observers.forEach(lambda);
        }
    }

}

