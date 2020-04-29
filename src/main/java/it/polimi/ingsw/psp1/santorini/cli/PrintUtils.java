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
import java.util.function.Function;
import java.util.stream.IntStream;

import static it.polimi.ingsw.psp1.santorini.cli.Color.*;

public class PrintUtils {

    public final static int MAX_LENGTH = 150;
    public final static int size = 3;
    public final static int spacing = 1;

    private static int mapX = 2;
    private static int mapY = 5;

    public static void stampMap(GameMap map, List<PlayerData> players, Map<String, Color> colorMap) {
        if (map == null) {
            return;
        }

        String s = String.format("%" + (size * 2 - 1) + "s", "");

        StringBuilder bgLine = new StringBuilder();
        IntStream.range(0, GameMap.SIDE_LENGTH * ((size * 2) + (spacing * 2 - 1)))
                .forEach(i -> bgLine.append(" "));

        for (int i = 0; i < GameMap.SIDE_LENGTH * (size + spacing) + 1; i++) {
            print(BACKGROUND_GRASS2 + bgLine.toString(), mapX, mapY + i, false);
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

                print(BACKGROUND_GRASS2 + BLACK.toString() + "L" + level, x + size * 2 - 3, y + size - 1, false);

                if (map.hasDome(point)) {
                    for (int k = 1; k < size - 1; k++) {
                        String string = BACKGROUND_BRIGHT_BLUE + s.substring(4);

                        print(string, x + 2, y + k, false);
                    }
                }
            }
        }

        for (PlayerData player : players) {
            for (int i = 0; i < player.getWorkers().size(); i++) {
                Worker w = player.getWorkers().get(i);

                int x = mapX + 2 + w.getPosition().x * (size * 2 + spacing * 2 - 1);
                int y = mapY + 2 + w.getPosition().y * (size + spacing);

                String string = colorMap.get(player.getName()) + "W" + (i + 1);

                print(string, x, y, false);
            }
        }
    }

    public static void firstClear() {
        System.out.print("\033[H\033[3J\033[2J");
        System.out.flush();
    }

    public static void clear() {
        for (int i = 0; i < 150; i++) {
            clearRow(0, i);
        }
    }

    // prendere il puntatore uguale sotto ma con R

    public static void setCursor(int x, int y) {
        System.out.print(String.format("\033[%d;%dH", y, x));
        System.out.flush();
    }

    public static void resetCursor() {
        Point point = getCommandCoords();
        setCursor(point.x + 3, point.y);
    }

    public static void clearRow(int x, int y) {
        setCursor(x, y);
        System.out.print("\033[K");
        System.out.flush();
    }

    public static void print(String string, int x, int y, boolean toClean) {
        if (toClean) {
            clearRow(x, y);
        }

        System.out.print(RESET);
        System.out.flush();

        setCursor(x, y);

        System.out.print(string);
        System.out.flush();

        resetCursor();

        System.out.print(RESET);
        System.out.flush();
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
        int row = 5;
        int offset = GameMap.SIDE_LENGTH * ((size + spacing) * 2 - 1) + 10;

        print(list.size() > 0 ? "Power list:" : "", offset, 4, true);

        for (int i = mapY; i < getCommandCoords().y - 3; i++) {
            clearRow(offset, i);
        }

        for (int i = 0; i < list.size(); i++) {
            if (i % 3 == 0) {
                row++;
            }

            print((i + 1) + ") " + BLUE + list.get(i).getClass().getSimpleName(),
                    (i % 3) * 20 + offset,
                    row,
                    false);
        }

        printCommand();
        resetCursor();
    }

    public static void printPlayerInfo(String clientPlayer, List<PlayerData> list, EnumTurnState state,
                                       Map<String, Color> colorMap, boolean interact) {

        Function<String, String> name = (s) -> colorMap.get(s) + s +
                (s.equals(clientPlayer) ? "(YOU)" : "") + RESET;

        clearRow(2, 1);
        clearRow(2, 2);
        clearRow(2, 3);

        PrintUtils.print("\uD83E\uDC7E Current player", 1, 1, true);

        for (int i = 0; i < list.size(); i++) {
            PlayerData data = list.get(i);

            print(name.apply(data.getName()), i * 20 + 2, 2, false);
            print(data.getPower() != null ? data.getPower().getClass().getSimpleName().toUpperCase() : "N/A",
                    i * 20 + 2, 3, false);
        }

//        if (list.size() > 0 && state != null) {
//            print(String.format("Playing: '%s', Turn State: %s",
//                    BLUE + list.get(0).getName() + RESET,
//                    RED + state.toString()),
//                    2, 3, true);
//        }

        printCommand();
    }

    public static void printValidMoves(List<Point> valid, Map<Power, List<Point>> blocked) {
        int counter = 1;

        for (Point point : valid) {
            if (blocked.values().stream().flatMap(Collection::stream).noneMatch(p -> p.equals(point))) {
                String s = String.valueOf(counter);
                print(BACKGROUND_BRIGHT_YELLOW + "" + RED + s,
                        mapX + point.x * (size * 2 - 1 + spacing * 2) + 1,
                        mapY + point.y * (size + spacing) + 1,
                        false);
                counter++;
            }
        }
    }

    private static Color getColorFromLevel(int level) {
        switch (level) {
            case 1:
                return BACKGROUND_GRAY1;
            case 2:
                return BACKGROUND_GRAY2;
            case 3:
                return BACKGROUND_GRAY3;
            default:
                return BACKGROUND_GRASS;
        }
    }
}
