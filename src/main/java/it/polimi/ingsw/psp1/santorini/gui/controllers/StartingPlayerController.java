package it.polimi.ingsw.psp1.santorini.gui.controllers;

import it.polimi.ingsw.psp1.santorini.gui.EnumScene;
import it.polimi.ingsw.psp1.santorini.gui.Gui;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Scene used to choose the starting player
 */
public class StartingPlayerController extends GuiController {

    private static StartingPlayerController instance;

    @FXML
    private HBox playerBox;

    /**
     * @return Singleton instance for this controller
     */
    public static StartingPlayerController getInstance() {
        if (instance == null) {
            instance = new StartingPlayerController();
        }

        return instance;
    }

    /**
     * Initializes the controller
     */
    @FXML
    private void initialize() {
        getInstance().playerBox = playerBox;
    }

    /**
     * Adds player on screen
     * @param playerName  name of the player
     * @param playerPower player power
     */
    public void addPlayer(String playerName, Power playerPower) {
        Platform.runLater(() -> {
            String powerPath = getClass().getResource("/gui_assets/god_cards/with_background/"
                    + playerPower.getName() + ".png").toString();

            Text text = new Text(playerName);
            text.setFont(new Font("Segoe UI", 24));
            text.setTranslateY(-30);

            ImageView image = new ImageView(powerPath);

            image.setOnMouseClicked(mouseEvent -> {
                getInstance().notifyObservers(o -> o.selectStartingPlayer(playerName));
                Gui.getInstance().changeSceneSync(EnumScene.GAME);
            });

            VBox vbox = new VBox(image, text);
            vbox.setAlignment(Pos.CENTER);

            getInstance().playerBox.getChildren().add(vbox);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        Platform.runLater(() -> {
            getInstance().playerBox.getChildren().clear();
        });
    }
}
