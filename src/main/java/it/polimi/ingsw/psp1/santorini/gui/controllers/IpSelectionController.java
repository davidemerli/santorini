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
import javafx.scene.paint.Color;
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

    private boolean connecting;

    /**
     * @return Singleton instance for this controller
     */
    public static IpSelectionController getInstance() {
        if (instance == null) {
            instance = new IpSelectionController();
        }

        return instance;
    }

    /**
     * Initializes the controller
     */
    @FXML
    private void initialize() {
        instance.ipTextField = ipTextField;
        instance.portTextField = portTextField;
        instance.connectButton = connectButton;
        instance.connectionIcon = connectionIcon;
        instance.connectionText = connectionText;

        instance.ipTextField.setTextFormatter(getTextFormatter());
        instance.portTextField.setTextFormatter(getTextFormatter());
    }

    /**
     * @return a TextFormatter that allows only normal characters
     */
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

    /**
     * Hud button handling
     *
     * @param event gui event
     */
    @FXML
    private void buttonClick(ActionEvent event) {
        if (instance.connecting) {
            return;
        }

        String ip = instance.ipTextField.getText();
        int port = Integer.parseInt(instance.portTextField.getText());

        instance.notifyObservers(o -> o.connectToServer(ip, port));
    }

    /**
     * Starts the connection animation
     */
    public void startConnectionAnimation() {
        instance.connecting = true;

        Platform.runLater(() -> {
            instance.connectionText.setText("Connecting...");
            instance.connectionText.setFill(Color.valueOf("#000000cc"));
            instance.connectionIcon.setVisible(true);
            instance.connectionText.setVisible(true);

            RotateTransition rt = new RotateTransition(Duration.millis(400), instance.connectionIcon);
            rt.setByAngle(360);
            rt.setCycleCount(Animation.INDEFINITE);
            rt.play();
        });
    }

    /**
     * Stops the connection animation
     */
    public void stopConnectionAnimation() {
        instance.connectionIcon.setVisible(false);
        instance.connectionText.setVisible(false);
    }

    /**
     * Shows a connection error
     */
    public void showConnectionError() {
        instance.connectionText.setVisible(true);
        instance.connectionIcon.setVisible(false);
        instance.connectionText.setText("Connection Failed!");
        instance.connectionText.setFill(Color.valueOf("#ff0000cc"));

        instance.connecting = false;
    }

    /**
     * Changes scene
     */
    public void changeToNameSelection() {
        Gui.getInstance().changeSceneSync(EnumScene.NAME_SELECT);

        instance.connecting = false;
    }

    @Override
    public void reset() {
        //Not needed in this specific case
    }
}
