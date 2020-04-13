package it.polimi.ingsw.psp1.santorini.cli;

import it.polimi.ingsw.psp1.santorini.model.map.GameMap;

public class PrintUtils {

    static void stampMap (GameMap map) {
        // get level e has dome
        // TODO clear o senza clear?
    }

    static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void setCursor(int x, int y) {
        System.out.print(String.format("%c[%d;%df",0x1B,x,y));
    }

    // stampo lista player
    static void stampList() {

    }


    // stampo stato corrente

    static void print(String string, int x, int y) {
        // mette il cursore a x,y una volta sola e printa la stringa
    }

    // reset cursor dove voglio scrivere di default

    // pulire la parte sotto dello schermo (sotto la mia scritta help) circa 15 righe prima di un command faccio la clear

    // NON usare bifucntion, usa reset cursor
}
