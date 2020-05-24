package it.polimi.ingsw.psp1.santorini.gui.controllers;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class ChooseGameSceneController extends GuiController {
    private static ChooseGameSceneController instance;

    boolean toggleRight;
    boolean toggleLeft;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button createButton;
    @FXML
    private Button joinButton;
    @FXML
    private GridPane rightPane;
    @FXML
    private Button confirmCreate;
    @FXML
    private CheckBox joinCheckBox3;
    @FXML
    private CheckBox joinCheckBox2;
    @FXML
    private Button joinX;
    @FXML
    private GridPane leftPane;
    @FXML
    private Button confirmJoin;
    @FXML
    private CheckBox createCheckBox2;
    @FXML
    private CheckBox createCheckBox3;
    @FXML
    private Button createX;

    public static ChooseGameSceneController getInstance() {
        if (instance == null) {
            instance = new ChooseGameSceneController();
        }

        return instance;
    }

    @FXML
    public void initialize() {
//        leftPane.setTranslateX(-leftPane.getBoundsInParent().getWidth());
//        rightPane.setTranslateX(rightPane.getBoundsInParent().getWidth());
    }

    @FXML
    private void clickCreate(ActionEvent event) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(toggleLeft ? 200 : 300), leftPane);
        tt.setToX(toggleLeft ? 0 : leftPane.getBoundsInParent().getWidth());
        tt.play();

        toggleLeft = !toggleLeft;

        if (toggleRight) {
            TranslateTransition tt1 = new TranslateTransition(Duration.millis(200), rightPane);
            tt1.setToX(toggleRight ? 0 : -rightPane.getBoundsInParent().getWidth());
            tt1.play();

            toggleRight = !toggleRight;
        }
    }

    @FXML
    private void clickJoin(ActionEvent event) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(toggleRight ? 200 : 300), rightPane);
        tt.setToX(toggleRight ? 0 : -rightPane.getBoundsInParent().getWidth());
        tt.play();

        toggleRight = !toggleRight;

        if (toggleLeft) {
            TranslateTransition tt1 = new TranslateTransition(Duration.millis(200), leftPane);
            tt1.setToX(toggleLeft ? 0 : leftPane.getBoundsInParent().getWidth());
            tt1.play();

            toggleLeft = !toggleLeft;
        }
    }

    @FXML
    private void click(ActionEvent event) {
        if (event.getSource().equals(createCheckBox2)) {
            createCheckBox3.setSelected(false);
        }
        if (event.getSource().equals(createCheckBox3)) {
            createCheckBox2.setSelected(false);
        }
        if (event.getSource().equals(joinCheckBox2)) {
            joinCheckBox3.setSelected(false);
        }
        if (event.getSource().equals(joinCheckBox3)) {
            joinCheckBox2.setSelected(false);
        }

        confirmCreate.setDisable(!createCheckBox2.isSelected() && !createCheckBox3.isSelected());
        confirmJoin.setDisable(!joinCheckBox2.isSelected() && !joinCheckBox3.isSelected());
    }

    @FXML
    private void clickX(ActionEvent event) {
        if (event.getSource().equals(createX)) {
            clickCreate(event);
        }
        if (event.getSource().equals(joinX)) {
            clickJoin(event);
        }
    }

    @FXML
    private void createGame(ActionEvent event) {
        getInstance().notifyObservers(o -> o.createGame(createCheckBox2.isSelected() ? 2 : 3));
    }

    @FXML
    private void joinGame(ActionEvent event) {
        getInstance().notifyObservers(o -> o.joinGame(joinCheckBox2.isSelected() ? 2 : 3));
    }
}
