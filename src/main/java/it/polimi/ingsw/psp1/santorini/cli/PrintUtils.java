package it.polimi.ingsw.psp1.santorini.cli;

import it.polimi.ingsw.psp1.santorini.model.map.GameMap;

import java.awt.*;
import java.util.Random;

public class PrintUtils {

    public final static int MAX_LENGTH = 150;

    public static void stampMap(GameMap map) {
        Point p;
        setCursor(8, 0);
        for (int i = 0; i < GameMap.SIDE_LENGTH; i++) {
            for (int j = 0; j < GameMap.SIDE_LENGTH; j++) {

                // System.out.print(map.getLevel(new Point(i, j)) + " ");
                p = new Point(i, j);

                int level = map.getLevel(p);
                if (level == 0) {
                    System.out.print(Color.BACKGROUND_WHITE + " " + Color.RESET + " ");
                } else if (level == 1) {
                    System.out.print(Color.BACKGROUND_BLUE + " " + Color.RESET + " ");
                } else if (level == 2) {
                    System.out.print(Color.BACKGROUND_GREEN + " " + Color.RESET + " ");
                } else if (level == 3) {
                    System.out.print(Color.BACKGROUND_YELLOW + " " + Color.RESET + " ");
                } else {
                    System.out.print(Color.BACKGROUND_RED + " " + Color.RESET + " ");
                }
            }
            System.out.println();
        }
    }

    public static void clearBoard() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void setCursor(int x, int y) {
        System.out.print(String.format("%c[%d;%df", 0x1B, x, y));
    }

    public static void resetCursor() {
        // definire x e y dove vogliamo sia il set predefinito del cursore.
        System.out.print(String.format("%c[%d;%df", 0x1B, 10, 20));
    }

    public static void print(String string, int x, int y, boolean clear) {
        setCursor(x, y);
        System.out.print(string);
        // come gestire il clear?
        // mette il cursore a x,y una volta sola e printa la stringa
    }

    // clear riga

    // per testing
    public static GameMap createMap() {
        GameMap map = new GameMap();
        for (int i = 0; i < 15; i++) {
            map.buildBlock(new Point(new Random().nextInt(5), new Random().nextInt(5)), false);
        }
        return map;
    }

    public static void updateMap(GameMap map) {
        map.buildBlock(new Point(new Random().nextInt(5), new Random().nextInt(5)), false);
    }

    // reset cursor dove voglio scrivere di default

    // pulire la parte sotto dello schermo (sotto la mia scritta help) circa 15 righe prima di un command faccio la clear
}

/*
STAMPARE TITOLO
CHIEDERE NOME E SERVER:

CLEAR

SCELTA GODS

CLEAR

SCHERMATA GIOCO
 */
