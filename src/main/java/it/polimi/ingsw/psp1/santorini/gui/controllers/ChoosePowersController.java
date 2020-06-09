package it.polimi.ingsw.psp1.santorini.gui.controllers;

import it.polimi.ingsw.psp1.santorini.gui.EnumScene;
import it.polimi.ingsw.psp1.santorini.gui.Gui;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

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
    private ImageView fullImage;
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

        fullImage.setImage(new Image(url));
        description.wrappingWidthProperty().bind(fullImage.fitWidthProperty());

        getInstance().confirmButton = confirmButton;
        getInstance().description = description;
        getInstance().descriptionPane = descriptionPane;
        getInstance().flowPane = flowPane;
        getInstance().fullImage = fullImage;
        getInstance().selectionBox = selectionBox;
        getInstance().selectedPowers = new ArrayList<>();
    }

    public void addGods(List<Power> powers, int selectSize) {
        getInstance().confirmButton.setDisable(getInstance().selectedPowers.size() > selectSize);

        Platform.runLater(() -> {
            for (Power power : powers) {
                System.out.println(power);
                String url = getClass().getResource("/gui_assets/god_cards/with_background/"
                        + power.getName() + ".png").toString();

                ImageView image = new ImageView(url);
                image.fitWidthProperty().bind(getInstance().flowPane.widthProperty().divide(3));
                image.setPreserveRatio(true);

                image.setOnMouseClicked(mouseEvent -> {
                    getInstance().description.setText(power.getDescription());
                    getInstance().fullImage.setImage(image.getImage());

                    if (mouseEvent.isShiftDown()) {//TODO: change this selection method
                        if (getInstance().selectedPowers.size() < selectSize) {
                            ImageView selectedImage = new ImageView(image.getImage());
                            selectedImage.fitWidthProperty().bind(image.fitWidthProperty().divide(1.5));
                            selectedImage.setPreserveRatio(true);
                            getInstance().selectionBox.getChildren().add(selectedImage);
                            getInstance().selectedPowers.add(power);
                        }
                    }

                    image.setStyle("-fx-border-color: #010101");

                    getInstance().confirmButton.setDisable(getInstance().selectedPowers.size() > selectSize);
                });

                getInstance().flowPane.getChildren().add(image);
            }
        });
    }

    @FXML
    void clickConfirm(ActionEvent event) {
        getInstance().notifyObservers(o -> o.selectPowers(getInstance().selectedPowers));

        Gui.getInstance().changeSceneAsync(EnumScene.WAIT_GOD_SELECTION, EnumTransition.LEFT);
    }

    @Override
    public void reset() {
        getInstance().flowPane.getChildren().clear();

        getInstance().selectionBox.getChildren().clear();
        getInstance().selectedPowers.clear();

        getInstance().description.setText("");
        getInstance().fullImage.setImage(null);
    }
}
