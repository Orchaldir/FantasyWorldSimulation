package jfws.app.demo.distribution;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import jfws.util.math.generator.gradient.CircularGradient;
import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.distribution.GridWithNoiseDistribution;
import jfws.util.math.geometry.distribution.PointDistribution;
import jfws.util.math.geometry.distribution.PoissonDiscDistribution;
import jfws.util.math.geometry.distribution.RandomPointDistribution;
import jfws.util.math.interpolation.LinearInterpolator;
import jfws.util.math.random.GeneratorWithRandom;
import jfws.util.rendering.CanvasRenderer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PointDistributionController {

	public static final Point2d SIZE = new Point2d(800, 600);
	public static final Point2d CENTER = SIZE.multiply(0.5);

	enum SelectedDistribution {
		GRID_WITH_NOISE,
		POISSON_DISC,
		RANDOM
	}

	@FXML
	private Canvas mapCanvas;

	@FXML
	private ComboBox<SelectedDistribution> distributionComboBox;

	@FXML
	private Slider numberOfPointsSlider;

	@FXML
	private Slider radiusSlider;

	private CanvasRenderer canvasRenderer;

	private final GeneratorWithRandom generator;
	private GridWithNoiseDistribution gridWithNoiseDistribution;
	private PoissonDiscDistribution poissonDiscDistribution;
	private RandomPointDistribution randomPointDistribution;

	private SelectedDistribution selectedDistribution = SelectedDistribution.POISSON_DISC;

	private int maxNumberOfPoints = 1000;
	private double radius = 10.0;
	private List<Point2d> points;

	public PointDistributionController() {
		log.info("PointDistributionController()");

		generator = new GeneratorWithRandom(42);

		gridWithNoiseDistribution = new GridWithNoiseDistribution(generator);
		createPoissonDiscDistribution();
		randomPointDistribution = new RandomPointDistribution(generator);
	}

	public void createPoissonDiscDistribution() {
		CircularGradient gradient = new CircularGradient(new LinearInterpolator(), CENTER, CENTER.getY(), radius*2, radius);
		poissonDiscDistribution = new PoissonDiscDistribution(generator, gradient);
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		canvasRenderer = new CanvasRenderer(mapCanvas.getGraphicsContext2D());

		distributionComboBox.setItems(FXCollections.observableArrayList(SelectedDistribution.values()));
		distributionComboBox.getSelectionModel().select(selectedDistribution);

		numberOfPointsSlider.setValue(maxNumberOfPoints);
		numberOfPointsSlider.valueProperty().addListener((
				observable, oldValue, newValue) -> onMaxNumberOfPointsChanged((Double) newValue));

		radiusSlider.setValue(radius);
		radiusSlider.valueProperty().addListener(
				(observable, oldValue, newValue) -> onRadiusChanged((Double) newValue));

		updatePoints();
		render();
	}

	public void render() {
		canvasRenderer.clear(0, 0, 900, 700);

		log.info("render(): selectedDistribution={} points={}", selectedDistribution, points.size());

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

	private void updatePoints() {
		points = getPointDistribution().distributePoints(SIZE, maxNumberOfPoints);
	}

	private PointDistribution getPointDistribution() {
		switch (selectedDistribution) {
			case GRID_WITH_NOISE:
				return gridWithNoiseDistribution;
			case POISSON_DISC:
				return poissonDiscDistribution;
			default:
				return randomPointDistribution;
		}
	}

	@FXML
	public void onDistributionSelected() {
		selectedDistribution = distributionComboBox.getSelectionModel().getSelectedItem();
		log.info("onDistributionSelected(): terrainType={}", selectedDistribution);
		updatePoints();
		render();
	}

	@FXML
	public void onMaxNumberOfPointsChanged(double newValue) {
		maxNumberOfPoints = (int) newValue;
		log.info("onMaxNumberOfPointsChanged(): {}", maxNumberOfPoints);
		updatePoints();
		render();
	}

	@FXML
	public void onRadiusChanged(double newValue) {
		radius = newValue;
		log.info("onRadiusChanged(): {}", radius);
		createPoissonDiscDistribution();
		updatePoints();
		render();
	}
}
