package jfws.app.demo.biome;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import jfws.feature.world.WorldCell;
import jfws.feature.world.attribute.magic.ManaLevel;
import jfws.feature.world.attribute.rainfall.RainfallLevel;
import jfws.feature.world.attribute.temperature.TemperatureLevel;
import jfws.feature.world.generation.AddGeneratorStep;
import jfws.feature.world.generation.ModifyWithAttributeStep;
import jfws.feature.world.attribute.elevation.ElevationColorSelector;
import jfws.feature.world.generation.WorldGenerationStep;
import jfws.util.map.ArrayCellMap2D;
import jfws.util.map.CellMap2d;
import jfws.util.map.ToCellMapper;
import jfws.util.map.rendering.ImageRenderer;
import jfws.util.math.generator.Sum;
import jfws.util.math.generator.noise.Transformation;
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

	enum SelectedMapType {
		GRID,
		POLYGON
	}

	enum SelectedFeature {
		ELEVATION,
		TEMPERATURE,
		RAINFALL,
		MANA_LEVEL
	}

	@FXML
	private Canvas mapCanvas;

	@FXML
	private ComboBox<SelectedFeature> featureComboBox;

	@FXML
	private ComboBox<SelectedMapType> mapComboBox;

	private SelectedMapType selectedMapType = SelectedMapType.GRID;
	private SelectedFeature selectedFeature = SelectedFeature.ELEVATION;

	private CanvasRenderer canvasRenderer;
	private MeshRenderer meshRenderer;

	private ColorSelector elevationColorSelector = new ElevationColorSelector();
	private ColorSelector temperatureColorSelector = TemperatureLevel.createColorSelector(TEMPERATURE);
	private ColorSelector rainfallColorSelector = RainfallLevel.createColorSelector(RAINFALL);
	private ColorSelector manaLevelColorSelector = ManaLevel.createClosestLevelColorSelector(MANA_LEVEL);

	private static final double POISSON_DISK_RADIUS = 5.0;

	private ImageBasedVoronoiDiagram<Void, Void, WorldCell> voronoiDiagram = new ImageBasedVoronoiDiagram<>(Rectangle.fromSize(SIZE), 2);

	private int pixelsPerCell = 1;
	private int cellMapWidth = 800;
	private int cellMapHeight = 600;
	private CellMap2d<WorldCell> cellMap;
	private ToCellMapper<WorldCell> mapper;
	private ImageRenderer imageRenderer = new ImageRenderer();

	// biome data
	private Point2d elevationCenter = BOTTOM;
	private Point2d temperatureCenter = CENTER;
	private Point2d temperatureDirection = UP;

	public BiomeController() {
		log.info("BiomeController()");

		createWorldMap();
	}

	private void createWorldMap() {
		log.info("createWorldMap()");
		GeneratorWithRandom generator = new GeneratorWithRandom(42);
		PoissonDiscDistribution poissonDiscDistribution = new PoissonDiscDistribution(generator, POISSON_DISK_RADIUS);

		List<Point2d> points = poissonDiscDistribution.distributePoints(SIZE, 1000);
		voronoiDiagram.update(points);

		generateBiomeData();

		log.info("createWorldMap(): finished");
	}

	private void generateBiomeData() {
		log.info("generateBiomeData(): Init cells");

		Mesh<Void, Void, WorldCell> mesh = initVoronoiDiagram();

		createCellMap();

		generateElevation(mesh);
		generateTemperature(mesh);
		generateRainfall(mesh);
		generateManaLevel(mesh);
	}

	private Mesh<Void, Void, WorldCell> initVoronoiDiagram() {
		Mesh<Void, Void, WorldCell> mesh = voronoiDiagram.getMesh();

		for (Face<Void, Void, WorldCell> face : mesh.getFaces()) {
			face.setData(new WorldCell());
		}
		return mesh;
	}

	private void createCellMap() {
		cellMapWidth = (int) (SIZE.getX() / (double)pixelsPerCell);
		cellMapHeight = (int) (SIZE.getY() / (double)pixelsPerCell);
		int numberOfCells = cellMapWidth * cellMapHeight;
		WorldCell[] cells = new WorldCell[numberOfCells];

		for(int i = 0; i < numberOfCells; i++) {
			cells[i] = new WorldCell();
		}

		cellMap = new ArrayCellMap2D<>(cellMapWidth, cellMapHeight, cells);
		mapper = new ToCellMapper<>(cellMap, pixelsPerCell);
	}

	private void generateElevation(Mesh<Void, Void, WorldCell> mesh) {
		log.info("generateElevation()");

		SimplexNoise simplexNoise = new SimplexNoise();
		Transformation elevationNoise = new Transformation(simplexNoise, -10.0, 120, 200);
		CircularGradient circularGradient = new CircularGradient(new LinearInterpolator(), elevationCenter, 350, 100.0, -65.0);
		Sum elevationGenerator = new Sum(List.of(elevationNoise, circularGradient));
		AddGeneratorStep elevationStep = new AddGeneratorStep(elevationGenerator, ELEVATION);

		generate(mesh, elevationStep);
	}

	private void generateTemperature(Mesh<Void, Void, WorldCell> mesh) {
		log.info("generateTemperature()");

		AbsoluteLinearGradient temperatureGenerator = new AbsoluteLinearGradient(new LinearInterpolator(),
				temperatureCenter, temperatureDirection,
				CENTER.getY(), 0.9, 0.1);
		AddGeneratorStep temperatureStep = new AddGeneratorStep(temperatureGenerator, TEMPERATURE);
		ModifyWithAttributeStep subtractElevationStep = new ModifyWithAttributeStep(ELEVATION, TEMPERATURE, 0.0, Double.MAX_VALUE, -0.3 / MAX_ELEVATION);

		generate(mesh, temperatureStep);
		generate(mesh, subtractElevationStep);
	}

	private void generateRainfall(Mesh<Void, Void, WorldCell> mesh) {
		log.info("generateRainfall()");

		SimplexNoise simplexNoise = new SimplexNoise();
		Transformation elevationNoise = new Transformation(simplexNoise, 0.0, 1.0, 200, 300, 300);
		AddGeneratorStep rainfallStep = new AddGeneratorStep(elevationNoise, RAINFALL);

		generate(mesh, rainfallStep);
	}

	private void generateManaLevel(Mesh<Void, Void, WorldCell> mesh) {
		log.info("generateManaLevel()");

		SimplexNoise simplexNoise = new SimplexNoise();
		Transformation elevationNoise = new Transformation(simplexNoise, 0.0, 1.0, 1200, 1300, 300);
		AddGeneratorStep manaStep = new AddGeneratorStep(elevationNoise, MANA_LEVEL);

		generate(mesh, manaStep);
	}

	private void generate(Mesh<Void, Void, WorldCell> mesh, WorldGenerationStep step) {
		step.generate(mesh);
		step.generate(mapper);
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		featureComboBox.setItems(FXCollections.observableArrayList(SelectedFeature.values()));
		featureComboBox.getSelectionModel().select(selectedFeature);

		mapComboBox.setItems(FXCollections.observableArrayList(SelectedMapType.values()));
		mapComboBox.getSelectionModel().select(selectedMapType);

		canvasRenderer = new CanvasRenderer(mapCanvas.getGraphicsContext2D());
		FaceRenderer faceRenderer = new FaceRenderer(canvasRenderer);
		meshRenderer = new MeshRenderer(faceRenderer);

		render();
	}

	public void render() {
		log.info("render()");

		canvasRenderer.clear(0, 0, 900, 700);

		ColorSelector colorSelector = getColorSelector();

		if(selectedMapType == SelectedMapType.GRID) {
			WritableImage image = imageRenderer.render(cellMap, colorSelector);
			mapCanvas.getGraphicsContext2D().drawImage(image, 0, 0, SIZE.getX(), SIZE.getY());
		}
		else {
			meshRenderer.renderFaces(voronoiDiagram.getMesh(), colorSelector);
		}
	}

	private ColorSelector getColorSelector() {
		switch (selectedFeature) {
			case ELEVATION:
				return elevationColorSelector;
			case TEMPERATURE:
				return temperatureColorSelector;
			case RAINFALL:
				return rainfallColorSelector;
			default:
				return manaLevelColorSelector;
		}
	}

	@FXML
	public void onFeatureSelected() {
		selectedFeature = featureComboBox.getSelectionModel().getSelectedItem();
		log.info("onFeatureSelected(): {}", selectedFeature);
		render();
	}

	@FXML
	public void onMapSelected() {
		selectedMapType = mapComboBox.getSelectionModel().getSelectedItem();
		log.info("onMapSelected(): {}", selectedMapType);
		render();
	}

	@FXML
	public void onMouseClicked(MouseEvent mouseEvent) {
		log.info("onMouseClicked(): x={} y={} button={}", mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getButton());

		switch (selectedFeature) {
			case ELEVATION:
				elevationCenter = new Point2d(mouseEvent.getX(), mouseEvent.getY());
				break;
			case TEMPERATURE:
				if(mouseEvent.getButton() == MouseButton.PRIMARY) {
					temperatureCenter = new Point2d(mouseEvent.getX(), mouseEvent.getY());
				}
				else {
					Point2d clickedPoint = new Point2d(mouseEvent.getX(), mouseEvent.getY());
					temperatureDirection = clickedPoint.subtract(temperatureCenter).getNormalized();
				}
				break;
			default:
				return;
		}

		generateBiomeData();
		render();
	}
}
