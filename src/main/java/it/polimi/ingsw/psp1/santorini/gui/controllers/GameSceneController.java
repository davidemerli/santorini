package it.polimi.ingsw.psp1.santorini.gui.controllers;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import it.polimi.ingsw.psp1.santorini.gui.GuiObserver;
import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class GameSceneController extends GuiController{

    private static GameSceneController instance;

    private final List<Node> validMoves = new ArrayList<>();

    private final DoubleProperty angleX = new SimpleDoubleProperty(40 * 4);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    public Map<Point, Group> map = new HashMap<>();
    public Map<Point, Group> workers = new HashMap<>();

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;

    private Group board;

    @FXML
    private SubScene mainScene;
    @FXML
    private AnchorPane pane;
    @FXML
    private Button button;
    @FXML
    private Button interactButton;
    @FXML
    private Button undoButton;
    @FXML
    private ImageView requestBackground;
    @FXML
    private Text requestText;

    public static GameSceneController getInstance() {
        if (instance == null) {
            instance = new GameSceneController();
        }

        return instance;
    }

    @FXML
    private void interactPressed(ActionEvent event) {
        getInstance().notifyObservers(GuiObserver::interactPressed);
    }

    @FXML
    private void undoPressed(ActionEvent event) {
        getInstance().notifyObservers(GuiObserver::undoPressed);
    }

    @FXML
    public void initialize() {
        getInstance().requestBackground = requestBackground;
        getInstance().requestText = requestText;
        getInstance().interactButton = interactButton;
        getInstance().undoButton = undoButton;

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-45.5);

        this.board = loadModel("/mesh/Board.obj", "/textures/Cliff_v007.png");
        Group cliff = loadModel("/mesh/cliff.obj", "/textures/Cliff_v007.png");
        Group sea = loadModel("/mesh/sea.obj", "/textures/Sea_v006.png");
        Group islands = loadModel("/mesh/islands.obj", "/textures/Islands_v002.png");
        sea.setTranslateY(2);
        islands.setTranslateY(2);

        Group root = new Group(board, cliff, sea, islands);

        mainScene = new SubScene(root, -1, -1, true, SceneAntialiasing.BALANCED);
        mainScene.setRoot(root);
        mainScene.setCamera(camera);
        mainScene.widthProperty().bind(pane.widthProperty());
        mainScene.heightProperty().bind(pane.heightProperty());
        pane.getChildren().add(1, mainScene);

        initMouseControl(root, mainScene);

        showValidMoves(List.of(new Point(0, 0), new Point(3, 2), new Point(4, 2)));

        button.prefWidthProperty().bind(pane.widthProperty().divide(20));
        button.prefHeightProperty().bind(pane.widthProperty().divide(20));
    }

    private Point3D convert2DTo3D(double x, double y) {
        double width = board.layoutBoundsProperty().getValue().getWidth() * 0.95;

        return new Point3D(x * width / 5, 0, y * width / 5)
                .subtract(width / 2, 0, width / 2)
                .add(width / 10, 0, width / 10);
    }

    public void showValidMoves(List<Point> moves) {
        double width = board.layoutBoundsProperty().getValue().getWidth() * 0.95;

        board.getChildren().removeAll(validMoves);
        validMoves.clear();

        for (Point p : moves) {
            Point3D p3D = convert2DTo3D(p.x, p.y);
            Cylinder box = new Cylinder(width / 15, 0.1);
            box.setTranslateX(p3D.getX());
            box.setTranslateY(-3.5 - (map.get(p) == null ? 0 : map.get(p).getLayoutBounds().getHeight()));
            box.setTranslateZ(p3D.getZ());
            PhongMaterial m = new PhongMaterial(Color.valueOf("#3DABFF99"),
                    null, null, null, null);
            box.setMaterial(m);

            ScaleTransition st = new ScaleTransition(Duration.millis(500), box);
            st.setCycleCount(Animation.INDEFINITE);
            st.setAutoReverse(true);
            st.setFromX(0.8);
            st.setFromZ(0.8);
            st.setToX(1);
            st.setToZ(1);
            st.play();

            box.setOnMouseClicked(event -> {
                notifyObservers(o -> o.onMoveSelected(p));
                if (event.isShiftDown() && event.isControlDown()) {
                    moveWorker(p, new Point(p.x + 1, p.y + 1));
                    return;
                }
                if (event.isControlDown()) {
                    addWorker(p.x, p.y, Color.GREEN, true);
                    return;
                }


                getInstance().notifyObservers(o -> o.onMoveSelected(p));

                int level = map.get(p) != null ? map.get(p).getChildren().size() + 1 : 1;

                addBlockAt(p.x, p.y, event.isShiftDown());

                board.getChildren().removeAll(validMoves);
                validMoves.clear();

                showValidMoves(new GameMap().getAllSquares().stream()
                        .filter(pp -> !map.containsKey(pp) || map.get(pp).getChildren().size() != 4)
                        .collect(Collectors.toList()));
            });

            validMoves.add(box);
        }

        board.getChildren().addAll(validMoves);
    }

    public void addWorker(int x, int y, Color color, boolean isOwn) {
        double width = board.layoutBoundsProperty().getValue().getWidth() * 0.95;
        Point p = new Point(x, y);

        Group worker = loadModel("/mesh/MaleBuilder_Blue.obj",
                "/textures/MaleBuilder_Tan_v001.png",
                color);

        worker.getTransforms().add(new Translate(
                -width / 2 + width / 10 + x * width / 5,
                0,
                -width / 2 + width / 10 + y * width / 5));

        if (isOwn) {
            worker.setOnMouseClicked(event -> {
                getInstance().notifyObservers(o -> o.onWorkerSelected(p));
            });
        }

        workers.put(p, worker);
        board.getChildren().add(worker);
    }

    public void addBlockAt(int x, int y, boolean forceDome) {
        double width = board.layoutBoundsProperty().getValue().getWidth() * 0.95;

        Point p = new Point(x, y);

        if (!map.containsKey(p)) {
            Group g = new Group();
            map.put(p, g);
            board.getChildren().add(g);
        }

        int level = forceDome ? 4 : (map.get(p).getChildren().size() + 1);

        Group block = loadModel("/mesh/level" + level + ".obj",
                "/textures/BuildingBlock0" + level + "_v001.png");

        block.getTransforms().add(new Translate(
                -width / 2 + width / 10 + x * width / 5,
                map.get(p).getChildren().size() > 0 ? -map.get(p).getLayoutBounds().getHeight() : 0,
                -width / 2 + width / 10 + y * width / 5));

        map.get(p).getChildren().add(block);

        TranslateTransition tt = new TranslateTransition(Duration.millis(400), block);
        tt.setInterpolator(Interpolator.EASE_IN);
        tt.setFromY(-20);
        tt.setToY(block.getLayoutY());
        tt.play();
    }

    public void moveWorker(Point from, Point to) {
        Group worker = workers.get(from);
        Point3D from3D = convert2DTo3D(from.x, from.y);
        Point3D to3D = convert2DTo3D(to.x, to.y);

        Point3D diff = to3D.subtract(from3D);

        if (worker == null) {
            throw new NoSuchElementException("Worker not found at given position");
        }

        double heightTo = map.get(to) != null ? -map.get(to).getLayoutBounds().getHeight() : 0;

        TranslateTransition tt = new TranslateTransition(Duration.millis(400), worker);
        tt.setByX(diff.getX());
        tt.setToY(heightTo);
        tt.setByZ(diff.getZ());
        tt.setInterpolator(Interpolator.TANGENT(Duration.millis(400), 2));
        tt.play();

        workers.remove(from);
        workers.put(to, worker);
    }

    private Group loadModel(String obj, String texture) {
        return loadModel(obj, texture, Color.BLACK);
    }

    private Group loadModel(String obj, String texture, Color color) {
        Group modelRoot = new Group();

        ObjModelImporter importer = new ObjModelImporter();
        importer.read(getClass().getResource(obj));

        for (MeshView view : importer.getImport()) {
            modelRoot.getChildren().add(view);

            Image im = new Image(getClass().getResourceAsStream(texture));


            PhongMaterial material = new PhongMaterial(color);
            material.setSelfIlluminationMap(im);
            view.setMaterial(material);
        }
        return modelRoot;
    }

    private void initMouseControl(Group group, SubScene scene) {
        Rotate rotateX = new Rotate(40 * 4, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);

        group.getTransforms().addAll(rotateX, rotateY);
        rotateX.angleProperty().bind(angleX.divide(4));
        rotateY.angleProperty().bind(angleY.divide(4));

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - anchorY + event.getSceneY());
            angleY.set(anchorAngleY + anchorX - event.getSceneX());

            if (angleX.get() > 90 * 4) {
                angleX.set(90 * 4);
            }

            if (angleX.get() < 40 * 4) {
                angleX.set(40 * 4);
            }
        });
    }

    public void showInteract(boolean show) {
        if(getInstance().interactButton == null) {
            return;
        }

        Platform.runLater(() -> getInstance().interactButton.setVisible(show));
    }

    public void showUndo(boolean show) {
        if(getInstance().undoButton == null) {
            return;
        }

        Platform.runLater(() -> getInstance().undoButton.setVisible(show));
    }

    public void showRequest(String request) {
        if (getInstance().requestBackground == null || getInstance().requestText == null) {
            return;
        }

        Platform.runLater(() -> {
            getInstance().requestBackground.setVisible(true);
            getInstance().requestText.setVisible(true);
            getInstance().requestText.setText(request);
        });
    }

    public void hideRequest() {
        if (getInstance().requestBackground == null || getInstance().requestText == null) {
            return;
        }

        Platform.runLater(() -> {
            getInstance().requestBackground.setVisible(false);
            getInstance().requestText.setVisible(false);
        });
    }
}
