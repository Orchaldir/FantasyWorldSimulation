package jfws.app.editor.map;

import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.stage.Window;
import jfws.features.elevation.*;
import jfws.features.elevation.noise.*;
import jfws.maps.region.RegionCell;
import jfws.maps.region.RegionMap;
import jfws.maps.sketch.SketchCell;
import jfws.maps.sketch.SketchMap;
import jfws.maps.sketch.elevation.ElevationGenerator;
import jfws.maps.sketch.elevation.ElevationGeneratorWithNoise;
import jfws.maps.sketch.rendering.TerrainColorSelector;
import jfws.util.map.rendering.ImageRenderer;
import jfws.util.map.MapInterpolator;
import jfws.util.map.rendering.MapRenderer;
import jfws.util.math.interpolation.BiTwoValueInterpolator;
import jfws.util.math.generator.ScaledInput;
import jfws.util.math.generator.noise.SimplexNoise;
import jfws.util.math.random.GeneratorWithRandom;
import jfws.util.rendering.CanvasRenderer;
import jfws.util.rendering.ColorSelector;
import jfws.util.rendering.ColorSelectorMap;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class MapEditorController implements EditorController {

	public static final double WORLD_TO_SCREEN = 0.5;
	public static final int BORDER_BETWEEN_CELLS = 0;

	enum MapType {
		SKETCH_MAP,
		REGION_MAP
	}

	@FXML
	private ComboBox<String> terrainTypeComboBox;

	@FXML
	private ComboBox<String> renderStyleComboBox;

	@FXML
	private Canvas mapCanvas;

	@FXML
	private MenuItem undoItem;

	@FXML
	private MenuItem redoItem;

	@FXML
	private MenuItem viewRegionMapItem;

	@FXML
	private MenuItem viewSketchMapItem;

	private MapStorage mapStorage;

	private MenuBarController menuBarController;

	private ElevationGenerator elevationGenerator = new ElevationGeneratorWithNoise(new GeneratorWithRandom(42));

	private ColorSelectorMap<SketchCell> colorSelectorMap;
	private ColorSelector<SketchCell> colorSelectorForSketch;

	private ColorSelector<RegionCell> colorSelectorForRegion;
	private MapInterpolator elevationInterpolator = ElevationCellInterpolator.createMapInterpolator(
			BiTwoValueInterpolator.createBiCosineInterpolator());

	private ElevationNoiseManager<RegionCell> elevationNoiseManager = new ElevationNoiseManager<>();
	private ElevationNoiseWithInterpolation hillNoise = new ElevationNoiseWithInterpolation("hill",
			BiTwoValueInterpolator.createBilinearInterpolator(), new ScaledInput(new SimplexNoise(), 50.0), 0);

	private MapType mapToRender =  MapType.SKETCH_MAP;
	private MapRenderer mapRenderer;
	private ImageRenderer imageRenderer = new ImageRenderer();

	public MapEditorController() {
		log.info("MapEditorController()");

		mapStorage = new MapStorage(50);
		mapStorage.getTerrainTypeManager().load(new File("data/terrain-types.json"));
		mapStorage.createEmptyMap(20, 10, "Plain");
		mapStorage.createTool("Hill");

		colorSelectorMap = new ColorSelectorMap<>(new TerrainColorSelector());
		colorSelectorMap.add(new ElevationColorSelector());
		colorSelectorForSketch = colorSelectorMap.getDefaultColorSelector();

		colorSelectorForRegion = new ElevationColorSelector<>();
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		menuBarController = new MenuBarController(mapStorage, this, undoItem, redoItem);

		initTerrainTypeComboBox();

		renderStyleComboBox.setItems(FXCollections.observableArrayList(colorSelectorMap.getNames()));
		renderStyleComboBox.getSelectionModel().select(colorSelectorMap.getDefaultColorSelector().getName());

		elevationNoiseManager.add(hillNoise);

		CanvasRenderer canvasRenderer = new CanvasRenderer(mapCanvas.getGraphicsContext2D());
		mapRenderer = new MapRenderer(canvasRenderer, WORLD_TO_SCREEN, BORDER_BETWEEN_CELLS);

		menuBarController.updateHistory();
		updateViewControls();
		render();
	}

	private void initTerrainTypeComboBox() {
		List<String> names = mapStorage.getTerrainTypeManager().getNamesSortedByGroup();

		terrainTypeComboBox.setItems(FXCollections.observableArrayList(names));
		terrainTypeComboBox.getSelectionModel().select(mapStorage.getChangeTerrainTypeTool().getTerrainType().getName());
	}

	@Override
	public BufferedImage getSnapshot() {
		WritableImage image = mapCanvas.snapshot(null, null);
		return SwingFXUtils.fromFXImage(image, null);
	}

	@Override
	public void saveSnapshot(File file, BufferedImage bufferedImage) throws IOException {
		ImageIO.write(bufferedImage, "PNG", file);
	}

	@Override
	public Window getWindow() {
		return null;
	}

	@Override
	public void render() {
		log.info("render()");
		SketchMap sketchMap = mapStorage.getSketchMap();
		RegionMap regionMap = mapStorage.getRegionMap();

		sketchMap.generateElevation(elevationGenerator);

		if (mapToRender == MapType.REGION_MAP) {
			elevationInterpolator.interpolate(sketchMap.getCellMap(), regionMap.getCellMap());

			for (ElevationNoise elevationNoise : elevationNoiseManager.getAll()) {
				elevationNoise.addTo(regionMap);
			}

			WritableImage image = imageRenderer.render(regionMap, colorSelectorForRegion);
			mapCanvas.getGraphicsContext2D().drawImage(image, 0, 0);
		} else {
			mapRenderer.render(sketchMap.getToCellMapper(), colorSelectorForSketch);
		}

		log.info("render(): Finished");
	}

	@Override
	public void showAlert(Alert.AlertType type, String title, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.showAndWait();
	}

	@FXML
	public void onLoadMap() {
		menuBarController.loadMap();
	}

	@FXML
	public void onSaveMap() {
		menuBarController.saveMap();
	}

	@FXML
	public void onExportImage() {
		menuBarController.exportImage();
	}

	@FXML
	public void onTerrainTypeSelected() {
		String selectedName = terrainTypeComboBox.getSelectionModel().getSelectedItem();
		log.info("onTerrainTypeSelected(): terrainType={}", selectedName);
		mapStorage.changeTypeOfTool(selectedName);
	}

	@FXML
	public void onRenderStyleSelected() {
		String selectedName = renderStyleComboBox.getSelectionModel().getSelectedItem();
		ColorSelector<SketchCell> selectedColorSelector = colorSelectorMap.get(selectedName);

		if(this.colorSelectorForSketch != selectedColorSelector) {
			log.info("onRenderStyleSelected(): Selected {}.", selectedName);
			this.colorSelectorForSketch = selectedColorSelector;
			render();
		}
	}

	@FXML
	public void onViewRegionMap() {
		log.info("onViewRegionMap()");
		changeMapToRender(MapType.REGION_MAP);
	}

	@FXML
	public void onViewSketchMap() {
		log.info("onViewSketchMap()");
		changeMapToRender(MapType.SKETCH_MAP);
	}

	public void changeMapToRender(MapType newMapToRender) {
		if(mapToRender != newMapToRender) {
			log.info("changeMapToRender(): {} -> {}", mapToRender, newMapToRender);
			mapToRender = newMapToRender;
			updateViewControls();
			render();
		}
	}

	@FXML
	public void onMouseClicked(MouseEvent mouseEvent) {
		onMouseEvent(mouseEvent);
	}

	@FXML
	public void onMouseDragged(MouseEvent mouseEvent) {
		if (!mouseEvent.isPrimaryButtonDown()) {
			return;
		}

		onMouseEvent(mouseEvent);
	}

	private void onMouseEvent(MouseEvent mouseEvent) {
		double worldX = mapRenderer.convertToWorld(mouseEvent.getX());
		double worldY = mapRenderer.convertToWorld(mouseEvent.getY());

		if(mapStorage.getChangeTerrainTypeTool().use(worldX, worldY)) {
			menuBarController.updateHistory();
			render();
		}
	}

	@FXML
	public void onUndoClicked() {
		menuBarController.undo();
	}

	@FXML
	public void onRedoClicked() {
		menuBarController.redo();
	}

	private void updateViewControls() {
		if (mapToRender == MapType.REGION_MAP) {
			mapRenderer.setBorderBetweenCells(0);
			renderStyleComboBox.setDisable(true);
			viewRegionMapItem.setDisable(true);
			viewSketchMapItem.setDisable(false);
		} else {
			mapRenderer.setBorderBetweenCells(5);
			renderStyleComboBox.setDisable(false);
			viewRegionMapItem.setDisable(false);
			viewSketchMapItem.setDisable(true);
		}
	}
}
