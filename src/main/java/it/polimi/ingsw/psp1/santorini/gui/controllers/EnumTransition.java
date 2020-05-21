package it.polimi.ingsw.psp1.santorini.gui.controllers;

import it.polimi.ingsw.psp1.santorini.gui.Gui;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;

public enum EnumTransition {
    UP(new int[]{0, 1}),
    DOWN(new int[]{0, -1}),
    LEFT(new int[]{1, 0}),
    RIGHT(new int[]{-1, 0}),
    FROM_TOP_LEFT(new int[]{-1, -1}),
    FROM_TOP_RIGHT(new int[]{1, -1}),
    FROM_BOTTOM_LEFT(new int[]{-1, 1}),
    FROM_BOTTOM_RIGHT(new int[]{1, 1});

    int[] vector;
    TranslateTransition tt;

    EnumTransition(int[] vector) {
        this.vector = vector;
        this.tt = new TranslateTransition();
    }

    public TranslateTransition getTransition(Pane currentPane, Pane nextPane) {
        tt.setNode(currentPane);

        tt.setByX(-currentPane.getWidth() * vector[0]);
        tt.setByY(-currentPane.getHeight() * vector[1]);
        tt.setInterpolator(Interpolator.EASE_BOTH);

        nextPane.translateXProperty().set(currentPane.getWidth() * vector[0]);
        nextPane.translateYProperty().set(currentPane.getHeight() * vector[1]);

        currentPane.getChildren().add(nextPane);

        tt.setOnFinished(event -> {
            nextPane.translateXProperty().set(0);
            nextPane.translateYProperty().set(0);
            currentPane.getChildren().clear();

            Gui.getInstance().changeScene(nextPane);
        });

        return tt;
    }
}
