package jfws.app.demo.distribution;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.distribution.PointDistribution;
import jfws.util.math.geometry.distribution.PoissonDiscDistribution;
import jfws.util.math.geometry.distribution.RandomPointDistribution;
import jfws.util.math.random.GeneratorWithRandom;
import jfws.util.rendering.CanvasRenderer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PointDistributionController {

	enum SelectedDistribution {
		POISSON,
		RANDOM
	}

	@FXML
	private Canvas mapCanvas;

	@FXML
	private Slider numberOfPointsSlider;

	private CanvasRenderer canvasRenderer;

	private RandomPointDistribution randomPointDistribution;
	private PoissonDiscDistribution poissonDiscDistribution;

	private SelectedDistribution selectedDistribution = SelectedDistribution.RANDOM;

	private int maxNumberOfPoints = 800;
	private double radius = 10.0;

	public PointDistributionController() {
		log.info("PointDistributionController()");

		GeneratorWithRandom generator = new GeneratorWithRandom(42);

		randomPointDistribution = new RandomPointDistribution(generator);
		poissonDiscDistribution = new PoissonDiscDistribution(generator);
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		canvasRenderer = new CanvasRenderer(mapCanvas.getGraphicsContext2D());

		numberOfPointsSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			onMaxNumberOfPointsChanged((Double) newValue);
		});

		render();
	}

	public void render() {
		List<Point2d> points = getPoints();

		log.info("render(): selectedDistribution={} points={}", selectedDistribution, points.size());

		canvasRenderer.clear(0, 0, 900, 700);

		canvasRenderer.setColor(Color.RED);

		for (Point2d point : points) {
			canvasRenderer.renderPoint(point, radius);
		}

		canvasRenderer.setColor(Color.BLUE);

		for (Point2d point : points) {
			canvasRenderer.renderPoint(point, 2);
		}

		log.info("render(): Finished");
	}

	private List<Point2d> getPoints() {
		List<Point2d> points = getPointDistribution().distributePoints(new Point2d(800, 600), radius);

		if(points.size() > maxNumberOfPoints)
		{
			return points.subList(0, maxNumberOfPoints);
		}

		return points;
	}

	private PointDistribution getPointDistribution() {
		return selectedDistribution == SelectedDistribution.POISSON ? poissonDiscDistribution : randomPointDistribution;
	}

	@FXML
	public void onMaxNumberOfPointsChanged(double newValue) {
		maxNumberOfPoints = (int) newValue;
		log.info("onMaxNumberOfPointsChanged(): {}", maxNumberOfPoints);
		render();
	}
}
