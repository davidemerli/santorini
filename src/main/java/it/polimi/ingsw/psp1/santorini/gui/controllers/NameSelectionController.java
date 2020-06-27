package it.polimi.ingsw.psp1.santorini.gui.controllers;

import it.polimi.ingsw.psp1.santorini.gui.EnumScene;
import it.polimi.ingsw.psp1.santorini.gui.Gui;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class NameSelectionController extends GuiController {

    private static NameSelectionController instance;

    @FXML
    private TextField nameTextField;

    @FXML
    private ImageView whirlpool;

    @FXML
    private Label message;

    @FXML
    private void initialize() {
        getInstance().whirlpool = whirlpool;
        getInstance().message = message;

        RotateTransition rt = new RotateTransition();
        rt.setNode(whirlpool);
        rt.setByAngle(360);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.play();
    }

    @FXML
    private void onButtonClick(ActionEvent event) {
        getInstance().notifyObservers(o -> o.onNameSelection(nameTextField.getText()));

        getInstance().whirlpool.setVisible(true);
        getInstance().message.setVisible(true);
        getInstance().message.setTextFill(Color.valueOf("#000000cc"));
        getInstance().message.setText("Seeking username validation...");
    }

    public static NameSelectionController getInstance() {
        if (instance == null) {
            instance = new NameSelectionController();
        }

        return instance;
    }

    public void showError() {
        Platform.runLater(() -> {
            getInstance().whirlpool.setVisible(false);
            getInstance().message.setVisible(true);
            getInstance().message.setTextFill(Color.valueOf("#ff0000cc"));
            getInstance().message.setText("Username is invalid or already in use.");
        });
    }

    @Override
    public void reset() {
        Platform.runLater(() -> {
            getInstance().message.setVisible(false);
            getInstance().whirlpool.setVisible(false);
        });
    }
}
