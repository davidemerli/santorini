package it.polimi.ingsw.psp1.santorini.gui.controllers;

import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChoosePowersController extends GuiController {

    private static ChoosePowersController instance;
    private final List<Power> selectedPowers = new ArrayList<>();

    @FXML
    private AnchorPane descriptionPane;
    @FXML
    private FlowPane flowPane;
    @FXML
    private Text description;
    @FXML
    private ImageView fullImage;
    @FXML
    private HBox selectionBox;

    public static ChoosePowersController getInstance() {
        if (instance == null) {
            instance = new ChoosePowersController();
        }

        return instance;
    }

    public void addGods(List<Power> powers, int selectSize) {
        Platform.runLater(() -> {
            for (Power power : powers) {
                String url = getClass().getResource("/gui_assets/god_cards/with_background/"
                        + power.getName() + ".png").toString();

                ImageView image = new ImageView(url);

                image.setOnMouseClicked(mouseEvent -> {
                    description.setTextContent(power.getDescription());
                    fullImage = new ImageView(image.getImage());

                    if (mouseEvent.isSecondaryButtonDown()) {//TODO: change this selection method
                        if (selectSize > selectedPowers.size()) {
                            selectionBox.getChildren().add(new ImageView(image.getImage()));
                            selectedPowers.add(power);
                        }
                    }
                });

                flowPane.getChildren().add(image);
            }
        });
    }

    @FXML
    void clickConfirm(ActionEvent event) {
        notifyObservers(o -> o.selectPowers(selectedPowers));
    }
}
