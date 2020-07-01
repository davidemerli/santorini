package it.polimi.ingsw.psp1.santorini.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Arrays;

/**
 * Defines all possible scenes
 */
public enum EnumScene {

    NAME_SELECT("choose_name"),
    IP_SELECT("choose_ip"),
    CREATE_JOIN("create_join"),
    CHOOSE_POWERS("choose_powers"),
    WAIT_GOD_SELECTION("wait_god_selection"),
    STARTING_PLAYER("choose_starting_player"),
    GAME("game_scene"),
    WIN("win"),
    LOSE("lose"),
    TEST("test");

    static {
        Arrays.stream(values()).forEach(enumScene -> {
            try {
                enumScene.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    String resource;
    Parent scene;

    EnumScene(String resource) {
        this.resource = resource;
    }

    /**
     * Loads the scene
     * @return loaded scene
     * @throws IOException if file not found
     */
    public Parent load() throws IOException {
        if (scene == null) {
            scene = FXMLLoader.load(getClass().getResource("/scenes/" + resource + ".fxml"));
        }

        return scene;
    }
}
