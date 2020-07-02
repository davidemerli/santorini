package it.polimi.ingsw.psp1.santorini.gui;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import it.polimi.ingsw.psp1.santorini.model.Game;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Utils for the 3D game scene
 */
public class RenderUtils {

    private static final DoubleProperty angleX = new SimpleDoubleProperty(40 * 4);
    private static final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private static double anchorX, anchorY;
    private static double anchorAngleX = 0;
    private static double anchorAngleY = 0;

    /**
     * Generates a color from rainbow spectrum
     *
     * @param value determines the color with freq
     * @param freq frequency
     * @param p1 phase1
     * @param p2 phase2
     * @param p3 phase3
     * @param center center of the spectrum
     * @param width width of the spectrum
     * @return a color from rainbow hue
     */
    public static Color makeColorGradient(long value, double freq, double p1, double p2, double p3,
                                          int center, int width) {
        if (center == -1)
            center = 128;
        if (width == -1)
            width = 127;

        int r = (int) (Math.sin(freq * value + p1) * width) + center;
        int g = (int) (Math.sin(freq * value + p2) * width) + center;
        int b = (int) (Math.sin(freq * value + p3) * width) + center;

        return Color.rgb(r, g, b);
    }

    /**
     * Setups the map click rotate behaviour
     *
     * @param group model
     * @param scene with the model
     */
    public static void initMouseControl(Group group, SubScene scene) {
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

    /**
     * Converts a 2D Point to a 3D one on the board
     *
     * @param board the 3D board model
     * @param x coordinate x
     * @param y coordinate y
     * @return a Point3D
     */
    public static Point3D convert2DTo3D(Group board, double x, double y) {
        double sideLength = getSideLength(board) * 0.95;

        return new Point3D(x * sideLength / 5, 0, (4 - y) * sideLength / 5)
                .subtract(sideLength / 2, 0, sideLength / 2)
                .add(sideLength / 10, 0, sideLength / 10);
    }

    /**
     * Gets board side length
     *
     * @param group the game board
     * @return board side length
     */
    private static double getSideLength(Group group) {
        double width = group.layoutBoundsProperty().getValue().getWidth();
        double depth = group.layoutBoundsProperty().getValue().getDepth();

        return (width + depth) / 2;
    }

    /**
     * Loads a 3D .obj model
     *
     * @param obj 3D model file
     * @param texture to apply to model
     * @return loaded model
     */
    public static Group loadModel(String obj, String texture) {
        return loadModel(obj, texture, Color.BLACK);
    }

    /**
     * Loads a 3D .obj model
     *
     * @param obj 3D model file
     * @param texture to apply to model
     * @param color to apply to the texture
     * @return loaded model
     */
    public static Group loadModel(String obj, String texture, Color color) {
        Group modelRoot = new Group();

        ObjModelImporter importer = new ObjModelImporter();
        importer.read(RenderUtils.class.getResource(obj));

        for (MeshView view : importer.getImport()) {
            modelRoot.getChildren().add(view);

            Image im = new Image(RenderUtils.class.getResourceAsStream(texture));

            PhongMaterial material = new PhongMaterial(color);

            if (color == null) {
                Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
                    try {
                        long time = System.currentTimeMillis() / 50;
                        Color rainbowColor = RenderUtils.makeColorGradient(time, 0.3, 0, 2, 4, -1, -1).darker();
                        material.setDiffuseColor(rainbowColor);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }, 0, 100, TimeUnit.MILLISECONDS);
            }

            material.setSelfIlluminationMap(im);
            view.setMaterial(material);
        }

        return modelRoot;
    }
}
