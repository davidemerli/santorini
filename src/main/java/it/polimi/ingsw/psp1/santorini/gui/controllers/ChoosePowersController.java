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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Scene used to select gods
 */
public class ChoosePowersController extends GuiController {

    private static ChoosePowersController instance;
    private List<Power> selectedPowers;

    @FXML
    private AnchorPane descriptionPane;
    @FXML
    private FlowPane flowPane;
    @FXML
    private Label powerName;
    @FXML
    private Text description;
    @FXML
    private Button confirmButton;
    @FXML
    private Button selectButton;
    @FXML
    private ImageView powerImageLabel;

    private Power hovered;

    private Map<Power, ImageView[]> frames;

    private int selectSize;

    /**
     * @return Singleton instance for this controller
     */
    public static ChoosePowersController getInstance() {
        if (instance == null) {
            instance = new ChoosePowersController();
        }

        return instance;
    }

    /**
     * Initializes the controller
     */
    @FXML
    private void initialize() {
        getInstance().selectSize = -1;

        getInstance().selectButton = selectButton;
        getInstance().confirmButton = confirmButton;
        getInstance().powerName = powerName;
        getInstance().description = description;
        getInstance().descriptionPane = descriptionPane;
        getInstance().flowPane = flowPane;
        getInstance().powerImageLabel = powerImageLabel;
        getInstance().selectedPowers = new ArrayList<>();
        getInstance().frames = new HashMap<>();

        getInstance().confirmButton.setDisable(true);
    }

    /**
     * Adds the gods to the screen
     *
     * @param powers     power list
     * @param selectSize size
     */
    public void addGods(List<Power> powers, int selectSize) {
        getInstance().selectSize = selectSize;

        Platform.runLater(() -> {
            getInstance().confirmButton.setDisable(!canStartGame());

            for (Power power : powers) {
                System.out.println(power);
                String framePath = getClass().getResource("/gui_assets/clp_frame_white.png").toString();
                String selectedFramePath = getClass().getResource("/gui_assets/clp_frame_blue.png").toString();

                String powerPath = getClass().getResource("/gui_assets/god_cards/with_background/"
                        + power.getName() + ".png").toString();


                ImageView frame = new ImageView(framePath);
                ImageView selectedFrame = new ImageView(selectedFramePath);

                ImageView image = new ImageView(powerPath);
                image.setSmooth(true);

                frame.fitWidthProperty().bind(getInstance().flowPane.widthProperty().subtract(2).divide(3));
                frame.setPreserveRatio(true);
                selectedFrame.fitWidthProperty().bind(getInstance().flowPane.widthProperty().subtract(2).divide(3));
                selectedFrame.setPreserveRatio(true);
                image.fitWidthProperty().bind(getInstance().flowPane.widthProperty().subtract(2).divide(3).subtract(10));
                image.setPreserveRatio(true);

                AnchorPane pane = new AnchorPane(image, frame, selectedFrame);
                AnchorPane.setTopAnchor(image, 5D);
                AnchorPane.setLeftAnchor(image, 5D);

                FadeTransition ft = new FadeTransition(Duration.millis(400), selectedFrame);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.setInterpolator(Interpolator.EASE_BOTH);
                ft.setAutoReverse(true);
                ft.setCycleCount(Animation.INDEFINITE);
                ft.play();

                selectedFrame.setVisible(false);

                getInstance().frames.put(power, new ImageView[]{frame, selectedFrame});

                pane.setOnMouseClicked(mouseEvent -> {

                    getInstance().hovered = power;

                    getInstance().selectButton.setText(selectedPowers.contains(power) ? "UNSELECT POWER" : "SELECT POWER");

                    selectButton.setDisable(canStartGame() && !selectedPowers.contains(power));

                    getInstance().frames.forEach((p, frames) -> {
                        frames[0].setVisible(true);
                        frames[1].setVisible(selectedPowers.contains(p));
                    });

                    frame.setVisible(false);
                    selectedFrame.setVisible(true);

                    String iconPath = getClass().getResource("/gui_assets/god_cards/power_icon/"
                            + power.getName().toLowerCase() + "_icon.png").toString();

                    getInstance().powerName.setText(power.getName().toUpperCase());
                    getInstance().description.setText(power.getDescription());
                    getInstance().powerImageLabel.setImage(new Image(iconPath));

                    if (mouseEvent.isShiftDown()) {//TODO: change this selection method
                        if (getInstance().selectedPowers.size() < selectSize) {
                            getInstance().selectedPowers.add(power);
                        }
                    }

                    getInstance().confirmButton.setDisable(!canStartGame());
                });

                getInstance().flowPane.getChildren().add(pane);
            }
        });
    }

    /**
     * Confirms power choosing
     *
     * @param event gui click event
     */
    @FXML
    void clickConfirm(ActionEvent event) {
        getInstance().notifyObservers(o -> o.selectPowers(getInstance().selectedPowers));

        Gui.getInstance().changeSceneSync(EnumScene.WAIT_GOD_SELECTION);
    }


    /**
     * Selects a power
     *
     * @param event gui click event
     */
    @FXML
    private void clickSelect(ActionEvent event) {
        if (getInstance().hovered != null && getInstance().selectSize != -1) {
            if (!getInstance().selectedPowers.contains(getInstance().hovered)) {
                getInstance().selectedPowers.add(getInstance().hovered);
                getInstance().selectButton.setText("UNSELECT POWER");

                String selectedFramePath = getClass().getResource("/gui_assets/clp_frame_red.png").toString();
                getInstance().frames.get(getInstance().hovered)[1].setImage(new Image(selectedFramePath));

            } else {
                String selectedFramePath = getClass().getResource("/gui_assets/clp_frame_blue.png").toString();
                getInstance().frames.get(getInstance().hovered)[1].setImage(new Image(selectedFramePath));

                getInstance().selectedPowers.remove(getInstance().hovered);
                getInstance().selectButton.setText("SELECT POWER");

            }

            getInstance().confirmButton.setDisable(!canStartGame());
        }
    }


    @Override
    public void reset() {
        Platform.runLater(() -> {
            getInstance().powerImageLabel.setImage(null);
            getInstance().confirmButton.setDisable(true);
            getInstance().flowPane.getChildren().clear();
            getInstance().selectedPowers.clear();
            getInstance().description.setText("");
            getInstance().selectButton.setText("SELECT POWER");
            getInstance().selectButton.setDisable(true);
        });
    }

    /**
     * @return if the number of powers chosen is enough to start the game
     */
    private boolean canStartGame() {
        return getInstance().selectedPowers.size() >= getInstance().selectSize;
    }

}
