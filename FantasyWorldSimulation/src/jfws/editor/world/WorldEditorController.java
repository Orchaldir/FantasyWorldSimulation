package jfws.editor.world;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.mesh.Face;
import jfws.util.math.geometry.mesh.Mesh;
import jfws.util.math.geometry.mesh.Vertex;
import jfws.util.math.random.GeneratorWithRandom;
import jfws.util.rendering.CanvasRenderer;
import jfws.util.rendering.RandomColorSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class WorldEditorController {

	@FXML
	private Canvas mapCanvas;

	private CanvasRenderer canvasRenderer;

	private RandomColorSelector randomColorSelector;

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

		randomColorSelector = new RandomColorSelector(new GeneratorWithRandom(42));

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

	public void render() {
		List<Face> faces = mesh.getFaces();

		log.info("render(): faces={}", faces.size());

		for (Face face : faces) {
			List<Point2d> polygonPoints = face.getVerticesInCCW().stream().map(Vertex::getPoint).collect(Collectors.toList());

			log.info("render(): face={} points={}", face.getId(), polygonPoints.size());

			canvasRenderer.setColor(randomColorSelector.select(face));
			canvasRenderer.renderPolygon(polygonPoints);
		}

		log.info("render(): Finished");
	}

	@FXML
	public void onExportImage() {
		log.info("onExportImage()");
		render();
	}
}
