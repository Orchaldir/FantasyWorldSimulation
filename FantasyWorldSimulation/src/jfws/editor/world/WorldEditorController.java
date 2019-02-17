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
import jfws.util.math.geometry.mesh.Face;
import jfws.util.math.geometry.mesh.Mesh;
import jfws.util.math.geometry.mesh.Vertex;
import jfws.util.rendering.CanvasRenderer;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
public class WorldEditorController implements EditorController {

	@FXML
	private Canvas mapCanvas;

	private CanvasRenderer canvasRenderer;

	private Mesh mesh;

	private static final double X0 = 100.0;
	private static final double Y0 = 100.0;
	private static final double X1 = 300.0;
	private static final double Y1 = 100.0;
	private static final double X2 = 300.0;
	private static final double Y2 = 400.0;
	private static final double X3 = 200.0;
	private static final double Y3 = 500.0;
	private static final double X4 =  50.0;
	private static final double Y4 = 300.0;

	public WorldEditorController() {
		log.info("WorldEditorController()");

		mesh = new Mesh();

		mesh.createVertex(X0, Y0);
		mesh.createVertex(X1, Y1);
		mesh.createVertex(X2, Y2);
		mesh.createVertex(X3, Y3);
		mesh.createVertex(X4, Y4);

		mesh.createTriangle(0, 1, 2);
		mesh.createFace(Arrays.asList(0, 2, 3, 4));
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
		List<Face> faces = mesh.getFaces();

		log.info("render(): faces={}", faces.size());

		Random generator = new Random();

		for (Face face : faces) {
			List<Point2d> polygonPoints = face.getVerticesInCCW().stream().map(Vertex::getPoint).collect(Collectors.toList());

			log.info("render(): face={} points={}", face.getId(), polygonPoints.size());

			canvasRenderer.setColor(Color.rgb(generator.nextInt(255), generator.nextInt(255), generator.nextInt(255)));
			canvasRenderer.renderPolygon(polygonPoints);
		}

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
