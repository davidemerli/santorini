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
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Manages the game 3D scene
 */
public class GameSceneController extends GuiController {

    private static final ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();
    private static final ExecutorService undoThreadPool = Executors.newSingleThreadExecutor();

    private static GameSceneController instance;
    private List<Node> validMoves;

    private Map<Point, Group> map;
    private Map<Group, Point> workers;

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
    private Label undoLabel;
    @FXML
    private GridPane menu;
    @FXML
    private VBox playerIcons;

    private String oldInteractLabel;

    private Map<String, TranslateTransition> playerPanes;

    private boolean hasGameEnded;
    private RotateTransition undoRotate;
    private Future<?> changeUndoLabel;

    /**
     * @return Singleton instance for this controller
     */
    public static GameSceneController getInstance() {
        if (instance == null) {
            instance = new GameSceneController();
        }

        return instance;
    }

    /**
     * Loads the board and all textures
     * Makes possible the rotation of the stage
     */
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
        instance.undoLabel = undoLabel;
        instance.playerIcons = playerIcons;
        instance.playerPanes = new HashMap<>();
        instance.validMoves = new ArrayList<>();
        instance.workers = new HashMap<>();
        instance.map = new HashMap<>();

        instance.undoRotate = new RotateTransition(Duration.millis(1000), instance.undoButton);
        instance.undoRotate.setByAngle(180);
        instance.undoRotate.setInterpolator(Interpolator.EASE_BOTH);
        instance.undoRotate.setAutoReverse(true);
        instance.undoRotate.setCycleCount(5);
    }

    /**
     * Converts all 2D points in 3D points and shows all valid moves for the selected worker
     *
     * @param moves        valid moves list
     * @param blockedMoves blocked moves list
     * @param state        current state
     */
    public void showValidMoves(List<Point> moves, List<Point> blockedMoves, EnumTurnState state) {
        runMapChange(() -> {
            instance.board.getChildren().removeAll(instance.validMoves);
            instance.validMoves.clear();

            for (Point p : moves) {
                boolean isBlocked = blockedMoves.contains(p);

                Point3D p3D = RenderUtils.convert2DTo3D(instance.board, p.x, p.y);
                Cylinder box = new Cylinder(1.15, 0.1);

                double y = -3.5 - getHeight(p);

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

                if (isBlocked) { //draws red move and and X on it
                    Box block1 = new Box(2, 0.2, 0.5);
                    Box block2 = new Box(2, 0.2, 0.5);
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
                        if (instance.workers.containsValue(p)) {
                            instance.notifyObservers(o -> o.onWorkerSelected(p));
                        }

                        instance.notifyObservers(o -> o.onMoveSelected(p));

                        instance.board.getChildren().removeAll(instance.validMoves);
                        instance.validMoves.clear();
                    });
                }

                instance.validMoves.add(move);
            }

            instance.board.getChildren().addAll(instance.validMoves);
        }, Duration.millis(200));
    }

    /**
     * Adds a worker on the map
     *
     * @param x       x coordinate
     * @param y       y coordinate
     * @param color   worker color
     * @param isOwn   true if is one of my worker
     * @param animate true if must use the animation
     */
    public void addWorker(int x, int y, Color color, boolean isOwn, boolean animate) {
        Duration duration = Duration.millis(200);

        runMapChange(() -> {
            Point p = new Point(x, y);

            if (instance.workers.containsValue(p))
                return;

            Point3D p3D = RenderUtils.convert2DTo3D(instance.board, x, y);

            Group worker = RenderUtils.loadModel("/mesh/builder.obj",
                    "/textures/MaleBuilder_Tan_v001.png",
                    color);

            Group blocks = instance.map.get(p);
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
                tt.setOnFinished(event -> instance.workers.put(worker, p));
                tt.play();
            } else {
                instance.workers.put(worker, p);
            }
        }, animate ? duration : Duration.millis(10));
    }

    /**
     * @param x         x coordinate
     * @param y         y coordinate
     * @param forceDome true if a dome must be built
     * @param animate   true if must use the animation
     */
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

            if (instance.workers.containsValue(p)) {
                Group w = instance.workers.keySet().stream()
                        .filter(g -> instance.workers.get(g).equals(p)).findFirst().get();

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

    /**
     * Moves a worker on the map
     *
     * @param from  initial position
     * @param to    final position
     * @param isOwn true if is one of my worker
     * @throws NoSuchElementException if there is not worker at given position
     */
    public void moveWorker(Point from, Point to, boolean isOwn) {
        Optional<Group> workerOnDestination = instance.workers.keySet().stream()
                .filter(g -> instance.workers.get(g).equals(to)).findFirst();

        runMapChange(() -> {
            Optional<Group> optWorker = instance.workers.keySet().stream()
                    .filter(g -> instance.workers.get(g).equals(from)).findFirst();

            if (optWorker.isEmpty()) {
                throw new NoSuchElementException("Worker not found at given position");
            }

            Group worker = optWorker.get();

            Point3D from3D = RenderUtils.convert2DTo3D(instance.board, from.x, from.y);
            Point3D to3D = RenderUtils.convert2DTo3D(instance.board, to.x, to.y);

            Point3D diff = to3D.subtract(from3D);


            double heightFrom = instance.map.get(from) != null ? instance.map.get(from).getLayoutBounds().getHeight() : 0;
            double heightTo = instance.map.get(to) != null ? instance.map.get(to).getLayoutBounds().getHeight() : 0;
            double heightDiff = heightFrom - heightTo;

            TranslateTransition tt = new TranslateTransition(Duration.millis(heightDiff < 0 ? 400 : 250), worker);
            tt.setByX(diff.getX());
            tt.setByZ(diff.getZ());
            tt.setInterpolator(Interpolator.EASE_BOTH);

            double duration = Math.abs(heightDiff) * (heightDiff < 0 ? 150 : 300);
            TranslateTransition ttY = new TranslateTransition(Duration.millis(duration), worker);
            ttY.setByY(heightDiff);
            ttY.setInterpolator(Interpolator.EASE_BOTH);

            ParallelTransition pt = new ParallelTransition(tt, ttY);
            pt.setOnFinished(event -> instance.workers.replace(worker, to));
            pt.play();

            addWorkerClickAction(worker, to, isOwn);
        }, workerOnDestination.isPresent() ? Duration.millis(50) : Duration.millis(400));
    }

    /**
     * Manages the interaction button
     *
     * @param power player power
     * @param show  true if the interaction button must be shown
     */
    public void showInteract(Power power, boolean show) {
        if (instance.interactButton == null || power == null) {
            return;
        }

        Platform.runLater(() -> {
            instance.interactButton.setText(power.getInteraction().get(0));
            instance.interactButton.setVisible(show);
            instance.interactButton.setEffect(null);
        });
    }

    /**
     * Manages the undo button
     *
     * @param show true if the undo button must be shown
     */
    public void showUndo(boolean show) {
        if (instance.undoButton == null) {
            return;
        }

        Platform.runLater(() -> instance.undoButton.setDisable(!show));
    }

    /**
     * Manages the requests on screen
     *
     * @param request to be shown
     */
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

    /**
     * Hides the request banner
     */
    public void hideRequest() {
        if (instance.requestBackground == null || instance.requestText == null) {
            return;
        }

        Platform.runLater(() -> {
            instance.requestBackground.setVisible(false);
            instance.requestText.setVisible(false);
        });
    }

    /**
     * Enqueues a map animation
     * Used to keep animations one after another
     *
     * @param toRun    the map change function
     * @param duration how much the change takes
     */
    private void runMapChange(Runnable toRun, Duration duration) {
        pool.execute(() -> Platform.runLater(toRun));

        pool.execute(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep((long) duration.toMillis());
            } catch (InterruptedException ignored) {
            }
        });
    }

    /**
     * If the game has ended, shows the end game screen
     *
     * @param username player username
     * @param hasWon   true if the player has won
     */
    public void showEndGame(String username, boolean hasWon) {
        instance.hasGameEnded = true;

        pool.schedule(() -> Platform.runLater(() -> {
            try {
                Parent winOrLose = hasWon ? EnumScene.WIN.load() : EnumScene.LOSE.load();
                BorderPane pane = new BorderPane(winOrLose);
                AnchorPane.setTopAnchor(pane, 0D);
                AnchorPane.setBottomAnchor(pane, 0D);
                AnchorPane.setLeftAnchor(pane, 0D);
                AnchorPane.setRightAnchor(pane, 0D);

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
        }), 300, TimeUnit.MILLISECONDS);
    }

    /**
     * Setup worker 3D model click handling
     *
     * @param worker         the worker model
     * @param positionToSend position of the worker
     * @param isOwn          if the worker is one of the current player's
     */
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

    /**
     * Changes interact button texture
     *
     * @param power that defines the interaction behaviour
     */
    public void setInteractButtonTexture(Power power) {
        Platform.runLater(() -> {
            String texture = "/gui_assets/god_cards/interactions/" + power.getInteractButton() + ".png";

            instance.interactButton.setStyle("-fx-background-size: 100% 100%;" +
                    "-fx-background-color: transparent;" +
                    String.format("-fx-background-image: url(%s);", texture));
        });
    }

    /**
     * Hud button handling
     *
     * @param event gui event
     */
    @FXML
    private void clickMenu(ActionEvent event) {
        instance.pane.setEffect(new GaussianBlur(22));
        instance.menu.setVisible(true);
    }

    /**
     * Hud button handling
     *
     * @param event gui event
     */
    @FXML
    private void clickResume(ActionEvent event) {
        instance.pane.setEffect(null);
        instance.menu.setVisible(false);
    }

    /**
     * Manages the undo time
     * Player can use the undo function within 5 seconds
     */
    public void setupUndoTimer() {
        Platform.runLater(() -> {
            instance.undoRotate.playFromStart();
        });

        instance.changeUndoLabel = undoThreadPool.submit(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    int value = 5 - i;
                    Platform.runLater(() -> instance.undoLabel.setText("" + value));
                    TimeUnit.SECONDS.sleep(1);
                }
                Platform.runLater(() -> instance.undoLabel.setText("0"));
                TimeUnit.MILLISECONDS.sleep(200);

                Platform.runLater(() -> instance.undoLabel.setText(""));
            } catch (InterruptedException ignored) {
            }
        });
    }

    /**
     * Hud button handling
     *
     * @param event gui event
     */
    @FXML
    private void interactPressed(ActionEvent event) {
        instance.notifyObservers(GuiObserver::interactPressed);

        if (!instance.interactButton.getText().equals("Cancel")) {
            instance.oldInteractLabel = instance.interactButton.getText();
            instance.interactButton.setText("Cancel");
            instance.interactButton.setEffect(new Glow());
        } else {
            instance.interactButton.setText(instance.oldInteractLabel);
            instance.interactButton.setEffect(null);
        }
    }

    /**
     * Hud button handling
     *
     * @param event gui event
     */
    @FXML
    private void quitPressed(ActionEvent event) {
        instance.notifyObservers(GuiObserver::disconnect);
    }

    /**
     * Hud button handling
     *
     * @param event gui event
     */
    @FXML
    private void undoPressed(ActionEvent event) {
        instance.undoLabel.setText("");
        instance.notifyObservers(GuiObserver::undoPressed);

        if (instance.changeUndoLabel != null) {
            instance.changeUndoLabel.cancel(true);
            instance.undoRotate.jumpTo(Duration.seconds(5));
        }
    }

    /**
     * Manages the end of the game
     *
     * @return true if the game has ended
     */
    public boolean hasGameEnded() {
        return instance.hasGameEnded;
    }

    /**
     * Adds player on screen
     *
     * @param player to be added
     * @param color  player Color
     * @param power  player power
     * @param isOwn  if the player is the one playing the game
     */
    public void addPlayer(String player, Color color, Power power, boolean isOwn) {
        Platform.runLater(() -> {
            if (instance.playerPanes.containsKey(player)) {
                return;
            }

            String powerIcon = getClass().getResource("/gui_assets/god_cards/with_background/"
                    + power.getName() + ".png").toString();
            String frameIcon = getClass().getResource("/gui_assets/ingame_frame.png").toString();

            AnchorPane pane = new AnchorPane();
            ImageView icon = new ImageView(powerIcon);
            ImageView frame = new ImageView(frameIcon);

            Label name = new Label(player);

            name.setStyle("-fx-font-family: 'Mount Olympus'; -fx-font-size: 25;");
            name.setTextFill(color);

            HBox nameBox = new HBox(name);
            nameBox.setAlignment(Pos.CENTER);

            frame.setTranslateX(-20);
            frame.setPreserveRatio(true);
            frame.setFitWidth(isOwn ? 130 : 120);
            icon.setPreserveRatio(true);
            nameBox.minWidthProperty().bind(frame.fitWidthProperty().subtract(20));
            icon.fitWidthProperty().bind(frame.fitWidthProperty().subtract(20).multiply(0.9));

            AnchorPane.setTopAnchor(nameBox, 2.5D);
            AnchorPane.setTopAnchor(icon, 6D);
            AnchorPane.setLeftAnchor(icon, 3D);

            pane.getChildren().addAll(icon, frame, nameBox);

            instance.playerIcons.getChildren().add(pane);

            TranslateTransition st = new TranslateTransition(Duration.millis(400), pane);
            st.setToX(20);

            instance.playerPanes.put(player, st);
        });
    }

    /**
     * Used to reset map
     */
    public void resetMap() {
        Platform.runLater(() -> {
            instance.workers.forEach((g, p) -> instance.board.getChildren().remove(g));
            instance.workers.clear();

            instance.map.forEach((point, group) -> instance.board.getChildren().remove(group));
            instance.map.clear();
        });
    }

    /**
     * Shows the current player with an animation
     *
     * @param name of the player
     */
    public void highlightCurrentPlayer(String name) {
        Platform.runLater(() -> {
            if (instance.playerPanes.containsKey(name)) {
                for (TranslateTransition value : instance.playerPanes.values()) {
                    value.getNode().setViewOrder(-1);
                    value.setToX(0);
                    value.playFromStart();
                }

                TranslateTransition selected = instance.playerPanes.get(name);
                selected.setToX(20);
                selected.getNode().setViewOrder(0);
                selected.playFromStart();
            }
        });
    }

    /**
     * @param point on the map
     * @return the sum of the heights of the 3D blocks at a given position
     */
    private double getHeight(Point point) {
        return instance.map.get(point) == null ? 0 : instance.map.get(point).getLayoutBounds().getHeight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        resetMap();

        Platform.runLater(() -> {
            instance.playerPanes.clear();
            instance.playerIcons.getChildren().clear();

            if (instance.hasGameEnded) {
                instance.pane.toFront();
                instance.menu.toFront();
                instance.playerIcons.toFront();
//                Pane rootPane = ((AnchorPane) instance.pane.getParent());
//                rootPane.getChildren().remove(rootPane.getChildren().size() - 1);
                instance.pane.setEffect(null);
            }

            showValidMoves(List.of(), List.of(), EnumTurnState.END_GAME);

            instance.hasGameEnded = false;
        });
    }
}