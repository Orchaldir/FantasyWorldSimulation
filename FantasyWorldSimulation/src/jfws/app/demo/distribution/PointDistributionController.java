package jfws.app.demo.distribution;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.distribution.PointDistribution;
import jfws.util.math.geometry.distribution.RandomPointDistribution;
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

	private PointDistribution pointDistribution;

	public PointDistributionController() {
		log.info("PointDistributionController()");

		randomColorSelector = new RandomColorSelector(new GeneratorWithRandom(42));

		pointDistribution = new RandomPointDistribution(new GeneratorWithRandom(42));
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		canvasRenderer = new CanvasRenderer(mapCanvas.getGraphicsContext2D());

		render();
	}

	public void render() {
		List<Point2d> points = pointDistribution.distributePoints(new Point2d(800, 600), 100);

		log.info("render(): points={}", points.size());

		for (Point2d point : points) {
			canvasRenderer.setColor(randomColorSelector.select(null));
			canvasRenderer.renderPoint(point, 10);
		}

		log.info("render(): Finished");
	}

	@FXML
	public void onExportImage() {
		log.info("onExportImage()");
		render();
	}
}
