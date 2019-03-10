package jfws.app.demo.voronoi;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.Rectangle;
import jfws.util.math.geometry.distribution.PoissonDiscDistribution;
import jfws.util.math.geometry.mesh.renderer.FaceRenderer;
import jfws.util.math.geometry.mesh.renderer.MeshRenderer;
import jfws.util.math.geometry.voronoi.ImageBasedVoronoiDiagram;
import jfws.util.math.random.GeneratorWithRandom;
import jfws.util.rendering.CanvasRenderer;
import jfws.util.rendering.RandomColorSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class VoronoiController {

	public static final Point2d SIZE = new Point2d(800, 600);

	@FXML
	private Canvas mapCanvas;

	@FXML
	private Slider radiusSlider;

	private CanvasRenderer canvasRenderer;
	private MeshRenderer meshRenderer;

	private PoissonDiscDistribution poissonDiscDistribution;

	private double radius = 10.0;

	private ImageBasedVoronoiDiagram voronoiDiagram = new ImageBasedVoronoiDiagram(Rectangle.fromSize(SIZE), 2);

	public VoronoiController() {
		log.info("VoronoiController()");

		GeneratorWithRandom generator = new GeneratorWithRandom(42);

		poissonDiscDistribution = new PoissonDiscDistribution(generator);
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		canvasRenderer = new CanvasRenderer(mapCanvas.getGraphicsContext2D());
		FaceRenderer faceRenderer = new FaceRenderer(canvasRenderer);
		meshRenderer = new MeshRenderer(faceRenderer);

		radiusSlider.setValue(radius);
		radiusSlider.valueProperty().addListener(
				(observable, oldValue, newValue) -> onRadiusChanged((Double) newValue));

		render();
	}

	public void render() {
		canvasRenderer.clear(0, 0, 900, 700);

		List<Point2d> points = poissonDiscDistribution.distributePoints(SIZE, radius);

		log.info("render(): Voronoi");

		voronoiDiagram.update(points);

		meshRenderer.renderFaces(voronoiDiagram.getMesh(), new RandomColorSelector<>(new GeneratorWithRandom(99)));

		log.info("render(): points={}", points.size());

		canvasRenderer.setColor(Color.RED);

		for (Point2d point : points) {
			canvasRenderer.renderPoint(point, 4);
		}

		canvasRenderer.setColor(Color.BLUE);

		for (Point2d point : points) {
			canvasRenderer.renderPoint(point, 2);
		}

		log.info("render(): Finished");
	}

	@FXML
	public void onRadiusChanged(double newValue) {
		radius = newValue;
		log.info("onRadiusChanged(): {}", radius);
		render();
	}
}
