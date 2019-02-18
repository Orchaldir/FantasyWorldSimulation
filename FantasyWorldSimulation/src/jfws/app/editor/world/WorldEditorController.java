package jfws.app.editor.world;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import jfws.util.math.geometry.mesh.Mesh;
import jfws.util.math.geometry.mesh.renderer.FaceRenderer;
import jfws.util.math.geometry.mesh.renderer.MeshRenderer;
import jfws.util.math.random.GeneratorWithRandom;
import jfws.util.rendering.CanvasRenderer;
import jfws.util.rendering.RandomColorSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class WorldEditorController {

	@FXML
	private Canvas mapCanvas;

	private CanvasRenderer canvasRenderer;

	private RandomColorSelector randomColorSelector;

	private Mesh mesh;

	private MeshRenderer meshRenderer;

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
	private static final double X5 = 400.0;
	private static final double Y5 = 500.0;

	public WorldEditorController() {
		log.info("WorldEditorController()");

		randomColorSelector = new RandomColorSelector(new GeneratorWithRandom(42));

		mesh = new Mesh();

		mesh.createVertex(X0, Y0);
		mesh.createVertex(X1, Y1);
		mesh.createVertex(X2, Y2);
		mesh.createVertex(X3, Y3);
		mesh.createVertex(X4, Y4);
		mesh.createVertex(X5, Y5);

		mesh.createTriangle(0, 1, 2);
		mesh.createFace(Arrays.asList(0, 2, 3, 4));
		mesh.createFace(Arrays.asList(1, 5, 3, 2));
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		canvasRenderer = new CanvasRenderer(mapCanvas.getGraphicsContext2D());
		FaceRenderer faceRenderer = new FaceRenderer(canvasRenderer);
		meshRenderer = new MeshRenderer(faceRenderer);

		render();
	}

	public void render() {
		log.info("render()");

		meshRenderer.renderFaces(mesh, randomColorSelector);

		log.info("render(): Finished");
	}

	@FXML
	public void onExportImage() {
		log.info("onExportImage()");
		render();
	}
}
