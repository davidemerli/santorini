package it.polimi.ingsw.psp1.santorini.cli.ansi;

import javafx.scene.paint.Color;

import java.util.Arrays;

public enum ANSI {

    RESET(0),
    BOLD(1),
    FAINT(2),
    ITALIC(3),
    UNDERLINE(4),
    SLOW_BLINK(5),
    RAPID_BLINK(6),
    REVERSE(7),
    HIDE(8),
    CROSS_OUT(9);

    private static final String ESC = "\033[";
    private final int code;

    ANSI(int code) {
        this.code = code;
    }

    public static String applyStyles(Color textColor, Color backgroundColor, ANSI... styles) {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(styles).map(ANSI::toString).forEach(sb::append);

        return colorToANSI(textColor, false) + colorToANSI(backgroundColor, true) + sb.toString();
    }

    public static String applyStyles(Color textColor, ANSI... styles) {
        return applyStyles(textColor, null, styles);
    }

    public static String colorToANSI(Color color, boolean isBackground) {
        if (color == null) {
            return "";
        }

        return String.format("%s%d;2;%d;%d:%dm", ESC,
                isBackground ? 48 : 38,
                (int) (color.getBlue() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    @Override
    public String toString() {
        return String.format("%s%dm", ESC, code);
    }
}
