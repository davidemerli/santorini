package it.polimi.ingsw.psp1.santorini.gui.controllers;

import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.LinkedHashMap;
import java.util.Map;

public class WaitGodSelectionController extends GuiController {

    private static WaitGodSelectionController instance;

    private Map<String, VBox> players;

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
            String framePath = getClass().getResource("/gui_assets/standard_power_frame.png").toString();
            String labelPath = getClass().getResource("/gui_assets/power_label.png").toString();
            String randomPath = getClass().getResource("/gui_assets/god_cards/with_background/Random.png").toString();

            Text text = new Text(playerName);
            text.setFont(new Font("Mount Olympus", 30));
            text.setFill(Color.valueOf("#3f3f3f"));
            text.setTranslateY(-60);

            ImageView frame = new ImageView(framePath);
            ImageView label = new ImageView(labelPath);
            ImageView image = new ImageView(randomPath);

            frame.setPreserveRatio(true);
            label.setPreserveRatio(true);
            image.setPreserveRatio(true);

            frame.setFitWidth(250);
            label.setFitWidth(240);
            image.setFitWidth(220);

            AnchorPane pane = new AnchorPane(frame);
            pane.getChildren().add(image);
            pane.getChildren().add(label);

            AnchorPane.setLeftAnchor(image, 15D);
            AnchorPane.setTopAnchor(image, 15D);

            AnchorPane.setLeftAnchor(label, 5D);
            AnchorPane.setBottomAnchor(label, 5D);

            VBox vbox = new VBox(pane, text);
            vbox.setAlignment(Pos.CENTER);

            getInstance().players.put(playerName, vbox);

            getInstance().imageBox.getChildren().add(vbox);
        });
    }

    public void addPlayerPower(String playerName, Power power) {
        Platform.runLater(() -> {
            String powerURL = getClass().getResource(String.format("/gui_assets/god_cards/with_background/%s.png",
                    power.getName())).toString();

            ImageView image = (ImageView) getInstance().players.get(playerName).getChildren().get(0);
            image.setImage(new Image(powerURL));
        });
    }

    public void reset() {
        if (getInstance().imageBox != null) {
            Platform.runLater(() -> {
                getInstance().imageBox.getChildren().clear();
                getInstance().players.forEach((key, value) -> value.getChildren().clear());
                getInstance().players.clear();
            });
        }
    }
}
