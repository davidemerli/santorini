package it.polimi.ingsw.psp1.santorini.cli;

import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumTurnState;
import it.polimi.ingsw.psp1.santorini.network.packets.server.PlayerData;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

public class PrintUtils {

    public final static int MAX_LENGTH = 150;
    public final static int size = 4;
    public final static int spacing = 1;

    private static int mapX = 2;
    private static int mapY = 5;

    public static void stampMap(GameMap map, List<PlayerData> players) {
        if(map == null) {
            return;
        }

        String s = String.format("%" + (size * 2 - 1) + "s", "");

        StringBuilder bgLine = new StringBuilder();
        IntStream.range(0, GameMap.SIDE_LENGTH * ((size * 2) + (spacing * 2 - 1)))
                .forEach(i -> bgLine.append(" "));

        for (int i = 0; i < GameMap.SIDE_LENGTH * (size + spacing) + 1; i++) {
            print(Color.BACKGROUND_GRASS2 + bgLine.toString(), mapX, mapY + i, false);
        }

        for (int i = 0; i < GameMap.SIDE_LENGTH; i++) {
            for (int j = 0; j < GameMap.SIDE_LENGTH; j++) {
                Point point = new Point(i, j);
                int level = map.getLevel(point) - (map.hasDome(point) ? 1 : 0);
                int x = mapX + i * (size * 2) + i + spacing;
                int y = mapY + j * size + j * spacing + spacing;

                for (int k = 0; k < size; k++) {
                    print(getColorFromLevel(level) + s, x, y + k, false);
                }

                print("L" + level, x + size*2 - 3, y + size - 1, false);

                if (map.hasDome(point)) {
                    for (int k = 1; k < size - 1; k++) {
                        String string = Color.BACKGROUND_BRIGHT_BLUE + s.substring(4);

                        print(string, x + 2, y + k, false);
                    }
                }
            }
        }

        for (PlayerData player : players) {
            for (Worker w : player.getWorkers()) {
                int x = mapX + 2 + w.getPosition().x * (size * 2 + spacing * 2 - 1);
                int y = mapY + 2 + w.getPosition().y * (size + spacing);

                String string = Color.BACKGROUND_BRIGHT_RED + "W";

                print(string, x, y, false);
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
        Point point = getCommandCoords();
        setCursor(point.x + 3, point.y);
    }

    public static void clearRow(int x, int y) {
        for (int i = MAX_LENGTH; i >= x; i--) {
            PrintUtils.setCursor(i, y);
            System.out.print(" ");
        }
    }

    public static void clearFrom(int y) {
        String s = String.format("%" + (MAX_LENGTH) + "s", "");
        for (int i = 0; i < 15; i++) {
            PrintUtils.setCursor(0, y + i);
            System.out.print(s);
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
        Point point = getCommandCoords();
        print("> ", point.x, point.y, true);
    }

    public static Point getCommandCoords() {
        return new Point(0, 4 + mapY + GameMap.SIDE_LENGTH * (size + spacing) - spacing);
    }

    public static void printPowerList(List<Power> list) {
        clearBoard();

        int row = 0;
        for (int i = 0; i < list.size(); i++) {
            if (i % 3 == 0) {
                row++;
            }

            print((i + 1) + ") " + list.get(i).getClass().getSimpleName(), (i % 3) * 20, row + 3, false);
        }

        printCommand();
    }

    public static void printPlayerInfo(List<PlayerData> list, EnumTurnState state) {
        StringBuilder builder = new StringBuilder();
        list.stream().map(p -> String.format("%-20s", p.getName())).forEach(builder::append);

        print(builder.toString(), 2, 1, true);

        builder = new StringBuilder();
        list.stream().map(p -> p.getPower() == null ? "N/A" : p.getPower().getClass().getSimpleName())
                .map(s -> String.format("%-20s", s)).forEach(builder::append);

        print(builder.toString(), 2, 2, true);

        if(list.size() > 0 && state != null) {
            print(String.format("Playing: '%s', Turn State: %s",
                    Color.BLUE + list.get(0).getName() + Color.RESET,
                    Color.RED + state.toString() + Color.RESET),
                    2, 3, true);
        }

        printCommand();
    }

    public static void printValidMoves(List<Point> valid, Map<Power, List<Point>> blocked) {
        int counter = 1;

        for (Point point : valid) {
            if (!blocked.values().stream().flatMap(Collection::stream).anyMatch(p -> p.equals(valid))) {
                String s = String.valueOf(counter);
                print(Color.BACKGROUND_BRIGHT_YELLOW + "" + Color.RED + s + Color.RESET,
                        mapX + point.x * (size * 2 - 1 + spacing * 2) + 1,
                        mapY + point.y * (size + spacing) + 1,
                        false);
                counter++;
            }
        }
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
