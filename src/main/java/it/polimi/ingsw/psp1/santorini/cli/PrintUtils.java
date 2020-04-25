package it.polimi.ingsw.psp1.santorini.cli;

import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.packets.server.PlayerData;

import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class PrintUtils {

    public final static int MAX_LENGTH = 150;
    public final static int size = 4;
    public final static int spacing = 1;

    private static int mapX = 2;
    private static int mapY = 5;

    public static void stampMap(GameMap map) {
        String s = String.format("%" + (size * 2 - 1) + "s", "");

        StringBuilder bgLine = new StringBuilder();
        IntStream.range(0, GameMap.SIDE_LENGTH * ((size * 2) + (spacing * 2 - 1)))
                .forEach(i -> bgLine.append(" "));

        for (int i = 0; i < GameMap.SIDE_LENGTH * (size + spacing) + 1; i++) {
            print(Color.BACKGROUND_GRASS + bgLine.toString(), mapX, mapY + i, false);
        }

        for (int i = 0; i < GameMap.SIDE_LENGTH; i++) {
            for (int j = 0; j < GameMap.SIDE_LENGTH; j++) {
                Point point = new Point(i, j);
                for (int k = 0; k < size; k++) {
                    int x = mapX + i * (size * 2) + i + spacing;
                    int y = mapY + j * size + j * spacing + k + spacing;

                    String string = getColorFromLevel(map.getLevel(point) - (map.hasDome(point) ? 1 : 0)) + s;

                    print(string, x, y, false);
                }

                if (map.hasDome(point)) {
                    for (int k = 1; k < size - 1; k++) {
                        int x = mapX + i * (size * 2) + i + spacing;
                        int y = mapY + j * size + j * spacing + k + spacing;

                        String string = Color.BACKGROUND_BRIGHT_BLUE + s.substring(4);

                        print(string, x + 2, y, false);
                    }
                }
            }
        }

        resetCursor();
        System.out.println(Color.RESET);
    }

    public static void clearBoard() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // prendere il puntatore uguale sotto ma con R

    public static void setCursor(int x, int y) {
        System.out.print(String.format("\033[%d;%df", y, x));
        System.out.flush();
    }

    public static void resetCursor() {
        setCursor(3, 2 + mapY + GameMap.SIDE_LENGTH * (size + spacing) - spacing);
    }

    public static void clearRow(int x, int y) {
        for (int i = MAX_LENGTH; i >= x; i--) {
            PrintUtils.setCursor(i, y);
            System.out.print(" ");
        }
    }

    public static void print(String string, int x, int y, boolean toClean) {
        if (toClean) {
            clearRow(x, y);
        }

        setCursor(x, y);
        System.out.print(string);
        System.out.flush();

        resetCursor();
    }

    public static void printFromCommand(String string, int xOff, int yOff, boolean toClear) {
        Point point = getCommandCoords();
        print(string, point.x + xOff, point.y + yOff, toClear);
    }

    public static void printCommand() {
        print("> ", 0, 2 + mapY + GameMap.SIDE_LENGTH * (size + spacing) - spacing, true);
    }

    public static Point getCommandCoords() {
        return new Point(0, 2 + mapY + GameMap.SIDE_LENGTH * (size + spacing) - spacing);
    }

    public static void printGodList(List<Power> list) {
        setCursor(0, 0);
        clearBoard();
        for (int i = 1; i <= list.size(); i++) {
            System.out.print(i + ") " + list.get(i - 1).getClass().getSimpleName() + " \t\t");

            if (i % 3 == 0) {
                System.out.println();
            }
        }

        printCommand();
    }

    public static void printPlayerInfo(List<PlayerData> list) {
        StringBuilder builder = new StringBuilder();
        list.stream().map(p -> String.format("%-20s", p.getName())).forEach(builder::append);

        print(builder.toString(), 2, 0, true);

        builder = new StringBuilder();
        list.stream().filter(p -> p.getPower() != null)
                .map(p -> p.getPower().getClass().getSimpleName())
                .map(s -> String.format("%-20s", s)).forEach(builder::append);

        print(builder.toString(), 2, 2, true);

        printCommand();
    }

    public static void printValidMoves(List<Point> valid, List<Point> blocked) {
        int counter = 1;

        for (Point point : valid) {
            if (!blocked.contains(point)) {
                String s = String.valueOf(counter);
                print(Color.BACKGROUND_BRIGHT_YELLOW + "" + Color.BLUE + s + Color.RESET,
                        point.x + mapX, point.y + mapY, false);
                counter++;
            }
        }
        // gioco di cursore e con il worker? o metto la mappa come parametro?
    }

    // per testing
    public static GameMap createMap() {
        GameMap map = new GameMap();
        for (int i = 0; i < 15; i++) {
            Point point = new Point(new Random().nextInt(5), new Random().nextInt(5));
            if (!map.hasDome(point)) {
                map.buildBlock(point, false);
            } else {
                i--;
            }
        }
        return map;
    }

    public static void updateMap(GameMap map) {
        Point point = new Point(new Random().nextInt(5), new Random().nextInt(5));
        if (!map.hasDome(point)) {
            map.buildBlock(point, false);
        }
    }

    private static Color getColorFromLevel(int level) {
        switch (level) {
            case 1:
                return Color.BACKGROUND_GRAY1;
            case 2:
                return Color.BACKGROUND_GRAY2;
            case 3:
                return Color.BACKGROUND_GRAY3;
            default:
                return Color.BACKGROUND_GRASS;
        }
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
