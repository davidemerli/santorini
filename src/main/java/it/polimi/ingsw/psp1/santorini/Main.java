package it.polimi.ingsw.psp1.santorini;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Main {

    public static void main(String[] args) {
        aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa();
    }

    public static void aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa() {
        Function<Character, Character> update = (c) -> c == '_' ? '#' : '_';
        BiFunction<Integer, Integer, String> MOVE_CURSOR = (x, y) -> "\u001b[32m" + String.format("\u001b[%d;%df", x, y);

        for (int i = 0; i < 256; i += 1) {
            System.out.print(String.format(" \u001b[38;2;0;0;%sm %s \u001b[0m ", i, i));
        }

        System.out.print("\033[H\033[2J");
        System.out.flush();

        char[][] matrix = {{'#', '#', '#'},
                {'#', '_', '#'},
                {'#', '#', '#'}};

        for (int c = 0; c < 100030; c++) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    System.out.print(MOVE_CURSOR.apply(j + 1, i + 1) + matrix[j][i]);

                }
            }

            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    matrix[i][j] = update.apply(matrix[i][j]);
                }
            }

            try {
                Thread.sleep(300);
            } catch (Exception ex) {
            }
        }

        System.out.print("\033[H\033[2J");
        System.out.flush();

    }
}
