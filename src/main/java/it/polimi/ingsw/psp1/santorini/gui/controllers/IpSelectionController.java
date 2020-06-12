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
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

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
        getInstance().connectButton = connectButton;
        getInstance().portTextField = portTextField;
        getInstance().connectionIcon = connectionIcon;
        getInstance().connectionText = connectionText;
    }

    @FXML
    private void buttonClick(ActionEvent event) {
        String ip = getInstance().ipTextField.getText();
        int port = Integer.parseInt(getInstance().portTextField.getText());

        getInstance().notifyObservers(o -> o.connectToServer(ip, port));
    }

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

    public void stopConnectionAnimation() {
        getInstance().connectionIcon.setVisible(false);
        getInstance().connectionText.setVisible(false);
    }

    public void changeToNameSelection() {
        Gui.getInstance().changeSceneAsync(EnumScene.NAME_SELECT, EnumTransition.DOWN);
    }

    @Override
    public void reset() {

    }
}
