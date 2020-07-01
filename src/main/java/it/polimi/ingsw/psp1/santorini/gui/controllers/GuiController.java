package it.polimi.ingsw.psp1.santorini.gui.controllers;

import it.polimi.ingsw.psp1.santorini.gui.GuiObserver;
import it.polimi.ingsw.psp1.santorini.observer.Observable;

/**
 * Generic GUI controller
 */
public abstract class GuiController extends Observable<GuiObserver> {

    /**
     * Resets the controller
     * (Used to reset after a game finishes, or there is a disconnection)
     */
    public abstract void reset();
}
