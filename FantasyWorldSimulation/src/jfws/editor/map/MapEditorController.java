package jfws.editor.map;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import jfws.features.elevation.ElevationColorSelector;
import jfws.features.elevation.ElevationInterpolator;
import jfws.features.elevation.noise.ElevationNoise;
import jfws.features.elevation.noise.ElevationNoiseManager;
import jfws.features.elevation.noise.ElevationNoiseWithInterpolation;
import jfws.maps.region.RegionCell;
import jfws.maps.region.RegionMap;
import jfws.maps.sketch.ChangeTerrainTypeCommand;
import jfws.maps.sketch.SketchCell;
import jfws.maps.sketch.SketchConverterWithJson;
import jfws.maps.sketch.SketchMap;
import jfws.maps.sketch.elevation.ElevationGenerator;
import jfws.maps.sketch.elevation.ElevationGeneratorWithNoise;
import jfws.maps.sketch.rendering.TerrainColorSelector;
import jfws.maps.sketch.terrain.TerrainType;
import jfws.maps.sketch.terrain.TerrainTypeConverter;
import jfws.maps.sketch.terrain.TerrainTypeConverterWithJson;
import jfws.maps.sketch.terrain.TerrainTypeManager;
import jfws.util.command.CommandHistory;
import jfws.util.io.ApacheFileUtils;
import jfws.util.io.FileUtils;
import jfws.util.map.MapRenderer;
import jfws.util.map.OutsideMapException;
import jfws.util.map.ToCellMapper;
import jfws.util.math.interpolation.BiTwoValueInterpolator;
import jfws.util.math.noise.SimplexNoise;
import jfws.util.math.random.GeneratorWithRandom;
import jfws.util.rendering.CanvasRenderer;
import jfws.util.rendering.ColorSelector;
import jfws.util.rendering.ColorSelectorMap;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static javafx.scene.control.Alert.AlertType.ERROR;
import static jfws.maps.sketch.terrain.TerrainType.NO_GROUP;

@Slf4j
public class MapEditorController {

	public static final double WORLD_TO_SCREEN = 0.5;
	public static final int BORDER_BETWEEN_CELLS = 0;
	public static final int CELLS_PER_SKETCH_CELL = 50;

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
	private Spinner<Integer> hillNoiseSpinner;

	@FXML
	private MenuItem viewRegionMapItem, viewSketchMapItem;

	private FileChooser fileChooser = new FileChooser();
	private FileUtils fileUtils = new ApacheFileUtils();

	private TerrainTypeConverter converter = new TerrainTypeConverterWithJson();
	private TerrainTypeManager terrainTypeManager = new TerrainTypeManager(fileUtils, converter);
	private TerrainType selectedTerrainType;
	private SketchMap sketchMap;
	private ElevationGenerator elevationGenerator = new ElevationGeneratorWithNoise(new GeneratorWithRandom(42));
	private SketchConverterWithJson sketchConverter = new SketchConverterWithJson(fileUtils, terrainTypeManager);

	private ColorSelectorMap<SketchCell> colorSelectorMap;
	private ColorSelector<SketchCell> colorSelectorForSketch;

	private RegionMap regionMap;
	private ColorSelector<RegionCell> colorSelectorForRegion;
	private ElevationInterpolator elevationInterpolator = new ElevationInterpolator(BiTwoValueInterpolator.createBiCosineInterpolator());

	private ElevationNoiseManager<RegionCell> elevationNoiseManager = new ElevationNoiseManager<>();
	private ElevationNoiseWithInterpolation elevationNoise = new ElevationNoiseWithInterpolation("hill", BiTwoValueInterpolator.createBilinearInterpolator(), new SimplexNoise(), 50.0, 0);

	private MapType mapToRender =  MapType.SKETCH_MAP;
	private MapRenderer mapRenderer;

	private CommandHistory commandHistory = new CommandHistory();

	public MapEditorController() {
		log.info("MapEditorController()");

		terrainTypeManager.load(new File("data/terrain-types.json"));
		TerrainType defaultTerrainType = terrainTypeManager.getOrDefault("Plain");
		selectedTerrainType = terrainTypeManager.getOrDefault("Medium Mountain");

		sketchMap = new SketchMap(20, 10, defaultTerrainType);

		colorSelectorMap = new ColorSelectorMap<>(new TerrainColorSelector());
		colorSelectorMap.add(new ElevationColorSelector());
		colorSelectorForSketch = colorSelectorMap.getDefaultColorSelector();

		regionMap = RegionMap.fromSketchMap(sketchMap, CELLS_PER_SKETCH_CELL);
		colorSelectorForRegion = new ElevationColorSelector<>();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON", "*.json");
		fileChooser.getExtensionFilters().add(extFilter);
		String currentPath = Paths.get("./data/map/").toAbsolutePath().normalize().toString();
		fileChooser.setInitialDirectory(new File(currentPath));
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		initTerrainTypeComboBox();

		renderStyleComboBox.setItems(FXCollections.observableArrayList(colorSelectorMap.getNames()));
		renderStyleComboBox.getSelectionModel().select(colorSelectorMap.getDefaultColorSelector().getName());

		hillNoiseSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, (int) elevationNoise.getResolution()));

		hillNoiseSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
			log.info("onHillNoiseSpinnerChanged(): {} -> {}", oldValue, newValue);
			elevationNoise.setResolution(newValue);
			render();
		});

		elevationNoiseManager.add(elevationNoise);

		CanvasRenderer canvasRenderer = new CanvasRenderer(sketchMapCanvas.getGraphicsContext2D());
		mapRenderer = new MapRenderer(canvasRenderer, WORLD_TO_SCREEN, BORDER_BETWEEN_CELLS);

		updateHistory();
		updateViewControls();
		render();
	}

	private void initTerrainTypeComboBox() {
		List<String> names = terrainTypeManager.getNamesForGroup(NO_GROUP);

		for(String group : terrainTypeManager.getGroups()) {
			names.addAll(terrainTypeManager.getNamesForGroup(group));
		}

		terrainTypeComboBox.setItems(FXCollections.observableArrayList(names));
		terrainTypeComboBox.getSelectionModel().select(selectedTerrainType.getName());
	}

	private void render() {
		log.info("render()");
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

	private void selectTerrainType(TerrainType selectedTerrainType) {
		if(this.selectedTerrainType != selectedTerrainType) {
			log.info("selectTerrainType(): {} -> {}", this.selectedTerrainType.getName(), selectedTerrainType.getName());
			this.selectedTerrainType = selectedTerrainType;
		}
	}

	@FXML
	public void onLoadMap() {
		File file = fileChooser.showOpenDialog(sketchMapCanvas.getScene().getWindow());

		if (file != null) {
			log.info("onLoadMap(): file={}", file.getPath());

			try {
				sketchMap = sketchConverter.load(file);
				regionMap = RegionMap.fromSketchMap(sketchMap, CELLS_PER_SKETCH_CELL);
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
		File file = fileChooser.showSaveDialog(sketchMapCanvas.getScene().getWindow());

		if (file != null) {
			log.info("onSaveMap(): file={}", file.getPath());

			try {
				 sketchConverter.save(file, sketchMap);
			} catch (IOException e) {
				e.printStackTrace();
				log.error("onSaveMap(): ", e);
			}
		}
		else {
			log.info("onSaveMap(): No file.");
		}
	}

	@FXML
	public void onTerrainTypeSelected() {
		String selectedName = terrainTypeComboBox.getSelectionModel().getSelectedItem();
		TerrainType selectedType = terrainTypeManager.getOrDefault(selectedName);
		log.info("onTerrainTypeSelected(): terrainType={} isDefault={}", selectedName, selectedType.isDefault());
		selectTerrainType(selectedType);
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
		onMouseEvent(mouseEvent,  "onMouseClicked");
	}

	@FXML
	public void onMouseDragged(MouseEvent mouseEvent) {
		if (!mouseEvent.isPrimaryButtonDown()) {
			return;
		}

		onMouseEvent(mouseEvent, "onMouseDragged");
	}

	private void onMouseEvent(MouseEvent mouseEvent, String text) {
		double worldX = mapRenderer.convertToWorld(mouseEvent.getX());
		double worldY = mapRenderer.convertToWorld(mouseEvent.getY());
		String mousePosText = String.format("Canvas: x=%.0f y=%.0f World: x=%.1f y=%.1f",
				mouseEvent.getX(), mouseEvent.getY(), worldX, worldY);

		onMouseEvent(text, worldX, worldY, mousePosText);
	}

	private void onMouseEvent(String text, double worldX, double worldY, String mousePosText) {
		try {
			ToCellMapper<SketchCell> toCellMapper = sketchMap.getToCellMapper();
			SketchCell cell = toCellMapper.getCell(worldX, worldY);
			int index = toCellMapper.getIndex(worldX, worldY);

			if(cell.getTerrainType() == selectedTerrainType) {
				log.info("{}(): Cell {} is already {}. {}",
						text, index, cell.getTerrainType().getName(), mousePosText);
				return;
			}

			log.info("{}(): {} oldTerrain={}", text, mousePosText, cell.getTerrainType().getName());

			ChangeTerrainTypeCommand command = new ChangeTerrainTypeCommand(sketchMap, index, selectedTerrainType);
			commandHistory.execute(command);

			updateHistory();
			render();
		} catch (OutsideMapException e1) {
			log.info("{}(): Outside map! {}", text, mousePosText);
		}
	}

	@FXML
	public void onUndoClicked() {
		log.info("onUndoClicked()");
		commandHistory.unExecute();
		updateHistory();
		render();
	}

	@FXML
	public void onRedoClicked() {
		log.info("onRedoClicked()");
		commandHistory.reExecute();
		updateHistory();
		render();
	}

	private void updateHistory() {
		undoButton.setDisable(!commandHistory.canUnExecute());
		redoButton.setDisable(!commandHistory.canReExecute());
	}

	private void updateViewControls() {
		switch (mapToRender) {
			case REGION_MAP:
				hillNoiseSpinner.setDisable(false);
				mapRenderer.setBorderBetweenCells(0);
				renderStyleComboBox.setDisable(true);
				viewRegionMapItem.setDisable(true);
				viewSketchMapItem.setDisable(false);
				break;
			default:
				hillNoiseSpinner.setDisable(true);
				mapRenderer.setBorderBetweenCells(5);
				renderStyleComboBox.setDisable(false);
				viewRegionMapItem.setDisable(false);
				viewSketchMapItem.setDisable(true);
				break;
		}
	}
}
