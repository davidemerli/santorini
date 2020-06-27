package it.polimi.ingsw.psp1.santorini.gui.controllers;

import it.polimi.ingsw.psp1.santorini.gui.EnumScene;
import it.polimi.ingsw.psp1.santorini.gui.GuiObserver;
import it.polimi.ingsw.psp1.santorini.gui.RenderUtils;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumTurnState;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameSceneController extends GuiController {

    private static final ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();
    private static GameSceneController instance;
    private final List<Node> validMoves = new ArrayList<>();

    public Map<Point, Group> map = new HashMap<>();
    public Map<Group, Point> workers = new HashMap<>();

    private Group board;

    @FXML
    private SubScene mainScene;
    @FXML
    private AnchorPane pane;
    @FXML
    private Button menuButton;
    @FXML
    private Button interactButton;
    @FXML
    private Button undoButton;
    @FXML
    private ImageView requestBackground;
    @FXML
    private Label requestText;
    @FXML
    private GridPane menu;

    private boolean hasGameEnded;

    public static GameSceneController getInstance() {
        if (instance == null) {
            instance = new GameSceneController();
        }

        return instance;
    }

    @FXML
    public void initialize() {
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-45.5);

        board = RenderUtils.loadModel("/mesh/board.obj", "/textures/Cliff_v007.png");
        Group cliff = RenderUtils.loadModel("/mesh/cliff.obj", "/textures/Cliff_v007.png");
        Group sea = RenderUtils.loadModel("/mesh/sea.obj", "/textures/Sea_v006.png");
        Group islands = RenderUtils.loadModel("/mesh/islands.obj", "/textures/Islands_v002.png");
        sea.setTranslateY(2);
        islands.setTranslateY(2);

        Group root = new Group(board, cliff, sea, islands);

        mainScene = new SubScene(root, -1, -1, true, SceneAntialiasing.BALANCED);
        mainScene.setRoot(root);
        mainScene.setCamera(camera);
        mainScene.setFill(Color.valueOf("#33B7F0"));
        mainScene.widthProperty().bind(pane.widthProperty());
        mainScene.heightProperty().bind(pane.heightProperty());
        pane.getChildren().add(0, mainScene);

        RenderUtils.initMouseControl(root, mainScene);

        instance.requestBackground = requestBackground;
        instance.requestText = requestText;
        instance.interactButton = interactButton;
        instance.undoButton = undoButton;
        instance.menu = menu;
        instance.menuButton = menuButton;
        instance.mainScene = mainScene;
        instance.pane = pane;
        instance.board = board;
    }

    public void showValidMoves(List<Point> moves, List<Point> blockedMoves, EnumTurnState state) {
        runMapChange(() -> {
            instance.board.getChildren().removeAll(instance.validMoves);
            instance.validMoves.clear();

            for (Point p : moves) {
                boolean isBlocked = blockedMoves.contains(p);

                Point3D p3D = RenderUtils.convert2DTo3D(instance.board, p.x, p.y);
                Cylinder box = new Cylinder(1.15, 0.1);

                double y = -3.5 - (instance.map.get(p) == null ? 0 : instance.map.get(p).getLayoutBounds().getHeight());

                box.setTranslateX(p3D.getX());
                box.setTranslateY(y);
                box.setTranslateZ(p3D.getZ());

                Color color;
                if (state == EnumTurnState.BUILD) {
                    color = Color.valueOf("#ffe10099");
                } else {
                    color = Color.valueOf("#00bbff99");
                }

                PhongMaterial m = new PhongMaterial(isBlocked ? Color.valueOf("#57171799") : color,
                        null, null, null, null);
                box.setMaterial(m);

                Group move;

                if (isBlocked) {
                    Box block1 = new Box(1, 0.2, 0.5);
                    Box block2 = new Box(1, 0.2, 0.5);
                    block1.setRotationAxis(Rotate.Y_AXIS);
                    block1.setRotate(45);
                    block2.setRotationAxis(Rotate.Y_AXIS);
                    block2.setRotate(135);

                    block1.setTranslateX(p3D.getX());
                    block1.setTranslateY(y - 0.2);
                    block1.setTranslateZ(p3D.getZ());
                    block2.setTranslateX(p3D.getX());
                    block2.setTranslateY(y - 0.2);
                    block2.setTranslateZ(p3D.getZ());

                    block1.setMaterial(m);
                    block2.setMaterial(m);
                    move = new Group(box, block1, block2);
                } else {
                    move = new Group(box);
                }

                ScaleTransition st = new ScaleTransition(Duration.millis(500), move);
                st.setCycleCount(Animation.INDEFINITE);
                st.setAutoReverse(true);
                st.setFromX(0.8);
                st.setFromZ(0.8);
                st.setToX(1);
                st.setToZ(1);
                st.play();

                if (!isBlocked) {
                    box.setOnMouseClicked(event -> {
                        instance.notifyObservers(o -> o.onMoveSelected(p));

                        instance.board.getChildren().removeAll(instance.validMoves);
                        instance.validMoves.clear();
                    });
                }

                instance.validMoves.add(move);
            }

            instance.board.getChildren().addAll(validMoves);
        }, Duration.millis(200));
    }

    public void addWorker(int x, int y, Color color, boolean isOwn, boolean animate) {
        Duration duration = Duration.millis(200);

        runMapChange(() -> {
            Point p = new Point(x, y);

            if (workers.containsValue(p))
                return;

            Point3D p3D = RenderUtils.convert2DTo3D(instance.board, x, y);

            Group worker = RenderUtils.loadModel("/mesh/MaleBuilder_Blue.obj",
                    "/textures/MaleBuilder_Tan_v001.png",
                    color);

            Group blocks = map.get(p);
            double height = blocks != null && blocks.getChildren().size() > 0 ? -blocks.getLayoutBounds().getHeight() : 0;

            worker.getTransforms().add(new Translate(p3D.getX(), height, p3D.getZ()));
            worker.getTransforms().add(new Rotate(new Random().nextInt(360), Rotate.Y_AXIS));
            addWorkerClickAction(worker, p, isOwn);
            instance.board.getChildren().add(worker);

            if (animate) {
                TranslateTransition tt = new TranslateTransition(duration, worker);
                tt.setInterpolator(Interpolator.EASE_BOTH);
                tt.setFromY(-20);
                tt.setToY(worker.getLayoutY());
                tt.setOnFinished(event -> {
                    instance.workers.put(worker, p);
                });
                tt.play();
            } else {
                instance.workers.put(worker, p);
            }
        }, animate ? duration : Duration.millis(10));
    }

    public void addBlockAt(int x, int y, boolean forceDome, boolean animate) {
        runMapChange(() -> {
            Point p = new Point(x, y);
            Point3D p3D = RenderUtils.convert2DTo3D(instance.board, x, y);

            if (!instance.map.containsKey(p)) {
                Group g = new Group();
                instance.map.put(p, g);
                instance.board.getChildren().add(g);
            }

            Group group = instance.map.get(p);
            int level = forceDome ? 4 : (group.getChildren().size() + 1);

            Group block = RenderUtils.loadModel("/mesh/level" + level + ".obj",
                    "/textures/BuildingBlock0" + level + "_v001.png");
            double height = group.getChildren().size() > 0 ? -group.getLayoutBounds().getHeight() : 0;

            block.getTransforms().add(new Translate(p3D.getX(), height, p3D.getZ()));

            instance.map.get(p).getChildren().add(block);

            if (workers.containsValue(p)) {
                Group w = workers.keySet().stream().filter(g -> workers.get(g).equals(p)).findFirst().get();

                if (animate) {
                    TranslateTransition tt = new TranslateTransition(Duration.millis(200), w);
                    tt.setByY(-block.getLayoutBounds().getHeight());
                    tt.setInterpolator(Interpolator.EASE_OUT);

                    FadeTransition ft = new FadeTransition(Duration.millis(400), block);
                    ft.setFromValue(0);
                    ft.setToValue(1);

                    new SequentialTransition(tt, ft).play();
                }
            } else {
                if (animate) {
                    TranslateTransition tt = new TranslateTransition(Duration.millis(200), block);
                    tt.setInterpolator(Interpolator.EASE_IN);
                    tt.setFromY(-10);
                    tt.setToY(block.getLayoutY());

                    FadeTransition ft = new FadeTransition(Duration.millis(200), block);
                    ft.setFromValue(0);
                    ft.setToValue(1);

                    new ParallelTransition(tt, ft).play();
                }
            }
        }, animate ? Duration.millis(400) : Duration.millis(50));
    }

    public void moveWorker(Point from, Point to, boolean isOwn) {
        runMapChange(() -> {
            Group worker = workers.keySet().stream().filter(g -> workers.get(g).equals(from)).findFirst().get();

            Point3D from3D = RenderUtils.convert2DTo3D(instance.board, from.x, from.y);
            Point3D to3D = RenderUtils.convert2DTo3D(instance.board, to.x, to.y);

            Point3D diff = to3D.subtract(from3D);

            if (worker == null) {
                throw new NoSuchElementException("Worker not found at given position");
            }

            double heightFrom = instance.map.get(from) != null ? instance.map.get(from).getLayoutBounds().getHeight() : 0;
            double heightTo = instance.map.get(to) != null ? instance.map.get(to).getLayoutBounds().getHeight() : 0;
            double heightDiff = heightFrom - heightTo;

            TranslateTransition tt = new TranslateTransition(Duration.millis(heightDiff < 0 ? 400 : 250), worker);
            tt.setByX(diff.getX());
            tt.setByZ(diff.getZ());
            tt.setInterpolator(Interpolator.EASE_BOTH);

            TranslateTransition ttY = new TranslateTransition(Duration.millis(Math.abs(heightDiff) * (heightDiff < 0 ? 150 : 300)), worker);
            ttY.setByY(heightDiff);
            ttY.setInterpolator(Interpolator.EASE_BOTH);

            ParallelTransition pt = new ParallelTransition(tt, ttY);
            pt.setOnFinished(event -> workers.replace(worker, to));
            pt.play();

            addWorkerClickAction(worker, to, isOwn);
        }, Duration.millis(400));
    }

    public void showInteract(boolean show) {
        if (instance.interactButton == null) {
            return;
        }

        Platform.runLater(() -> instance.interactButton.setVisible(show));
    }

    public void showUndo(boolean show) {
        if (instance.undoButton == null) {
            return;
        }

        Platform.runLater(() -> instance.undoButton.setVisible(show));
    }

    public void showRequest(String request) {
        if (instance.requestBackground == null || instance.requestText == null) {
            return;
        }

        Platform.runLater(() -> {
            instance.requestBackground.setVisible(true);
            instance.requestText.setVisible(true);
            instance.requestText.setText(request);
        });
    }

    public void hideRequest() {
        if (instance.requestBackground == null || instance.requestText == null) {
            return;
        }

        Platform.runLater(() -> {
            instance.requestBackground.setVisible(false);
            instance.requestText.setVisible(false);
        });
    }

    private void runMapChange(Runnable toRun, Duration duration) {
        pool.submit(() -> {
            Platform.runLater(toRun);
        });

        pool.submit(() -> {
            try {
                Thread.sleep((long) duration.toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void showEndGame(String username, boolean hasWon) {
        instance.hasGameEnded = true;

        pool.schedule(() -> {
            Platform.runLater(() -> {
                try {
                    Parent pane = hasWon ? EnumScene.WIN.load() : EnumScene.LOSE.load();
                    WinLoseController.getInstance().setPlayerName(username);

                    instance.pane.setEffect(new GaussianBlur(22));

                    ScaleTransition st = new ScaleTransition(Duration.seconds(1), pane);
                    st.setFromX(0D);
                    st.setFromY(0D);
                    st.setFromZ(0D);
                    st.setToX(1D);
                    st.setToY(1D);
                    st.setToZ(1D);
                    st.play();
                    ((AnchorPane) instance.pane.getParent()).getChildren().add(pane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }, 1, TimeUnit.SECONDS);
    }

    private void addWorkerClickAction(Group worker, Point positionToSend, boolean isOwn) {
        Duration duration = Duration.millis(100);

        worker.setOnMouseClicked(event -> {
            runMapChange(() -> {
                if (isOwn) {
                    TranslateTransition selectAnimation = new TranslateTransition(duration, worker);
                    selectAnimation.setInterpolator(Interpolator.EASE_BOTH);
                    selectAnimation.setByY(-0.15);
                    selectAnimation.setCycleCount(4);
                    selectAnimation.setAutoReverse(true);

                    instance.notifyObservers(o -> o.onWorkerSelected(positionToSend));
                } else {
                    instance.notifyObservers(o -> o.onMoveSelected(positionToSend));
                }
            }, duration);
        });
    }

    public void setInteractButtonTexture(Power power) {
        Platform.runLater(() -> {
            String texture = "/gui_assets/god_cards/interactions/" + power.getInteractButton() + ".png";
            String defaultMessage = power.getInteraction().get(0).replaceAll(" ", "\n");
            int index = power.getInteraction().size() > 1 && instance.interactButton.getText().equals(defaultMessage)
                    ? 1 : 0;

            instance.interactButton.setText(power.getInteraction().get(index).replaceAll(" ", "\n"));

            instance.interactButton.setOnMouseClicked(event -> {
                if (power.getInteraction().size() > 1) {
                    instance.interactButton.setText(power.getInteraction().get(index).replaceAll(" ", "\n"));
                }
            });

            instance.interactButton.setStyle("-fx-background-size: 100% 100%;" +
                    "-fx-background-color: transparent;" +
                    String.format("-fx-background-image: url(%s);", texture));
        });
    }

    @FXML
    private void clickMenu(ActionEvent event) {
        instance.pane.setEffect(new GaussianBlur(22));
        instance.menu.setVisible(true);
    }

    @FXML
    private void clickResume(ActionEvent event) {
        instance.pane.setEffect(null);
        instance.menu.setVisible(false);
    }

    @FXML
    private void interactPressed(ActionEvent event) {
        instance.notifyObservers(GuiObserver::interactPressed);
    }

    @FXML
    private void undoPressed(ActionEvent event) {
        instance.notifyObservers(GuiObserver::undoPressed);
    }

    public boolean hasGameEnded() {
        return instance.hasGameEnded;
    }

    @Override
    public void reset() {
        Platform.runLater(() -> {
            instance.workers.forEach((g, p) -> instance.board.getChildren().remove(g));
            instance.workers.clear();

            instance.map.forEach((point, group) -> instance.board.getChildren().remove(group));
            instance.map.clear();

            if (hasGameEnded) {
                Pane rootPane = ((AnchorPane) instance.pane.getParent());
                rootPane.getChildren().remove(rootPane.getChildren().size() - 1);
            }

            hasGameEnded = false;
        });
    }
}