package it.polimi.ingsw.psp1.santorini.gui.controllers;

import it.polimi.ingsw.psp1.santorini.network.packets.server.PlayerData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.LinkedHashMap;
import java.util.Map;

public class WaitGodSelectionController extends GuiController {

    private static WaitGodSelectionController instance;

    private Map<PlayerData, VBox> players;

    @FXML
    private HBox imageBox;

    public static WaitGodSelectionController getInstance() {
        if (instance == null) {
            instance = new WaitGodSelectionController();
        }

        return instance;
    }

    @FXML
    private void initialize() {
        getInstance().imageBox = imageBox;
        getInstance().players = new LinkedHashMap<>();
    }

    public void addPlayer(String playerName) {
        Platform.runLater(() -> {
            String random = getClass().getResource("/gui_assets/god_cards/" +
                    "_0000s_0000_god_and_hero_cards_0057_Random.png").toString();

            Text text = new Text(playerName);
            text.setFont(new Font("Segoe UI", 24));
            text.setTranslateY(-30);

            ImageView image = new ImageView(random);

            VBox vbox = new VBox(image, text);
            vbox.setAlignment(Pos.CENTER);

            getInstance().imageBox.getChildren().add(vbox);
        });
    }
}
