package it.polimi.ingsw.psp1.santorini.gui.controllers;

import it.polimi.ingsw.psp1.santorini.gui.EnumScene;
import it.polimi.ingsw.psp1.santorini.gui.Gui;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

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
        String ip = ipTextField.getText();
        int port = Integer.parseInt(portTextField.getText());

        getInstance().notifyObservers(o -> o.connectToServer(ip, port));
    }

    public void startConnectionAnimation() {
        getInstance().connectionIcon.setVisible(true);
        getInstance().connectionText.setVisible(true);

        RotateTransition rt = new RotateTransition(Duration.millis(400), connectionIcon);
        rt.setByAngle(360);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.play();
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
