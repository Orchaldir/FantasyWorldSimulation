package jfws.app.demo.distribution;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import jfws.util.math.random.GeneratorWithRandom;
import jfws.util.rendering.CanvasRenderer;
import jfws.util.rendering.RandomColorSelector;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PointDistributionController {

	@FXML
	private Canvas mapCanvas;

	private CanvasRenderer canvasRenderer;

	private RandomColorSelector randomColorSelector;

	public PointDistributionController() {
		log.info("PointDistributionController()");

		randomColorSelector = new RandomColorSelector(new GeneratorWithRandom(42));
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		canvasRenderer = new CanvasRenderer(mapCanvas.getGraphicsContext2D());

		render();
	}

	public void render() {
		log.info("render()");

		log.info("render(): Finished");
	}

	@FXML
	public void onExportImage() {
		log.info("onExportImage()");
		render();
	}
}
