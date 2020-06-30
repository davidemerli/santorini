package it.polimi.ingsw.psp1.santorini.gui.controllers;

import it.polimi.ingsw.psp1.santorini.gui.EnumScene;
import it.polimi.ingsw.psp1.santorini.gui.Gui;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manages the wait screen during the selection of gods
 */
public class WaitGodSelectionController extends GuiController {

    private static WaitGodSelectionController instance;

    @FXML
    private HBox imageBox;
    @FXML
    private ImageView whirlpool;
    @FXML
    private Label stateMessage;
    @FXML
    private Label gameRoom;

    private Map<String, PlayerPane> playerPanes;

    public static WaitGodSelectionController getInstance() {
        if (instance == null) {
            instance = new WaitGodSelectionController();
        }

        return instance;
    }

    @FXML
    private void initialize() {
        getInstance().imageBox = imageBox;
        getInstance().whirlpool = whirlpool;
        getInstance().gameRoom = gameRoom;
        getInstance().stateMessage = stateMessage;
        getInstance().playerPanes = new LinkedHashMap<>();

        RotateTransition rt = new RotateTransition();
        rt.setNode(whirlpool);
        rt.setByAngle(360);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.play();
    }

    /**
     * Adds player on screen
     *
     * @param playerName name of the player
     */
    public void addPlayer(String playerName) {
        Platform.runLater(() -> {
            String framePath = getClass().getResource("/gui_assets/standard_power_frame.png").toString();
            String labelPath = getClass().getResource("/gui_assets/power_label.png").toString();
            String randomPath = getClass().getResource("/gui_assets/god_cards/full/Random.png").toString();

            Text text = new Text(playerName);
            text.setFont(new Font("Mount Olympus", 30));
            text.setFill(Color.valueOf("#3f3f3f"));
            text.setTextAlignment(TextAlignment.CENTER);

            ImageView frame = new ImageView(framePath);
            ImageView label = new ImageView(labelPath);
            ImageView image = new ImageView(randomPath);

            HBox textBox = new HBox(text);
            textBox.setAlignment(Pos.CENTER);

            frame.setPreserveRatio(true);
            label.setPreserveRatio(true);
            image.setPreserveRatio(true);

            frame.setFitWidth(250);
            label.setFitWidth(240);
            image.setFitWidth(220);

            AnchorPane pane = new AnchorPane(frame);
            pane.getChildren().add(image);
            pane.getChildren().add(label);
            pane.getChildren().add(textBox);

            AnchorPane.setLeftAnchor(image, 15D);
            AnchorPane.setTopAnchor(image, 15D);

            AnchorPane.setLeftAnchor(label, 5D);
            AnchorPane.setBottomAnchor(label, 5D);

            AnchorPane.setBottomAnchor(textBox, 40D);
            AnchorPane.setLeftAnchor(textBox, 0D);
            AnchorPane.setRightAnchor(textBox, 0D);

            ScaleTransition st = new ScaleTransition(Duration.millis(200), pane);
            st.setFromX(0);
            st.setFromY(0);
            st.setFromZ(0);
            st.setToX(1);
            st.setToY(1);
            st.setToZ(1);
            st.play();

            getInstance().playerPanes.put(playerName, new PlayerPane(pane, image, false));

            getInstance().imageBox.getChildren().add(pane);
        });
    }

    /**
     * Sets player power and his image
     *
     * @param player current player
     * @param power  power choosen by player
     */
    public void setPlayerPower(String player, Power power) {
        if (getInstance().playerPanes.containsKey(player) && !playerPanes.get(player).updated) {
            Platform.runLater(() -> {
                String powerPath = getClass().getResource("/gui_assets/god_cards/full/"
                        + power.getName() + ".png").toString();

                getInstance().playerPanes.get(player).powerImage.setImage(new Image(powerPath));
            });
        }
    }

    /**
     * Manages the starting player scene
     */
    public void setupForStartingPlayerChoice() {
        Platform.runLater(() -> {
            getInstance().playerPanes.forEach((player, playerPane) -> {

                playerPane.pane.setScaleX(0.9);
                playerPane.pane.setScaleY(0.9);
                playerPane.pane.setScaleZ(0.9);

                playerPane.pane.setOnMouseClicked(mouseEvent -> {
                    getInstance().notifyObservers(o -> o.selectStartingPlayer(player));
                    Gui.getInstance().changeSceneSync(EnumScene.GAME);
                });

                playerPane.pane.setOnMouseEntered(event -> {
                    playerPane.pane.setViewOrder(-1);
                    ScaleTransition st = new ScaleTransition(Duration.millis(200), playerPane.pane);
                    st.setToX(1.1);
                    st.setToY(1.1);
                    st.setToZ(1.1);
                    st.play();
                });

                playerPane.pane.setOnMouseExited(event -> {
                    playerPane.pane.setViewOrder(0);

                    ScaleTransition st = new ScaleTransition(Duration.millis(200), playerPane.pane);
                    st.setToX(0.9);
                    st.setToY(0.9);
                    st.setToZ(0.9);
                    st.play();
                });
            });
        });
    }

    public void reset() {
        Platform.runLater(() -> {
            if (getInstance().imageBox != null) {
                getInstance().imageBox.getChildren().clear();
                getInstance().gameRoom.setText("");
                getInstance().playerPanes.clear();
            }
        });
    }

    /**
     * Shows room ID
     * @param ID room ID
     */
    public void showRoomID(String ID) {
        Platform.runLater(() -> {
            getInstance().gameRoom.setText("Game ID: " + ID);
            getInstance().gameRoom.setVisible(true);
        });
    }

    public void setStateMessage(String message) {
        Platform.runLater(() -> {
            getInstance().whirlpool.setVisible(true);

            getInstance().stateMessage.setText(message);
            getInstance().stateMessage.setVisible(true);
        });
    }

    public int getConnectedPlayersCount() {
        return playerPanes.size();
    }

    private static class PlayerPane {
        private final Pane pane;
        private final ImageView powerImage;
        private final boolean updated;

        public PlayerPane(Pane pane, ImageView powerImage, boolean updated) {
            this.pane = pane;
            this.powerImage = powerImage;
            this.updated = updated;
        }
    }
}
