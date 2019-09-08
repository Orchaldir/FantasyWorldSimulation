package jfws.app.rpg.arena;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import jfws.feature.rpg.component.Pose;
import jfws.feature.rpg.component.Statistics;
import jfws.feature.rpg.rules.unit.Trait;
import jfws.feature.rpg.rules.unit.TraitManager;
import jfws.feature.rpg.system.RenderSystem;
import jfws.util.ecs.component.ComponentMap;
import jfws.util.ecs.component.ComponentStorage;
import jfws.util.math.geometry.Point2d;
import jfws.util.rendering.CanvasRenderer;
import lombok.extern.slf4j.Slf4j;

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

	// rules
	private TraitManager traitManager;

	// components
	private ComponentStorage<Pose> poseStorage;
	private ComponentStorage<Statistics> statisticsStorage;

	// systems
	private RenderSystem renderSystem;

	public ArenaController() {
		log.info("ArenaController()");
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		canvasRenderer = new CanvasRenderer(mapCanvas.getGraphicsContext2D());

		// rules
		traitManager = new TraitManager();
		traitManager.add(new Trait("Toughness"));

		// components
		poseStorage = new ComponentMap<>();
		statisticsStorage = new ComponentMap<>();

		// systems
		renderSystem = new RenderSystem(poseStorage, 50.0, 0.5);

		// entities
		poseStorage.add(0, new Pose(10.0, 2.0, 0.0));
		statisticsStorage.add(0, new Statistics());

		poseStorage.add(1, new Pose(4.0, 5.0, 0.0));
		statisticsStorage.add(1, new Statistics());

		render();
	}

	public void render() {
		log.info("render()");

		canvasRenderer.clear(0, 0, 900, 700);

		renderSystem.render(canvasRenderer);
	}
}
