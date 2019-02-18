package jfws.app.demo.distribution;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.distribution.PoissonDiscDistribution;
import jfws.util.math.random.GeneratorWithRandom;
import jfws.util.rendering.CanvasRenderer;
import jfws.util.rendering.RandomColorSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PointDistributionController {

	@FXML
	private Canvas mapCanvas;

	private CanvasRenderer canvasRenderer;

	private RandomColorSelector randomColorSelector;

	private PoissonDiscDistribution pointDistribution;

	public PointDistributionController() {
		log.info("PointDistributionController()");

		randomColorSelector = new RandomColorSelector(new GeneratorWithRandom(42));

		pointDistribution = new PoissonDiscDistribution(new GeneratorWithRandom(42), 20.0);
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		canvasRenderer = new CanvasRenderer(mapCanvas.getGraphicsContext2D());

		render();
	}

	public void render() {
		List<Point2d> points = pointDistribution.distributePoints(new Point2d(800, 600), 800);

		log.info("render(): points={}", points.size());

		canvasRenderer.setColor(Color.RED);

		for (Point2d point : points) {
			canvasRenderer.renderPoint(point, pointDistribution.getRadius());
		}

		canvasRenderer.setColor(Color.BLUE);

		for (Point2d point : points) {
			canvasRenderer.renderPoint(point, 5);
		}

		log.info("render(): Finished");
	}

	@FXML
	public void onExportImage() {
		log.info("onExportImage()");
		render();
	}
}
