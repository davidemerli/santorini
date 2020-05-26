package it.polimi.ingsw.psp1.santorini.gui;

import it.polimi.ingsw.psp1.santorini.gui.controllers.EnumTransition;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Gui extends Application {

    private static final Gui instance = new Gui();
    private static Stage primaryStage;

    private EnumScene currentScene;

    public static void launch(String[] args) {
        Application.launch("");
    }

    public static Gui getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        EnumScene.GAME.load();

        Gui.primaryStage = primaryStage;
        changeScene(EnumScene.IP_SELECT);

        primaryStage.setTitle("Santorini");
        primaryStage.show();
    }

    public void changeSceneAsync(EnumScene scene) {
        Platform.runLater(() -> {
            changeScene(scene);
        });
    }

    public void changeScene(EnumScene scene) {
        if(Objects.equals(currentScene, scene)) {
            return;
        }

        try {
            if (primaryStage.getScene() == null) {
                primaryStage.setScene(new Scene(scene.load()));
            } else {
                primaryStage.getScene().setRoot(scene.load());
            }

            currentScene = scene;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void changeScene(Pane scenePane) {
        Scene newScene = new Scene(scenePane, primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight());
        primaryStage.setScene(newScene);
    }

    public void changeSceneAsync(EnumScene scene, EnumTransition transition) {
        Platform.runLater(() -> {
            if(Objects.equals(currentScene, scene)) {
                return;
            }

            try {
                Parent toReplace = primaryStage.getScene().getRoot();
                AnchorPane anchor = new AnchorPane(toReplace);
                AnchorPane.setTopAnchor(toReplace, 0D);
                AnchorPane.setBottomAnchor(toReplace, 0D);
                AnchorPane.setLeftAnchor(toReplace, 0D);
                AnchorPane.setRightAnchor(toReplace, 0D);

                Pane currentPane = new StackPane(anchor);

                Parent root = scene.load();
                AnchorPane pane = new AnchorPane(root);
                AnchorPane.setTopAnchor(root, 0D);
                AnchorPane.setBottomAnchor(root, 0D);
                AnchorPane.setLeftAnchor(root, 0D);
                AnchorPane.setRightAnchor(root, 0D);
                changeScene(currentPane);

                Transition t = transition.getTransition(currentPane, pane);
                t.play();

                currentScene = scene;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}