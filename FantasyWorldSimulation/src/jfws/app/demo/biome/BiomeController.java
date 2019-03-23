package jfws.app.demo.biome;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import jfws.feature.world.WorldCell;
import jfws.feature.world.generation.AddGeneratorStep;
import jfws.feature.world.generation.ModifyWithAttributeStep;
import jfws.features.elevation.ElevationColorSelector;
import jfws.features.rainfall.RainfallColorSelector;
import jfws.features.temperature.TemperatureColorSelector;
import jfws.util.math.generator.Sum;
import jfws.util.math.generator.Transformation;
import jfws.util.math.generator.gradient.AbsoluteLinearGradient;
import jfws.util.math.generator.gradient.CircularGradient;
import jfws.util.math.generator.noise.SimplexNoise;
import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.Rectangle;
import jfws.util.math.geometry.distribution.PoissonDiscDistribution;
import jfws.util.math.geometry.mesh.Face;
import jfws.util.math.geometry.mesh.Mesh;
import jfws.util.math.geometry.mesh.renderer.FaceRenderer;
import jfws.util.math.geometry.mesh.renderer.MeshRenderer;
import jfws.util.math.geometry.voronoi.ImageBasedVoronoiDiagram;
import jfws.util.math.interpolation.LinearInterpolator;
import jfws.util.math.random.GeneratorWithRandom;
import jfws.util.rendering.CanvasRenderer;
import jfws.util.rendering.ColorSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static jfws.feature.world.WorldCell.*;

@Slf4j
public class BiomeController {

	public static final Point2d SIZE = new Point2d(800, 600);
	public static final Point2d CENTER = SIZE.multiply(0.5);
	public static final Point2d BOTTOM = new Point2d(CENTER.getX(), 0);
	public static final Point2d UP = new Point2d(0, 1);
	public static final double MAX_ELEVATION = 175.0;

	enum SelectedFeature {
		ELEVATION,
		TEMPERATURE,
		RAINFALL
	}

	@FXML
	private Canvas mapCanvas;

	@FXML
	private ComboBox<SelectedFeature> featureComboBox;

	private SelectedFeature selectedFeature = SelectedFeature.ELEVATION;

	private CanvasRenderer canvasRenderer;
	private MeshRenderer meshRenderer;

	private ColorSelector elevationColorSelector = new ElevationColorSelector();
	private ColorSelector temperatureColorSelector = new TemperatureColorSelector();
	private ColorSelector rainfallColorSelector = new RainfallColorSelector();

	private final double poissonDiskRadius = 5.0;

	private ImageBasedVoronoiDiagram<Void, Void, WorldCell> voronoiDiagram = new ImageBasedVoronoiDiagram<>(Rectangle.fromSize(SIZE), 2);

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

		Mesh<Void, Void, WorldCell> mesh = voronoiDiagram.getMesh();

		for (Face<Void, Void, WorldCell> face : mesh.getFaces()) {
			face.setData(new WorldCell());
		}

		generateElevation(mesh);
		generateTemperature(mesh);
		generateRainfall(mesh);

		log.info("createWorldMap(): finished");
	}

	private void generateElevation(Mesh<Void, Void, WorldCell> mesh) {
		log.info("generateElevation()");

		SimplexNoise simplexNoise = new SimplexNoise();
		Transformation elevationNoise = new Transformation(simplexNoise, 55.0, 120, 200);
		CircularGradient circularGradient = new CircularGradient(new LinearInterpolator(), BOTTOM, 350, 100.0, -65.0);
		Sum elevationGenerator = new Sum(List.of(elevationNoise, circularGradient));
		AddGeneratorStep elevationStep = new AddGeneratorStep(elevationGenerator, ELEVATION);

		elevationStep.generate(mesh);
	}

	private void generateTemperature(Mesh<Void, Void, WorldCell> mesh) {
		log.info("generateTemperature()");

		AbsoluteLinearGradient temperatureGenerator = new AbsoluteLinearGradient(new LinearInterpolator(), CENTER, UP,
				CENTER.getY(), 0.9, 0.1);
		AddGeneratorStep temperatureStep = new AddGeneratorStep(temperatureGenerator, TEMPERATURE);
		ModifyWithAttributeStep subtractElevationStep = new ModifyWithAttributeStep(ELEVATION, TEMPERATURE, 0.0, Double.MAX_VALUE, -0.3 / MAX_ELEVATION);

		temperatureStep.generate(mesh);
		subtractElevationStep.generate(mesh);
	}

	private void generateRainfall(Mesh<Void, Void, WorldCell> mesh) {
		log.info("generateRainfall()");

		SimplexNoise simplexNoise = new SimplexNoise();
		Transformation elevationNoise = new Transformation(simplexNoise, 0.5, 1.0, 200);
		AddGeneratorStep elevationStep = new AddGeneratorStep(elevationNoise, RAINFALL);

		elevationStep.generate(mesh);
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
		switch (selectedFeature) {
			case ELEVATION:
				return elevationColorSelector;
			case TEMPERATURE:
				return temperatureColorSelector;
			default:
				return rainfallColorSelector;
		}
	}

	@FXML
	public void onFeatureSelected() {
		selectedFeature = featureComboBox.getSelectionModel().getSelectedItem();
		log.info("onFeatureSelected(): {}", selectedFeature);
		render();
	}
}
