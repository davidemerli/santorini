package it.polimi.ingsw.psp1.santorini.cli;

import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.packets.server.PlayerData;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class PrintUtils {

    public final static int MAX_LENGTH = 150;
    public final static int size = 6;
    private final static int space = 2 * size;

    public static void stampMap(GameMap map) {
        Point cursor;
        Point p;
        String s = String.format("%1$" + space + "s", "");
        for (int i = 0; i < GameMap.SIDE_LENGTH; i++) {
            for (int j = 0; j < GameMap.SIDE_LENGTH; j++) {
                p = new Point(i, j);
                int level = map.getLevel(p);
                cursor = setCursorInMap(i, j);
                if (level == 0) {
                    // solo spazi
                    for (int k = 1; k <= size; k++) {
                        System.out.print(s + Color.RESET + " ");
                        setCursor(cursor.x + k, cursor.y);
                    }
                } else if (level == 1) {
                    for (int k = 1; k <= size; k++) {
                        System.out.print(Color.BACKGROUND_GRAY2 + s + Color.RESET + " ");
                        setCursor(cursor.x + k, cursor.y);
                    }
                } else if (level == 2) {
                    for (int k = 1; k <= size; k++) {
                        System.out.print(Color.BACKGROUND_YELLOW + s + Color.RESET + " ");
                        setCursor(cursor.x + k, cursor.y);
                    }
                } else if (level == 3) {
                    for (int k = 1; k <= size; k++) {
                        System.out.print(Color.BACKGROUND_BRIGHT_RED + s + Color.RESET + " ");
                        setCursor(cursor.x + k, cursor.y);
                    }
                } else {
                    String isDome;
                    String notDome;
                    int red, blue;
                    for (int k = 1; k <= size; k++) {
                        if (k == 1 || k == size) {
                            System.out.print(Color.BACKGROUND_BRIGHT_RED + s + Color.RESET + " ");
                        } else {
                            red = space / 4 - 1;
                            blue = space - 2 * red;
                            notDome = String.format("%1$" + red + "s", "");
                            isDome = String.format("%1$" + blue + "s", "");
                            System.out.print(Color.BACKGROUND_BRIGHT_RED + notDome +
                                    Color.BACKGROUND_BLUE + isDome +
                                    Color.BACKGROUND_BRIGHT_RED + notDome +
                                    Color.RESET + " ");
                        }
                        setCursor(cursor.x + k, cursor.y);
                    }
                }
            }
        }
    }

    public static Point setCursorInMap(int i, int j) {
        int x, y;
        int defaultPositionX = 8;
        int defaultPositionY = 1;

        x = defaultPositionX + (i * (size + 1));
        y = defaultPositionY + (j * (space + 2));

        setCursor(x, y);
        return new Point(x, y);
    }

    public static void clearBoard() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // prendere il puntatore uguale sotto ma con R

    public static void setCursor(int x, int y) {
        System.out.print(String.format("%c[%d;%df", 0x1B, x, y));
    }

    public static void resetCursor() {
        System.out.print(String.format("%c[%d;%df", 0x1B, 10, 0));
    }

    public static void clearRow(int x, int y) {
        for (int i = MAX_LENGTH; i >= y; i--) {
            PrintUtils.setCursor(x, i);
            System.out.print(" ");
        }
    }

    public static void print(String string, int x, int y, boolean toClean) {
        if (toClean) {
            clearRow(x, y);
        }
        setCursor(x, y);
        System.out.print(string);
    }

    public static void printGodList(List<Power> list) {
        setCursor(2, 0);
        for (int i = 1; i < list.size() + 1; i++) {
            System.out.print(i + ") " + list.get(i - 1).getClass().getSimpleName() + " \t\t");
            if (i % 3 == 0) {
                System.out.println();
            }
        }
    }

    public static void printPlayerInfo(List<PlayerData> list) {
        //      StringJoiner players = new StringJoiner("\t\t-\t");
        //      StringJoiner gods = new StringJoiner("\t\t-\t");

        //      setCursor(3, 0);

        //      list.forEach(p -> players.add(p.getName()));
        //      list.forEach(g -> gods.add(g.getPower().getClass().getSimpleName()));

        //      System.out.println(players);
        //      System.out.println(gods);

        setCursor(2, 0);
        list.forEach(p -> System.out.format("%-20s", p.getName()));
        System.out.println();
        list.stream().map(p -> p.getPower().getClass().getSimpleName()).forEach(s -> System.out.format("%-20s", s));

        // stato attuale
    }

    public static void printValidMoves(List<Point> valid, List<Point> blocked) {
        setCursor(16, 0);
        for (int i = 0; i < valid.size(); i++) {

        }
        // gioco di cursore e con il worker? o metto la mappa come parametro?
    }


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
