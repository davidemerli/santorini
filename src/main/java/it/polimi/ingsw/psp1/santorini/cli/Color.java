package it.polimi.ingsw.psp1.santorini.cli;

public enum Color {

    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    RED("\u001B[31m"),
    WHITE("\u001B[37m"),
    BLUE("\033[34m"),

    BACKGROUND_GREEN("\u001B[42m"),
    BACKGROUND_YELLOW("\u001B[43m"),
    BACKGROUND_RED("\u001B[41m"),
    BACKGROUND_WHITE("\u001B[47m"),
    BACKGROUND_BLUE("\u001B[44m");

    static final String RESET = "\u001B[0m";

    private String s;

    Color(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}
