package jfws.app.rpg.arena;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import jfws.feature.rpg.component.Pose;
import jfws.feature.rpg.system.RenderSystem;
import jfws.feature.world.WorldCell;
import jfws.feature.world.attribute.elevation.ElevationColorSelector;
import jfws.feature.world.attribute.magic.ManaLevel;
import jfws.feature.world.attribute.rainfall.RainfallLevel;
import jfws.feature.world.attribute.temperature.TemperatureLevel;
import jfws.feature.world.generation.AddGeneratorStep;
import jfws.feature.world.generation.ModifyWithAttributeStep;
import jfws.feature.world.generation.WorldGenerationStep;
import jfws.util.ecs.component.ComponentMap;
import jfws.util.ecs.component.ComponentStorage;
import jfws.util.map.ArrayCellMap2D;
import jfws.util.map.CellMap2d;
import jfws.util.map.ToCellMapper;
import jfws.util.map.rendering.ImageRenderer;
import jfws.util.math.generator.Sum;
import jfws.util.math.generator.gradient.AbsoluteLinearGradient;
import jfws.util.math.generator.gradient.CircularGradient;
import jfws.util.math.generator.noise.SimplexNoise;
import jfws.util.math.generator.noise.Transformation;
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
public class ArenaController {

	public static final Point2d SIZE = new Point2d(800, 600);
	public static final Point2d CENTER = SIZE.multiply(0.5);
	public static final Point2d BOTTOM = new Point2d(CENTER.getX(), 0);
	public static final Point2d UP = new Point2d(0, 1);
	public static final double MAX_ELEVATION = 175.0;

	@FXML
	private Canvas mapCanvas;

	private CanvasRenderer canvasRenderer;

	// components
	private ComponentStorage<Pose> poseStorage;

	// systems
	private RenderSystem renderSystem;

	public ArenaController() {
		log.info("ArenaController()");
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		canvasRenderer = new CanvasRenderer(mapCanvas.getGraphicsContext2D());

		// components
		poseStorage = new ComponentMap<>();

		// systems
		renderSystem = new RenderSystem(poseStorage);

		// entities
		poseStorage.add(0, new Pose(100, 200, 0.0));

		render();
	}

	public void render() {
		log.info("render()");

		canvasRenderer.clear(0, 0, 900, 700);

		renderSystem.render(canvasRenderer);
	}
}
