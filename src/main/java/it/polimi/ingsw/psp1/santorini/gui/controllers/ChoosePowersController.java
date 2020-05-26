package it.polimi.ingsw.psp1.santorini.gui.controllers;

import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import org.w3c.dom.Text;

import java.util.List;

public class ChoosePowersController extends GuiController{
    private static ChooseGameSceneController instance;

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

    public static ChooseGameSceneController getInstance() {
        if (instance == null) {
            instance = new ChooseGameSceneController();
        }

        return instance;
    }

    @FXML
    public void addGods(List<Power> powers) {

//        for (Power power : powers) {
//            ImageView image = new ImageView(power.getName() + ".png");
//            image.setOnMouseClicked(mouseEvent -> {
//                // descrizione
//                // description.setText(power.getDescription())
//                // immagine full
//                // ImageView fullImage = new ImageView(power.getName() + "full" + ".png);
//                //
//            });
//            flowPane.getChildren().add(image);
//        }
        ImageView mortal = new ImageView(new Image("/gui_assets/god_cards/_0000s_0000_god_and_hero_cards_0057_Human.png"));
        ImageView apollo = new ImageView(new Image("/gui_assets/god_cards/_0000s_0043_god_and_hero_cards_0013_apollo.png"));
        ImageView artemis = new ImageView(new Image("/gui_assets/god_cards/_0000s_0054_god_and_hero_cards_0002_Artemis.png"));
        ImageView athena = new ImageView(new Image("/gui_assets/god_cards/_0000s_0052_god_and_hero_cards_0004_Athena.png"));
        ImageView atlas = new ImageView(new Image("/gui_assets/god_cards/_0000s_0053_god_and_hero_cards_0003_Atlas.png"));
        ImageView chronus = new ImageView(new Image("/gui_assets/god_cards/_0000s_0027_god_and_hero_cards_0029_Chronus.png"));
        ImageView demeter = new ImageView(new Image("/gui_assets/god_cards/_0000s_0050_god_and_hero_cards_0006_Demeter.png"));
        ImageView hephaestus = new ImageView(new Image("/gui_assets/god_cards/_0000s_0009_god_and_hero_cards_0047_Hephaestus.png"));
        ImageView hestia = new ImageView(new Image("/gui_assets/god_cards/_0000s_0017_god_and_hero_cards_0039_Hestia.png"));
        ImageView minotaur = new ImageView(new Image("/gui_assets/god_cards/_0000s_0008_god_and_hero_cards_0048_Minotaur.png"));
        ImageView pan = new ImageView(new Image("/gui_assets/god_cards/_0000s_0046_god_and_hero_cards_0010_Pan.png"));
        ImageView poseidon = new ImageView(new Image("/gui_assets/god_cards/_0000s_0045_god_and_hero_cards_0011_Poseidon.png"));
        ImageView prometheus = new ImageView(new Image("/gui_assets/god_cards/_0000s_0004_god_and_hero_cards_0052_Prometheus.png"));
        ImageView triton = new ImageView(new Image("/gui_assets/god_cards/_0000s_0028_god_and_hero_cards_0028_triton.png"));
        ImageView zeus = new ImageView(new Image("/gui_assets/god_cards/_0000s_0014_god_and_hero_cards_0042_zeus.png"));
        flowPane.getChildren().addAll(mortal, apollo, artemis, athena, atlas, chronus, demeter, hephaestus, hestia,
                minotaur, pan, poseidon, prometheus, triton, zeus);

        for (Node child : flowPane.getChildren()) {
            if(child instanceof ImageView) {
                child.setOnMouseClicked(mouseEvent -> {
                    // carica descrizione
                    ImageView image = (ImageView) child;
                    // description.setText(power.getDescription());
                    // carica immagine
                    fullImage = new ImageView(image.getImage());
                    if(mouseEvent.isSecondaryButtonDown()) {
                        selectionBox.getChildren().add(new ImageView(image.getImage()));
                    }
                    // TODO: powers
                });
            }
        }
    }

    @FXML
    void clickConfirm(ActionEvent event) {

    }
}
