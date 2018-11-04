package jfws.editor.map;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import jfws.maps.sketch.SketchCell;
import jfws.maps.sketch.SketchConverterWithJson;
import jfws.maps.sketch.SketchMap;
import jfws.maps.sketch.ChangeTerrainTypeCommand;
import jfws.maps.sketch.elevation.BaseElevationGenerator;
import jfws.maps.sketch.rendering.ElevationColorSelector;
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
import jfws.util.rendering.CanvasRenderer;
import jfws.util.rendering.ColorSelector;
import jfws.util.rendering.ColorSelectorMap;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static javafx.scene.control.Alert.AlertType.ERROR;

@Slf4j
public class MapEditorController {

	public static final double SKETCH_TO_WORLD = 100.0;
	public static final double WORLD_TO_SCREEN = 0.2;
	public static final int BORDER_BETWEEN_CELLS = 5;

	@FXML
	private ComboBox<String> terrainTypeComboBox, renderStyleComboBox;

	@FXML
	private Canvas sketchMapCanvas;

	@FXML
	private Button undoButton, redoButton;

	private FileChooser fileChooser = new FileChooser();
	private FileUtils fileUtils = new ApacheFileUtils();

	private CanvasRenderer canvasRenderer;

	private TerrainTypeConverter converter = new TerrainTypeConverterWithJson();
	private TerrainTypeManager terrainTypeManager = new TerrainTypeManager(fileUtils, converter);
	private final TerrainType defaultTerrainType, mountainTerrainType;
	private TerrainType selectedTerrainType;
	private SketchMap sketchMap;
	private ToCellMapper<SketchCell> toCellMapper;
	private BaseElevationGenerator elevationGenerator = new BaseElevationGenerator();
	private MapRenderer<SketchCell> mapRenderer;
	private SketchConverterWithJson sketchConverter = new SketchConverterWithJson(fileUtils, terrainTypeManager);

	private ColorSelectorMap<SketchCell> colorSelectorMap;

	private CommandHistory commandHistory = new CommandHistory();

	public MapEditorController() {
		log.info("MapEditorController()");

		terrainTypeManager.load(new File("data/terrain-types.json"));
		defaultTerrainType = terrainTypeManager.getOrDefault("Plain");
		mountainTerrainType = terrainTypeManager.getOrDefault("Medium Mountain");
		selectedTerrainType = mountainTerrainType;

		sketchMap = new SketchMap(20, 10, defaultTerrainType);

		toCellMapper = new ToCellMapper<>(sketchMap.getCells(), SKETCH_TO_WORLD);

		colorSelectorMap = new ColorSelectorMap<>(new TerrainColorSelector());
		colorSelectorMap.add(new ElevationColorSelector());

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON", "*.json");
		fileChooser.getExtensionFilters().add(extFilter);
		String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
		fileChooser.setInitialDirectory(new File(currentPath));
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		terrainTypeComboBox.setItems(FXCollections.observableArrayList(terrainTypeManager.getNames()));
		terrainTypeComboBox.getSelectionModel().select(selectedTerrainType.getName());

		renderStyleComboBox.setItems(FXCollections.observableArrayList(colorSelectorMap.getNames()));
		renderStyleComboBox.getSelectionModel().select(colorSelectorMap.getDefaultColorSelector().getName());

		canvasRenderer = new CanvasRenderer(sketchMapCanvas.getGraphicsContext2D());
		mapRenderer = new MapRenderer<>(colorSelectorMap.getDefaultColorSelector(), canvasRenderer, toCellMapper, WORLD_TO_SCREEN, BORDER_BETWEEN_CELLS);

		updateHistory();
		render();
	}

	private void render() {
		log.info("render()");
		sketchMap.generateElevation(elevationGenerator);
		mapRenderer.render();
	}

	private void selectTerrainType(TerrainType selectedTerrainType) {
		if(this.selectedTerrainType != selectedTerrainType) {
			log.info("selectTerrainType(): {} -> {}", this.selectedTerrainType.getName(), selectedTerrainType.getName());
			this.selectedTerrainType = selectedTerrainType;
		}
	}

	@FXML
	public void onLoadMap() {
		File file = fileChooser.showSaveDialog(sketchMapCanvas.getScene().getWindow());

		if (file != null) {
			log.info("onLoadMap(): file={}", file.getPath());

			try {
				sketchMap = sketchConverter.load(file);
				toCellMapper = new ToCellMapper<>(sketchMap.getCells(), SKETCH_TO_WORLD);
				mapRenderer.setToCellMapper(toCellMapper);
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
		File file = fileChooser.showOpenDialog(sketchMapCanvas.getScene().getWindow());

		if (file != null) {
			log.info("onSaveMap(): file={}", file.getPath());

			try {
				 sketchConverter.save(file, sketchMap);
			} catch (IOException e) {
				e.printStackTrace();
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

		if(mapRenderer.getColorSelector() != selectedColorSelector) {
			log.info("onRenderStyleSelected(): Selected {}.", selectedName);
			mapRenderer.setColorSelector(selectedColorSelector);
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
}
