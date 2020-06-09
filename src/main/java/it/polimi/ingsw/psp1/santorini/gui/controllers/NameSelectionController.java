package it.polimi.ingsw.psp1.santorini.gui.controllers;

import it.polimi.ingsw.psp1.santorini.gui.EnumScene;
import it.polimi.ingsw.psp1.santorini.gui.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class NameSelectionController extends GuiController {

    private static NameSelectionController instance;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button button;

    @FXML
    private void onButtonClick(ActionEvent event) {
        Gui.getInstance().changeSceneAsync(EnumScene.CREATE_JOIN, EnumTransition.UP);

        getInstance().notifyObservers(o -> o.onNameSelection(nameTextField.getText()));
    }

    public static NameSelectionController getInstance() {
        if (instance == null) {
            instance = new NameSelectionController();
        }

        return instance;
    }

    @Override
    public void reset() {

    }
}
