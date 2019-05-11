package jfws.app.demo.tectonic;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import jfws.feature.world.WorldCell;
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
	private Slider radiusSlider;

	private SelectedView selectedView = SelectedView.PLATES;

	private CanvasRenderer canvasRenderer;
	private MeshRenderer meshRenderer;

	private double plateRadius = 100.0;
	private double worldCellRadius = 10.0;

	private ImageBasedVoronoiDiagram<Void, Void, WorldCell> plateVoronoiDiagram = new ImageBasedVoronoiDiagram<>(RECTANGLE, 2);
	private ImageBasedVoronoiDiagram<Void, Void, WorldCell> worldVoronoiDiagram = new ImageBasedVoronoiDiagram<>(RECTANGLE, 2);

	public PlateTectonicsController() {
		log.info("PlateTectonicsController()");

		createPlateTectonicsMap();
		createWorldMap();
	}

	private void createPlateTectonicsMap() {
		log.info("createPlateTectonicsMap()");
		GeneratorWithRandom generator = new GeneratorWithRandom(42);
		PoissonDiscDistribution poissonDiscDistribution = new PoissonDiscDistribution(generator, plateRadius);

		List<Point2d> points = poissonDiscDistribution.distributePoints(SIZE, 1000);
		plateVoronoiDiagram.update(points);

		log.info("createPlateTectonicsMap(): finished");
	}

	private void createWorldMap() {
		log.info("createWorldMap()");
		GeneratorWithRandom generator = new GeneratorWithRandom(42);
		PoissonDiscDistribution poissonDiscDistribution = new PoissonDiscDistribution(generator, worldCellRadius);

		List<Point2d> points = poissonDiscDistribution.distributePoints(SIZE, 1000);
		worldVoronoiDiagram.update(points);

		log.info("createWorldMap(): finished");
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		viewComboBox.setItems(FXCollections.observableArrayList(SelectedView.values()));
		viewComboBox.getSelectionModel().select(selectedView);

		radiusSlider.setValue(plateRadius);
		radiusSlider.valueProperty().addListener((
				observable, oldValue, newValue) -> onPlateRadiusChanged((Double) newValue));

		canvasRenderer = new CanvasRenderer(mapCanvas.getGraphicsContext2D());
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
				meshRenderer.renderFaces(plateVoronoiDiagram.getMesh(), colorSelector);
				break;
			case CELLS:
				meshRenderer.renderFaces(worldVoronoiDiagram.getMesh(), colorSelector);
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
	public void onPlateRadiusChanged(double newValue) {
		plateRadius = (int) newValue;
		log.info("onPlateRadiusChanged(): {}", plateRadius);
		createPlateTectonicsMap();
		render();
	}
}
