package jfws.app.demo.tectonic;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import jfws.feature.world.WorldCell;
import jfws.util.map.ArrayCellMap2D;
import jfws.util.map.CellMap2d;
import jfws.util.map.ToCellMapper;
import jfws.util.map.rendering.MapRenderer;
import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.Rectangle;
import jfws.util.math.geometry.distribution.PoissonDiscDistribution;
import jfws.util.math.geometry.mesh.renderer.FaceRenderer;
import jfws.util.math.geometry.mesh.renderer.MeshRenderer;
import jfws.util.math.geometry.voronoi.ImageBasedVoronoiDiagram;
import jfws.util.math.random.GeneratorWithRandom;
import jfws.util.rendering.CanvasRenderer;
import jfws.util.rendering.ColorSelector;
import jfws.util.rendering.RandomColorSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PlateTectonicsController {

	public static final Point2d SIZE = new Point2d(800, 600);
	public static final Rectangle RECTANGLE = Rectangle.fromSize(SIZE);
	public static final Point2d CENTER = SIZE.multiply(0.5);

	enum SelectedView {
		PLATES,
		CELLS
	}

	@FXML
	private Canvas mapCanvas;

	@FXML
	private ComboBox<SelectedView> viewComboBox;

	@FXML
	private Slider widthSlider;

	@FXML
	private Slider heightSlider;

	private SelectedView selectedView = SelectedView.PLATES;

	private CanvasRenderer canvasRenderer;
	private MeshRenderer meshRenderer;
	private MapRenderer mapRenderer;

	private int width = 7;
	private int height = 5;
	private CellMap2d<Integer> plateTectonicsMap;
	private ToCellMapper<Integer> cellMapper;

	private ImageBasedVoronoiDiagram<Void, Void, WorldCell> plateVoronoiDiagram = new ImageBasedVoronoiDiagram<>(RECTANGLE, 2);

	public PlateTectonicsController() {
		log.info("PlateTectonicsController()");

		create();
	}

	private void create() {
		createPlateTectonicsMap();
		createWorldMap();
	}

	private void createPlateTectonicsMap() {
		log.info("createPlateTectonicsMap()");

		plateTectonicsMap = new ArrayCellMap2D<>(width, height, new Integer[width*height]);
		cellMapper = ToCellMapper.fromRectangle(plateTectonicsMap, RECTANGLE);
	}

	private void createWorldMap() {
		log.info("createWorldMap()");
		GeneratorWithRandom generator = new GeneratorWithRandom(42);
		PoissonDiscDistribution poissonDiscDistribution = new PoissonDiscDistribution(generator, 100);

		List<Point2d> points = poissonDiscDistribution.distributePoints(SIZE, 1000);
		plateVoronoiDiagram.update(points);

		log.info("createWorldMap(): finished");
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		viewComboBox.setItems(FXCollections.observableArrayList(SelectedView.values()));
		viewComboBox.getSelectionModel().select(selectedView);

		widthSlider.setValue(width);
		widthSlider.valueProperty().addListener((
				observable, oldValue, newValue) -> onWidthChanged((Double) newValue));

		heightSlider.setValue(height);
		heightSlider.valueProperty().addListener((
				observable, oldValue, newValue) -> onHeightChanged((Double) newValue));

		canvasRenderer = new CanvasRenderer(mapCanvas.getGraphicsContext2D());
		mapRenderer = new MapRenderer(canvasRenderer, 1.0, 0.0);
		FaceRenderer faceRenderer = new FaceRenderer(canvasRenderer);
		meshRenderer = new MeshRenderer(faceRenderer);

		render();
	}

	public void render() {
		log.info("render()");

		canvasRenderer.clear(0, 0, 900, 700);

		ColorSelector colorSelector = new RandomColorSelector(new GeneratorWithRandom(99));

		switch (selectedView) {
			case PLATES:
				mapRenderer.render(cellMapper, colorSelector);
				break;
			case CELLS:
				meshRenderer.renderFaces(plateVoronoiDiagram.getMesh(), colorSelector);
				break;
		}
	}

	@FXML
	public void onViewSelected() {
		selectedView = viewComboBox.getSelectionModel().getSelectedItem();
		log.info("onViewSelected(): {}", selectedView);
		render();
	}

	@FXML
	public void onWidthChanged(double newValue) {
		width = (int) newValue;
		log.info("onWidthChanged(): {}", width);
		create();
		render();
	}

	@FXML
	public void onHeightChanged(double newValue) {
		height = (int) newValue;
		log.info("onHeightChanged(): {}", height);
		create();
		render();
	}
}
