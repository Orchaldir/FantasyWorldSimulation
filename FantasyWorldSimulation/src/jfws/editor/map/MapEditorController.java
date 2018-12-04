package jfws.editor.map;

import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.stage.FileChooser;
import jfws.features.elevation.ElevationCellInterpolator;
import jfws.features.elevation.ElevationColorSelector;
import jfws.features.elevation.noise.ElevationNoise;
import jfws.features.elevation.noise.ElevationNoiseManager;
import jfws.features.elevation.noise.ElevationNoiseWithInterpolation;
import jfws.maps.region.RegionCell;
import jfws.maps.region.RegionMap;
import jfws.maps.sketch.SketchCell;
import jfws.maps.sketch.SketchMap;
import jfws.maps.sketch.elevation.ElevationGenerator;
import jfws.maps.sketch.elevation.ElevationGeneratorWithNoise;
import jfws.maps.sketch.rendering.TerrainColorSelector;
import jfws.util.map.MapInterpolator;
import jfws.util.map.MapRenderer;
import jfws.util.math.interpolation.BiTwoValueInterpolator;
import jfws.util.math.noise.ScalableNoise;
import jfws.util.math.noise.SimplexNoise;
import jfws.util.math.random.GeneratorWithRandom;
import jfws.util.rendering.CanvasRenderer;
import jfws.util.rendering.ColorSelector;
import jfws.util.rendering.ColorSelectorMap;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static javafx.scene.control.Alert.AlertType.ERROR;

@Slf4j
public class MapEditorController {

	public static final double WORLD_TO_SCREEN = 0.5;
	public static final int BORDER_BETWEEN_CELLS = 0;

	enum MapType {
		SKETCH_MAP,
		REGION_MAP
	}

	@FXML
	private ComboBox<String> terrainTypeComboBox, renderStyleComboBox;

	@FXML
	private Canvas sketchMapCanvas;

	@FXML
	private Button undoButton, redoButton;

	@FXML
	private MenuItem viewRegionMapItem;

	@FXML
	private MenuItem viewSketchMapItem;

	@FXML
	private MenuItem loadMapItem;

	@FXML
	private MenuItem saveMapItem;

	@FXML
	private MenuItem exportImageItem;

	private FileChooser mapChooser = new FileChooser();
	private FileChooser imageChooser = new FileChooser();

	private MapStorage mapStorage;

	private ElevationGenerator elevationGenerator = new ElevationGeneratorWithNoise(new GeneratorWithRandom(42));

	private ColorSelectorMap<SketchCell> colorSelectorMap;
	private ColorSelector<SketchCell> colorSelectorForSketch;

	private ColorSelector<RegionCell> colorSelectorForRegion;
	private MapInterpolator elevationInterpolator = ElevationCellInterpolator.createMapInterpolator(
			BiTwoValueInterpolator.createBiCosineInterpolator());

	private ElevationNoiseManager<RegionCell> elevationNoiseManager = new ElevationNoiseManager<>();
	private ElevationNoiseWithInterpolation elevationNoise = new ElevationNoiseWithInterpolation("hill",
			BiTwoValueInterpolator.createBilinearInterpolator(), new ScalableNoise(new SimplexNoise(), 50.0), 0);

	private MapType mapToRender =  MapType.SKETCH_MAP;
	private MapRenderer mapRenderer;

	public MapEditorController() {
		log.info("MapEditorController()");

		mapStorage = new MapStorage(50);
		mapStorage.getTerrainTypeManager().load(new File("data/terrain-types.json"));
		mapStorage.createEmptyMap(20,  10, "Plain");
		mapStorage.createTool("Hill");

		colorSelectorMap = new ColorSelectorMap<>(new TerrainColorSelector());
		colorSelectorMap.add(new ElevationColorSelector());
		colorSelectorForSketch = colorSelectorMap.getDefaultColorSelector();

		colorSelectorForRegion = new ElevationColorSelector<>();

		addExtension(mapChooser, "JSON", "*.json");
		setDirectory(mapChooser, "./data/map/");

		addExtension(imageChooser, "PNG", "*.png");
		setDirectory(imageChooser, ".");
	}

	private void addExtension(FileChooser fileChooser, String description, String extension) {
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extension);
		fileChooser.getExtensionFilters().add(extFilter);
	}

	private void setDirectory(FileChooser fileChooser, String relativePath) {
		String currentPath = Paths.get(relativePath).toAbsolutePath().normalize().toString();
		fileChooser.setInitialDirectory(new File(currentPath));
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		initTerrainTypeComboBox();

		renderStyleComboBox.setItems(FXCollections.observableArrayList(colorSelectorMap.getNames()));
		renderStyleComboBox.getSelectionModel().select(colorSelectorMap.getDefaultColorSelector().getName());

		elevationNoiseManager.add(elevationNoise);

		CanvasRenderer canvasRenderer = new CanvasRenderer(sketchMapCanvas.getGraphicsContext2D());
		mapRenderer = new MapRenderer(canvasRenderer, WORLD_TO_SCREEN, BORDER_BETWEEN_CELLS);

		loadMapItem.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));
		saveMapItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		exportImageItem.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));

		updateHistory();
		updateViewControls();
		render();
	}

	private void initTerrainTypeComboBox() {
		List<String> names = mapStorage.getTerrainTypeManager().getNamesSortedByGroup();

		terrainTypeComboBox.setItems(FXCollections.observableArrayList(names));
		terrainTypeComboBox.getSelectionModel().select(mapStorage.getChangeTerrainTypeTool().getTerrainType().getName());
	}

	private void render() {
		log.info("render()");
		SketchMap sketchMap = mapStorage.getSketchMap();
		RegionMap regionMap = mapStorage.getRegionMap();

		sketchMap.generateElevation(elevationGenerator);

		switch (mapToRender) {
			case REGION_MAP:
				elevationInterpolator.interpolate(sketchMap.getCellMap(), regionMap.getCellMap());

				for(ElevationNoise elevationNoise : elevationNoiseManager.getAll()) {
					elevationNoise.addTo(regionMap);
				}

				mapRenderer.render(regionMap.getToCellMapper(), colorSelectorForRegion);
				break;
			default:
				mapRenderer.render(sketchMap.getToCellMapper(), colorSelectorForSketch);
				break;
		}

		log.info("render(): Finished");
	}

	@FXML
	public void onLoadMap() {
		File file = mapChooser.showOpenDialog(sketchMapCanvas.getScene().getWindow());

		if (file != null) {
			log.info("onLoadMap(): file={}", file.getPath());

			try {
				SketchMap sketchMap = mapStorage.getSketchConverter().load(file);
				mapStorage.setSketchMap(sketchMap);
				render();
			} catch (IOException e) {
				Alert alert = new Alert(ERROR);
				alert.setTitle("Map loading failed!");
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			}
		}
		else {
			log.info("onLoadMap(): No file.");
		}
	}

	@FXML
	public void onSaveMap() {
		File file = mapChooser.showSaveDialog(sketchMapCanvas.getScene().getWindow());

		if (file != null) {
			log.info("onSaveMap(): file={}", file.getPath());

			try {
				mapStorage.getSketchConverter().save(file, mapStorage.getSketchMap());
			} catch (IOException e) {
				log.error("onSaveMap(): ", e);
			}
		}
		else {
			log.info("onSaveMap(): No file.");
		}
	}

	@FXML
	public void onExportImage() {
		File file = imageChooser.showSaveDialog(sketchMapCanvas.getScene().getWindow());

		if (file != null) {
			log.info("onExportImage(): file={}", file.getPath());
			WritableImage image = sketchMapCanvas.snapshot(null, null);
			BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);

			try {
				ImageIO.write(bImage, "PNG", file);
			} catch (IOException e) {
				log.error("onExportImage(): ", e);
			}
		}
		else {
			log.info("onExportImage(): No file.");
		}
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
			updateHistory();
			render();
		}
	}

	@FXML
	public void onUndoClicked() {
		log.info("onUndoClicked()");
		mapStorage.getCommandHistory().unExecute();
		updateHistory();
		render();
	}

	@FXML
	public void onRedoClicked() {
		log.info("onRedoClicked()");
		mapStorage.getCommandHistory().reExecute();
		updateHistory();
		render();
	}

	private void updateHistory() {
		undoButton.setDisable(!mapStorage.getCommandHistory().canUnExecute());
		redoButton.setDisable(!mapStorage.getCommandHistory().canReExecute());
	}

	private void updateViewControls() {
		switch (mapToRender) {
			case REGION_MAP:
				mapRenderer.setBorderBetweenCells(0);
				renderStyleComboBox.setDisable(true);
				viewRegionMapItem.setDisable(true);
				viewSketchMapItem.setDisable(false);
				break;
			default:
				mapRenderer.setBorderBetweenCells(5);
				renderStyleComboBox.setDisable(false);
				viewRegionMapItem.setDisable(false);
				viewSketchMapItem.setDisable(true);
				break;
		}
	}
}
