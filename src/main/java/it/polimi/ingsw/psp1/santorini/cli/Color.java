package it.polimi.ingsw.psp1.santorini.cli;

public enum Color {

    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    RED("\u001B[31m"),

    BACKGROUND_GREEN("\u001B[42m"),
    BACKGROUND_YELLOW("\u001B[43m"),
    BACKGROUND_RED("\u001B[41m");

    Color(String s) {
    }
}
