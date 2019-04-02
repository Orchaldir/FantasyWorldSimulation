package jfws.app.demo.tectonic;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
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
	public static final Point2d CENTER = SIZE.multiply(0.5);

	enum SelectedView {
		PLATES,
		CELLS
	}

	@FXML
	private Canvas mapCanvas;

	@FXML
	private ComboBox<SelectedView> viewComboBox;

	private SelectedView selectedView = SelectedView.PLATES;

	private CanvasRenderer canvasRenderer;
	private MeshRenderer meshRenderer;

	private final double poissonDiskRadius = 50.0;

	private ImageBasedVoronoiDiagram<Void, Void, WorldCell> plateVoronoiDiagram = new ImageBasedVoronoiDiagram<>(Rectangle.fromSize(SIZE), 2);

	public PlateTectonicsController() {
		log.info("PlateTectonicsController()");

		createWorldMap();
	}

	private void createWorldMap() {
		log.info("createWorldMap()");
		GeneratorWithRandom generator = new GeneratorWithRandom(42);
		PoissonDiscDistribution poissonDiscDistribution = new PoissonDiscDistribution(generator);

		List<Point2d> points = poissonDiscDistribution.distributePoints(SIZE, poissonDiskRadius);
		plateVoronoiDiagram.update(points);

		log.info("createWorldMap(): finished");
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		viewComboBox.setItems(FXCollections.observableArrayList(SelectedView.values()));
		viewComboBox.getSelectionModel().select(selectedView);

		canvasRenderer = new CanvasRenderer(mapCanvas.getGraphicsContext2D());
		FaceRenderer faceRenderer = new FaceRenderer(canvasRenderer);
		meshRenderer = new MeshRenderer(faceRenderer);

		render();
	}

	public void render() {
		log.info("render()");

		canvasRenderer.clear(0, 0, 900, 700);

		ColorSelector colorSelector = new RandomColorSelector(new GeneratorWithRandom(99));
		meshRenderer.renderFaces(plateVoronoiDiagram.getMesh(), colorSelector);
	}

	@FXML
	public void onViewSelected() {
		selectedView = viewComboBox.getSelectionModel().getSelectedItem();
		log.info("onViewSelected(): {}", selectedView);
		render();
	}
}
