package jfws.editor.map;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import jfws.generation.region.AbstractRegionCell;
import jfws.generation.region.AbstractRegionMap;
import jfws.generation.region.ChangeTerrainTypeCommand;
import jfws.generation.region.elevation.BaseElevationGenerator;
import jfws.generation.region.rendering.ElevationColorSelector;
import jfws.generation.region.rendering.TerrainColorSelector;
import jfws.generation.region.terrain.TerrainType;
import jfws.generation.region.terrain.TerrainTypeConverter;
import jfws.generation.region.terrain.TerrainTypeJsonConverter;
import jfws.generation.region.terrain.TerrainTypeManager;
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

@Slf4j
public class MapEditorController {

	@FXML
	private ComboBox<String> terrainTypeComboBox, renderStyleComboBox;

	@FXML
	private Canvas sketchMapCanvas;

	@FXML
	private Button undoButton, redoButton;

	private FileUtils fileUtils = new ApacheFileUtils();

	private CanvasRenderer canvasRenderer;

	private TerrainTypeConverter converter = new TerrainTypeJsonConverter();
	private TerrainTypeManager terrainTypeManager = new TerrainTypeManager(fileUtils, converter);
	private final TerrainType defaultTerrainType, mountainTerrainType;
	private TerrainType selectedTerrainType;
	private AbstractRegionMap abstractRegionMap;
	private ToCellMapper<AbstractRegionCell> toCellMapper;
	private BaseElevationGenerator elevationGenerator = new BaseElevationGenerator();
	private MapRenderer<AbstractRegionCell> mapRenderer;

	private ColorSelectorMap<AbstractRegionCell> colorSelectorMap;

	private CommandHistory commandHistory = new CommandHistory();

	public MapEditorController() throws OutsideMapException {
		log.info("MapEditorController()");

		terrainTypeManager.load(new File("data/terrain-types.json"));
		defaultTerrainType = terrainTypeManager.getOrDefault("Plain");
		mountainTerrainType = terrainTypeManager.getOrDefault("Medium Mountain");
		selectedTerrainType = mountainTerrainType;

		abstractRegionMap = new AbstractRegionMap(20, 10, defaultTerrainType);
		abstractRegionMap.getCells().getCell(5, 3).setTerrainType(mountainTerrainType);

		toCellMapper = new ToCellMapper<>(abstractRegionMap.getCells(), 20);

		colorSelectorMap = new ColorSelectorMap<>(new TerrainColorSelector());
		colorSelectorMap.add(new ElevationColorSelector());
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		terrainTypeComboBox.setItems(FXCollections.observableArrayList(terrainTypeManager.getNames()));
		terrainTypeComboBox.getSelectionModel().select(selectedTerrainType.getName());

		renderStyleComboBox.setItems(FXCollections.observableArrayList(colorSelectorMap.getNames()));
		renderStyleComboBox.getSelectionModel().select(colorSelectorMap.getDefaultColorSelector().getName());

		canvasRenderer = new CanvasRenderer(sketchMapCanvas.getGraphicsContext2D());
		mapRenderer = new MapRenderer<>(colorSelectorMap.getDefaultColorSelector(), canvasRenderer, toCellMapper, 1);

		updateHistory();
		render();
	}

	private void render() {
		log.info("render()");
		abstractRegionMap.generateElevation(elevationGenerator);
		mapRenderer.render();
	}

	private void selectTerrainType(TerrainType selectedTerrainType) {
		if(this.selectedTerrainType != selectedTerrainType) {
			log.info("selectTerrainType(): {} -> {}", this.selectedTerrainType.getName(), selectedTerrainType.getName());
			this.selectedTerrainType = selectedTerrainType;
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
		ColorSelector<AbstractRegionCell> selectedColorSelector = colorSelectorMap.get(selectedName);

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
		try {
			AbstractRegionCell cell = toCellMapper.getCell(mouseEvent.getX(), mouseEvent.getY());
			int index = toCellMapper.getIndex(mouseEvent.getX(), mouseEvent.getY());

			if(cell.getTerrainType() == selectedTerrainType) {
				log.info("{}(): Cell {} is already {}. x={} y={}", text, index, cell.getTerrainType().getName(), mouseEvent.getX(), mouseEvent.getY());
				return;
			}

			log.info("{}(): x={} y={} oldTerrain={}", text, mouseEvent.getX(), mouseEvent.getY(), cell.getTerrainType().getName());

			ChangeTerrainTypeCommand command = new ChangeTerrainTypeCommand(abstractRegionMap, index, selectedTerrainType);
			commandHistory.execute(command);

			updateHistory();
			render();
		} catch (OutsideMapException e1) {
			log.info("{}(): Outside map! x={} y={}", text, mouseEvent.getX(), mouseEvent.getY());
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
