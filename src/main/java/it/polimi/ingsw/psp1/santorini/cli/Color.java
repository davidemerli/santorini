package it.polimi.ingsw.psp1.santorini.cli;

public enum Color {

    BLACK("\033[30m"),
    RED("\033[31m"),
    GREEN("\033[32m"),
    YELLOW("\033[33m"),
    BLUE("\033[34m"),

    BACKGROUND_RED("\033[41m"),
    BACKGROUND_GREEN("\033[42m"),
    BACKGROUND_YELLOW("\033[43m"),
    BACKGROUND_BLUE("\033[44m"),
    BACKGROUND_CYAN("\033[46m"),
    BACKGROUND_BRIGHT_RED("\033[101m"),
    BACKGROUND_BRIGHT_GREEN("\033[102m"),
    BACKGROUND_BRIGHT_YELLOW("\033[103m"),

    BACKGROUND_GRAY1("\033[48;2;140;140;140m"),
    BACKGROUND_GRAY2("\033[48;2;175;175;175m"),
    BACKGROUND_GRAY3("\033[48;2;220;220;220m"),
    BACKGROUND_BRIGHT_BLUE("\033[48;2;40;69;186m"),
    BACKGROUND_GRASS("\033[48;2;93;181;105m"),
    BACKGROUND_GRASS2("\033[48;2;93;140;105m"),


    RESET("\033[0m");

    private String s;

    Color(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}
