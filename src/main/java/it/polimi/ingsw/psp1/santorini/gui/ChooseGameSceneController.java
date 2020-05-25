package it.polimi.ingsw.psp1.santorini.gui;

import it.polimi.ingsw.psp1.santorini.gui.controllers.GuiController;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class ChooseGameSceneController extends GuiController {
    boolean toggleRight;
    boolean toggleLeft;

    private static ChooseGameSceneController instance ;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button joinButton;

    @FXML
    private Button createButton;

    @FXML
    private CheckBox createCheckBox2;

    @FXML
    private CheckBox createCheckBox3;

    @FXML
    private CheckBox joinCheckBox2;

    @FXML
    private CheckBox joinCheckBox3;

    @FXML
    private GridPane leftPane;

    @FXML
    private GridPane rightPane;

    @FXML
    private Button joinX;

    @FXML
    private Button createX;

    @FXML
    public void initialize() {

        // rightPane.prefWidthProperty().bind(rootPane.widthProperty().divide(2));
        // leftPane.prefHeightProperty().bind(rootPane.widthProperty().divide(2));
        // rightPane.prefHeightProperty().bind(rootPane.widthProperty().divide(2));
        // distanza bottoni
    }

    public static ChooseGameSceneController getInstance() {
        if (instance == null) {
            instance = new ChooseGameSceneController();
        }
        return instance;
    }

    @FXML
    void ClickCreate(ActionEvent event) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(toggleLeft ? 300 : 600), leftPane);
        tt.setToX(toggleLeft ? 0 : leftPane.getBoundsInParent().getWidth());
//        tt.setToX(0);
        tt.play();

        toggleLeft = !toggleLeft;
        if(toggleRight) {
            TranslateTransition tt1 = new TranslateTransition(Duration.millis(300), rightPane);
            tt1.setToX(toggleRight ? 0 : -rightPane.getBoundsInParent().getWidth());
            toggleRight = !toggleRight;
            tt1.play();
        }

    }

    @FXML
    void ClickJoin(ActionEvent event) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(toggleRight ? 300 : 600), rightPane);
        tt.setToX(toggleRight ? 0 : -rightPane.getBoundsInParent().getWidth());
//        tt.setToX(0);
        tt.play();

        toggleRight = !toggleRight;
        if(toggleLeft) {
            TranslateTransition tt1 = new TranslateTransition(Duration.millis(300), leftPane);
            tt1.setToX(toggleLeft ? 0 : leftPane.getBoundsInParent().getWidth());
            toggleLeft = !toggleLeft;
            tt1.play();
        }
    }

    @FXML
    void click(ActionEvent event) {
        if(event.getSource().equals(createCheckBox2)) {
            createCheckBox3.setSelected(false);
        }
        if(event.getSource().equals(createCheckBox3)) {
            createCheckBox2.setSelected(false);
        }
        if(event.getSource().equals(joinCheckBox2)) {
            joinCheckBox3.setSelected(false);
        }
        if(event.getSource().equals(joinCheckBox3)) {
            joinCheckBox2.setSelected(false);
        }
    }

    @FXML
    void clickX(ActionEvent event) {
        if(event.getSource().equals(createX)) {
            ClickCreate(event);
        }
        if(event.getSource().equals(joinX)) {
            ClickJoin(event);
        }
    }
}
