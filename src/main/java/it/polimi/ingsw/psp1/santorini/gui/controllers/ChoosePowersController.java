package it.polimi.ingsw.psp1.santorini.gui.controllers;

import it.polimi.ingsw.psp1.santorini.gui.EnumScene;
import it.polimi.ingsw.psp1.santorini.gui.Gui;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class ChoosePowersController extends GuiController {

    private static ChoosePowersController instance;
    private List<Power> selectedPowers;

    @FXML
    private AnchorPane descriptionPane;
    @FXML
    private FlowPane flowPane;
    @FXML
    private Text description;
    @FXML
    private HBox selectionBox;
    @FXML
    private Button confirmButton;

    public static ChoosePowersController getInstance() {
        if (instance == null) {
            instance = new ChoosePowersController();
        }

        return instance;
    }

    @FXML
    private void initialize() {
        String url = getClass().getResource("/gui_assets/god_cards/with_background/Random.png").toString();

        getInstance().confirmButton = confirmButton;
        getInstance().description = description;
        getInstance().descriptionPane = descriptionPane;
        getInstance().flowPane = flowPane;
        getInstance().selectionBox = selectionBox;
        getInstance().selectedPowers = new ArrayList<>();
    }

    public void addGods(List<Power> powers, int selectSize) {
        getInstance().confirmButton.setDisable(getInstance().selectedPowers.size() > selectSize);

        Platform.runLater(() -> {
            for (Power power : powers) {
                System.out.println(power);
                String framePath = getClass().getResource("/gui_assets/clp_frame_white.png").toString();
                String selectedFramePath = getClass().getResource("/gui_assets/clp_frame_blue.png").toString();

                String powerPath = getClass().getResource("/gui_assets/god_cards/with_background/"
                        + power.getName() + ".png").toString();


                ImageView frame = new ImageView(framePath);
                ImageView selectedFrame = new ImageView(selectedFramePath);

                ImageView image = new ImageView(powerPath);

                frame.fitWidthProperty().bind(getInstance().flowPane.widthProperty().subtract(2).divide(3));
                frame.setPreserveRatio(true);
                selectedFrame.fitWidthProperty().bind(getInstance().flowPane.widthProperty().subtract(2).divide(3));
                selectedFrame.setPreserveRatio(true);
                image.fitWidthProperty().bind(getInstance().flowPane.widthProperty().subtract(2).divide(3).subtract(10));
                image.setPreserveRatio(true);

                AnchorPane pane = new AnchorPane(image, frame);
                AnchorPane.setTopAnchor(image, 5D);
                AnchorPane.setLeftAnchor(image, 5D);

                pane.setOnMouseClicked(mouseEvent -> {
                    getInstance().description.setText(power.getDescription());

                    frame.setImage(selectedFrame.getImage());

                    FadeTransition ft = new FadeTransition(Duration.millis(400), frame);
                    ft.setInterpolator(Interpolator.EASE_BOTH);
                    ft.setAutoReverse(true);
                    ft.setCycleCount(Animation.INDEFINITE);
                    ft.play();

                    if (mouseEvent.isShiftDown()) {//TODO: change this selection method
                        if (getInstance().selectedPowers.size() < selectSize) {
                            ImageView selectedImage = new ImageView(image.getImage());
                            selectedImage.fitWidthProperty().bind(image.fitWidthProperty().divide(1.5));
                            selectedImage.setPreserveRatio(true);
                            getInstance().selectionBox.getChildren().add(selectedImage);
                            getInstance().selectedPowers.add(power);
                        }
                    }

                    getInstance().confirmButton.setDisable(getInstance().selectedPowers.size() > selectSize);
                });

                getInstance().flowPane.getChildren().add(pane);
            }
        });
    }

    @FXML
    void clickConfirm(ActionEvent event) {
        getInstance().notifyObservers(o -> o.selectPowers(getInstance().selectedPowers));

        Gui.getInstance().changeSceneSync(EnumScene.WAIT_GOD_SELECTION);
    }

    @Override
    public void reset() {
        getInstance().confirmButton.setDisable(true);
        getInstance().flowPane.getChildren().clear();

        getInstance().selectionBox.getChildren().clear();
        getInstance().selectedPowers.clear();

        getInstance().description.setText("");
    }
}
