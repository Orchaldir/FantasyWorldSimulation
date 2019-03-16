package jfws.app.demo.biome;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import jfws.features.elevation.ElevationColorSelector;
import jfws.util.math.generator.Transformation;
import jfws.util.math.generator.noise.SimplexNoise;
import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.Rectangle;
import jfws.util.math.geometry.distribution.PoissonDiscDistribution;
import jfws.util.math.geometry.mesh.Face;
import jfws.util.math.geometry.mesh.renderer.FaceRenderer;
import jfws.util.math.geometry.mesh.renderer.MeshRenderer;
import jfws.util.math.geometry.voronoi.ImageBasedVoronoiDiagram;
import jfws.util.math.random.GeneratorWithRandom;
import jfws.util.rendering.CanvasRenderer;
import jfws.util.rendering.ColorSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class BiomeController {

	public static final Point2d SIZE = new Point2d(800, 600);
	public static final Point2d CENTER = SIZE.multiply(0.5);

	@FXML
	private Canvas mapCanvas;

	private CanvasRenderer canvasRenderer;
	private MeshRenderer meshRenderer;
	private ColorSelector<Face> colorSelector = new ElevationColorSelector();

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
		Transformation gradient = new Transformation(simplexNoise, -50.0, 175.0, 200);

		for (Face<Void, Void, Cell> face : voronoiDiagram.getMesh().getFaces()) {
			double elevation = gradient.generate(points.get(face.getId()));
			face.setData(new Cell(elevation));
		}
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		canvasRenderer = new CanvasRenderer(mapCanvas.getGraphicsContext2D());
		FaceRenderer faceRenderer = new FaceRenderer(canvasRenderer);
		meshRenderer = new MeshRenderer(faceRenderer);

		render();
	}

	public void render() {
		log.info("render()");

		canvasRenderer.clear(0, 0, 900, 700);

		meshRenderer.renderFaces(voronoiDiagram.getMesh(), colorSelector);
	}
}
