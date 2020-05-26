package it.polimi.ingsw.psp1.santorini.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public enum EnumScene {

    NAME_SELECT("choose_name"),
    IP_SELECT("choose_ip"),
    CREATE_JOIN("create_join"),
    CHOOSE_POWERS("choose2"),
    GAME("game_scene"),
    TEST("test"),
    WAIT_GOD_SELECTION("wait_god_selection");

    String resource;
    Parent scene;

    EnumScene(String resource) {
        this.resource = resource;
    }

    public Parent load() throws IOException {
        if(scene == null) {
            scene = FXMLLoader.load(getClass().getResource("/scenes/" + resource + ".fxml"));
        }

        return scene;
    }
}
