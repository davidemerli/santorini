package it.polimi.ingsw.PSP1.santorini.observer;

public interface Subject {

    void attach(Observer observer);

    void detach(Observer observer);

    void notify(Observer observer);

}
