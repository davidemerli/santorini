package it.polimi.ingsw.psp1.santorini.gui.controllers;

import it.polimi.ingsw.psp1.santorini.gui.EnumScene;
import it.polimi.ingsw.psp1.santorini.gui.Gui;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Scene used to select ip address and port
 */
public class IpSelectionController extends GuiController {

    private static IpSelectionController instance;

    @FXML
    private TextField ipTextField;

    @FXML
    private TextField portTextField;

    @FXML
    private Button connectButton;

    @FXML
    private ImageView connectionIcon;

    @FXML
    private Text connectionText;

    public static IpSelectionController getInstance() {
        if (instance == null) {
            instance = new IpSelectionController();
        }

        return instance;
    }

    @FXML
    private void initialize() {
        getInstance().ipTextField = ipTextField;
        getInstance().portTextField = portTextField;
        getInstance().connectButton = connectButton;
        getInstance().connectionIcon = connectionIcon;
        getInstance().connectionText = connectionText;



        getInstance().ipTextField.setTextFormatter(getTextFormatter());
        getInstance().portTextField.setTextFormatter(getTextFormatter());
    }

    private TextFormatter<String> getTextFormatter() {
        return new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }

            if (!change.getControlNewText().matches("[\\w\\d !#@$%*&()\\-_+=;:'\",<.>?/]*")) {
                return null;
            }

            return change;
        });
    }

    @FXML
    private void buttonClick(ActionEvent event) {
        String ip = getInstance().ipTextField.getText();
        int port = Integer.parseInt(getInstance().portTextField.getText());

        getInstance().notifyObservers(o -> o.connectToServer(ip, port));
    }

    /**
     * Starts the connection animation
     */
    public void startConnectionAnimation() {
        Platform.runLater(() -> {
            getInstance().connectionIcon.setVisible(true);
            getInstance().connectionText.setVisible(true);

            RotateTransition rt = new RotateTransition(Duration.millis(400), getInstance().connectionIcon);
            rt.setByAngle(360);
            rt.setCycleCount(Animation.INDEFINITE);
            rt.play();
        });
    }

    /**
     * Stops the connection animation
     */
    public void stopConnectionAnimation() {
        getInstance().connectionIcon.setVisible(false);
        getInstance().connectionText.setVisible(false);
    }

    /**
     * Changes scene
     */
    public void changeToNameSelection() {
        Gui.getInstance().changeSceneSync(EnumScene.NAME_SELECT);
    }

    @Override
    public void reset() {

    }
}
