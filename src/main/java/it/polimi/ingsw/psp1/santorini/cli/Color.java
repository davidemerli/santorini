package it.polimi.ingsw.psp1.santorini.cli;

public enum Color {

    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\033[34m"),

    BACKGROUND_RED("\u001B[41m"),
    BACKGROUND_GREEN("\u001B[42m"),
    BACKGROUND_YELLOW("\u001B[43m"),
    BACKGROUND_BLUE("\u001B[44m"),
    BACKGROUND_CYAN("\u001B[46m"),
    BACKGROUND_BRIGHT_RED("\u001B[101m"),
    BACKGROUND_BRIGHT_GREEN("\u001B[102m"),
    BACKGROUND_BRIGHT_YELLOW("\u001B[103m"),
    BACKGROUND_GRAY("\u001B[48;5;245m"),
    BACKGROUND_GRAY2("\u001B[48;5;250m");


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
