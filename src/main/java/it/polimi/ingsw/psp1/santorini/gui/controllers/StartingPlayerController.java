package it.polimi.ingsw.psp1.santorini.gui.controllers;

import it.polimi.ingsw.psp1.santorini.gui.EnumScene;
import it.polimi.ingsw.psp1.santorini.gui.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class StartingPlayerController extends GuiController {

    private static StartingPlayerController instance;


    @FXML
    private HBox playerBox;

    @FXML
    private void inizialize() {

    }

    public static StartingPlayerController getInstance() {
        if (instance == null) {
            instance = new StartingPlayerController();
        }

        return instance;
    }

    @FXML
    private void initialize() {
        getInstance().playerBox = playerBox;
    }

}
