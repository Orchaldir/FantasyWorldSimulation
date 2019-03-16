package jfws.app.demo.biome;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import jfws.features.elevation.ElevationColorSelector;
import jfws.features.temperature.TemperatureColorSelector;
import jfws.util.math.generator.Transformation;
import jfws.util.math.generator.gradient.AbsoluteLinearGradient;
import jfws.util.math.generator.noise.SimplexNoise;
import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.Rectangle;
import jfws.util.math.geometry.distribution.PoissonDiscDistribution;
import jfws.util.math.geometry.mesh.Face;
import jfws.util.math.geometry.mesh.renderer.FaceRenderer;
import jfws.util.math.geometry.mesh.renderer.MeshRenderer;
import jfws.util.math.geometry.voronoi.ImageBasedVoronoiDiagram;
import jfws.util.math.interpolation.LinearInterpolator;
import jfws.util.math.random.GeneratorWithRandom;
import jfws.util.rendering.CanvasRenderer;
import jfws.util.rendering.ColorSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class BiomeController {

	public static final Point2d SIZE = new Point2d(800, 600);
	public static final Point2d CENTER = SIZE.multiply(0.5);
	public static final Point2d UP = new Point2d(0, 1);

	enum SelectedFeature {
		ELEVATION,
		TEMPERATURE
	}

	@FXML
	private Canvas mapCanvas;

	@FXML
	private ComboBox<SelectedFeature> featureComboBox;

	private SelectedFeature selectedFeature = SelectedFeature.TEMPERATURE;

	private CanvasRenderer canvasRenderer;
	private MeshRenderer meshRenderer;

	private ColorSelector elevationColorSelector = new ElevationColorSelector();
	private ColorSelector temperatureColorSelector = new TemperatureColorSelector();

	private final double poissonDiskRadius = 5.0;

	private ImageBasedVoronoiDiagram<Void, Void, Cell> voronoiDiagram = new ImageBasedVoronoiDiagram<>(Rectangle.fromSize(SIZE), 2);

	public BiomeController() {
		log.info("BiomeController()");

		createWorldMap();
	}

	private void createWorldMap() {
		log.info("createWorldMap()");
		GeneratorWithRandom generator = new GeneratorWithRandom(42);
		PoissonDiscDistribution poissonDiscDistribution = new PoissonDiscDistribution(generator);

		List<Point2d> points = poissonDiscDistribution.distributePoints(SIZE, poissonDiskRadius);
		voronoiDiagram.update(points);

		log.info("createWorldMap(): Init cells");

		SimplexNoise simplexNoise = new SimplexNoise();
		double maxElevation = 175.0;
		Transformation elevationGenerator = new Transformation(simplexNoise, -50.0, maxElevation, 200);

		AbsoluteLinearGradient temperatureGenerator = new AbsoluteLinearGradient(new LinearInterpolator(), CENTER, UP,
				CENTER.getY(), 0.9, 0.1);

		for (Face<Void, Void, Cell> face : voronoiDiagram.getMesh().getFaces()) {
			Point2d point = points.get(face.getId());
			double elevation = elevationGenerator.generate(point);
			double temperature = temperatureGenerator.generate(point) - 0.2 * Math.max(elevation / maxElevation, 0.0);
			face.setData(new Cell(elevation, temperature));
		}
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		featureComboBox.setItems(FXCollections.observableArrayList(SelectedFeature.values()));
		featureComboBox.getSelectionModel().select(selectedFeature);

		canvasRenderer = new CanvasRenderer(mapCanvas.getGraphicsContext2D());
		FaceRenderer faceRenderer = new FaceRenderer(canvasRenderer);
		meshRenderer = new MeshRenderer(faceRenderer);

		render();
	}

	public void render() {
		log.info("render()");

		canvasRenderer.clear(0, 0, 900, 700);

		ColorSelector colorSelector = getColorSelector();
		meshRenderer.renderFaces(voronoiDiagram.getMesh(), colorSelector);
	}

	private ColorSelector getColorSelector() {
		if(selectedFeature == SelectedFeature.ELEVATION) {
			return elevationColorSelector;
		}
		else {
			return temperatureColorSelector;
		}
	}

	@FXML
	public void onFeatureSelected() {
		selectedFeature = featureComboBox.getSelectionModel().getSelectedItem();
		log.info("onFeatureSelected(): {}", selectedFeature);
		render();
	}
}
