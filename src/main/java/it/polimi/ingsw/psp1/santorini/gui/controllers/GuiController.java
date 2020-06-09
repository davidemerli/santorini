package it.polimi.ingsw.psp1.santorini.gui.controllers;

import it.polimi.ingsw.psp1.santorini.gui.GuiObserver;
import it.polimi.ingsw.psp1.santorini.observer.Observable;

public abstract class GuiController extends Observable<GuiObserver> {
    public abstract void reset();
}
