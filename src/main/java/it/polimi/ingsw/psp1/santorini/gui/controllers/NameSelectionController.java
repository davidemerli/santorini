package it.polimi.ingsw.psp1.santorini.gui.controllers;

import it.polimi.ingsw.psp1.santorini.gui.EnumScene;
import it.polimi.ingsw.psp1.santorini.gui.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class NameSelectionController extends GuiController {

    @FXML
    private TextField nameTextField;

    @FXML
    private Button button;

    @FXML
    private void onButtonClick(ActionEvent event) {
        Gui.getInstance().changeSceneAsync(EnumScene.CREATE_JOIN, EnumTransition.UP);

        notifyObservers(o -> o.onNameSelection(nameTextField.getText()));
    }

}
