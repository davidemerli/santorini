package it.polimi.ingsw.psp1.santorini.gui.controllers;

import it.polimi.ingsw.psp1.santorini.gui.EnumScene;
import it.polimi.ingsw.psp1.santorini.gui.Gui;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.stream.Stream;

public class WinLoseController extends GuiController {

    private static WinLoseController instance;

    @FXML
    private ImageView rightTrumpetWin;

    @FXML
    private ImageView leftTrumpetWin;

    @FXML
    private ImageView leftCloudWin;

    @FXML
    private ImageView rightCloudWin;

    @FXML
    private ImageView rightTrumpetLose;

    @FXML
    private ImageView leftTrumpetLose;

    @FXML
    private ImageView leftCloudLose;

    @FXML
    private ImageView rightCloudLose;

    @FXML
    private Label playerNameWin;

    @FXML
    private Label playerNameLose;

    public static WinLoseController getInstance() {
        if (instance == null) {
            instance = new WinLoseController();
        }

        return instance;
    }

    @FXML
    private void initialize() {
        initComponents(rightTrumpetWin, leftTrumpetWin, rightCloudWin, leftCloudWin);
        initComponents(rightTrumpetLose, leftTrumpetLose, rightCloudLose, leftCloudLose);

        if(playerNameWin != null) {
            getInstance().playerNameWin = playerNameWin;
        }

        if(playerNameLose != null) {
            getInstance().playerNameLose = playerNameLose;
        }
    }

    public void setPlayerName(String username) {
        Platform.runLater(() -> {
            if (getInstance().playerNameWin != null) {
                getInstance().playerNameWin.setText(username);
            }
            if (getInstance().playerNameLose != null) {
                getInstance().playerNameLose.setText(username);
            }
        });
    }

    private void initComponents(ImageView rightTrumpet, ImageView leftTrumpet, ImageView rightCloud, ImageView leftCloud) {
        RotateTransition rt1 = new RotateTransition(Duration.seconds(3), rightTrumpet);
        RotateTransition rt2 = new RotateTransition(Duration.seconds(3), leftTrumpet);
        TranslateTransition tt1 = new TranslateTransition(Duration.seconds(3), rightTrumpet);
        TranslateTransition tt2 = new TranslateTransition(Duration.seconds(3), leftTrumpet);
        TranslateTransition tt3 = new TranslateTransition(Duration.seconds(3), rightCloud);
        TranslateTransition tt4 = new TranslateTransition(Duration.seconds(3), leftCloud);

        rt1.setByAngle(-15);
        rt2.setByAngle(15);

        Stream.of(tt2, rt1, tt4).forEach(t -> {
            t.setDelay(Duration.seconds(1));
        });

        Stream.of(rt1, rt2).forEach(rt -> {
            rt.setCycleCount(Animation.INDEFINITE);
            rt.setAutoReverse(true);
            rt.setInterpolator(Interpolator.EASE_BOTH);
            rt.play();
        });

        Stream.of(tt3, tt4).forEach(tt -> {
            tt.setFromX(-20);
            tt.setByX(20);
            tt.setCycleCount(Animation.INDEFINITE);
            tt.setAutoReverse(true);
            tt.play();
        });

        Stream.of(tt1, tt2).forEach(tt -> {
            tt.setFromX(-10);
            tt.setByX(10);
            tt.setCycleCount(Animation.INDEFINITE);
            tt.setInterpolator(Interpolator.EASE_OUT);
            tt.setAutoReverse(true);
            tt.play();
        });
    }

    @FXML
    void clickMainMenu(ActionEvent event) {
        Gui.getInstance().changeSceneSync(EnumScene.IP_SELECT);
        GameSceneController.getInstance().reset();
        WinLoseController.getInstance().reset();
    }

    @Override
    public void reset() {
        setPlayerName("");
    }
}
