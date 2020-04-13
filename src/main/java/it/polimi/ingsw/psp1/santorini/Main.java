package it.polimi.ingsw.psp1.santorini;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Main {

    public static void main(String[] args) {
        aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa();
    }


    static BiFunction<Integer, Integer, String> MOVE_CURSOR = (x, y) -> "\u001b[32m" + String.format("\u001b[%d;%df", y, x);
    static Function<Character, Character> update = (c) -> c == '_' ? '#' : '_';


    public static void aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa() {

        for (int i = 0; i < 256; i += 1) {
            System.out.print(String.format(" \u001b[38;2;0;0;%sm %s \u001b[0m ", i, i));
        }

        System.out.print("\033[H\033[2J");
        System.out.flush();

        char[][] matrix = { {'#', '#', '#'},
                            {'#', '_', '#'},
                            {'#', '#', '#'} };

        Scanner scanner = new Scanner(System.in);
        System.out.print(MOVE_CURSOR.apply(0, 4) + " ");

        while (true) {
            String s = scanner.nextLine();

            printMatrix(matrix);

            for (int i = s.length() + 1; i >= 0; i--) {
                System.out.print(MOVE_CURSOR.apply(i, 4) + " ");
            }

            System.out.print(MOVE_CURSOR.apply(0, 8) + "");

            List<String> ss = Arrays.asList("Testtest".split(""));

            Collections.shuffle(ss);

            System.out.println(ss.toString());


            System.out.print(MOVE_CURSOR.apply(0, 4) + "");



//            System.out.print("\033[H\033[2J");
//            System.out.flush();


        }
    }

    static void printMatrix(char[][] matrix) {
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

        System.out.println();
    }
}

