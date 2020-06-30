package it.polimi.ingsw.psp1.santorini.gui.controllers;

import it.polimi.ingsw.psp1.santorini.gui.GuiObserver;
import it.polimi.ingsw.psp1.santorini.observer.Observable;

/**
 * Generic GUI controller
 */
public abstract class GuiController extends Observable<GuiObserver> {
    /**
     * Reset
     */
    public abstract void reset();
}
