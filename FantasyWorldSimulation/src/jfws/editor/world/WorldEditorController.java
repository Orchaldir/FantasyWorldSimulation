package jfws.editor.world;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Window;
import jfws.editor.map.EditorController;
import jfws.util.math.geometry.Point2d;
import jfws.util.rendering.CanvasRenderer;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class WorldEditorController implements EditorController {

	@FXML
	private Canvas mapCanvas;

	private CanvasRenderer canvasRenderer;

	private static final double X0 = 100.0;
	private static final double Y0 = 100.0;
	private static final double X1 = 120.0;
	private static final double Y1 = 100.0;
	private static final double X2 = 120.0;
	private static final double Y2 = 130.0;
	private static final double X3 = 110.0;
	private static final double Y3 = 140.0;
	private static final double X4 =  90.0;
	private static final double Y4 = 120.0;

	public WorldEditorController() {
		log.info("WorldEditorController()");
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		canvasRenderer = new CanvasRenderer(mapCanvas.getGraphicsContext2D());

		render();
	}

	@Override
	public BufferedImage getSnapshot() {
		WritableImage image = mapCanvas.snapshot(null, null);
		return SwingFXUtils.fromFXImage(image, null);
	}

	@Override
	public void saveSnapshot(File file, BufferedImage bufferedImage) throws IOException {
		ImageIO.write(bufferedImage, "PNG", file);
	}

	@Override
	public Window getWindow() {
		return null;
	}

	@Override
	public void render() {
		log.info("render()");

		ArrayList<Point2d> polygonPoints = new ArrayList<>(5);

		polygonPoints.add(new Point2d(X0, Y0));
		polygonPoints.add(new Point2d(X1, Y1));
		polygonPoints.add(new Point2d(X2, Y2));
		polygonPoints.add(new Point2d(X3, Y3));
		polygonPoints.add(new Point2d(X4, Y4));

		canvasRenderer.setColor(Color.RED);
		canvasRenderer.renderPolygon(polygonPoints);

		log.info("render(): Finished");
	}

	@Override
	public void showAlert(Alert.AlertType type, String title, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.showAndWait();
	}

	@FXML
	public void onExportImage() {
		log.info("onExportImage()");
	}
}
