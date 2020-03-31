package it.polimi.ingsw.psp1.santorini.observer;

public interface Subject {

    void attach(Observer observer);

    void detach(Observer observer);

    void notify(Observer observer);

}
