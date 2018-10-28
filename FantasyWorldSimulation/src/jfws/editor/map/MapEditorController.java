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
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class MapEditorController {

	@FXML
	private ComboBox<String> terrainTypeComboBox;

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
	private MapRenderer<AbstractRegionCell> mapRenderer;

	private CommandHistory commandHistory = new CommandHistory();

	public MapEditorController() throws OutsideMapException {
		log.info("MapEditorController()");

		terrainTypeManager.load(new File("data/terrain-types.json"));
		defaultTerrainType = terrainTypeManager.getOrDefault("Plain");
		mountainTerrainType = terrainTypeManager.getOrDefault("Mountain");
		selectedTerrainType = mountainTerrainType;

		abstractRegionMap = new AbstractRegionMap(20, 10, defaultTerrainType);
		abstractRegionMap.getCells().getCell(5, 3).setTerrainType(mountainTerrainType);

		toCellMapper = new ToCellMapper<>(abstractRegionMap.getCells(), 20);
	}

	@FXML
	private void initialize() {
		log.info("initialize()");
		terrainTypeComboBox.setItems(FXCollections.observableArrayList(terrainTypeManager.getNames()));
		terrainTypeComboBox.getSelectionModel().select(selectedTerrainType.getName());

		canvasRenderer = new CanvasRenderer(sketchMapCanvas.getGraphicsContext2D());
		mapRenderer = new MapRenderer<>(AbstractRegionCell.TERRAIN_COLOR_SELECTOR, canvasRenderer, toCellMapper, 1);

		updateHistory();
		render();
	}

	private void render() {
		log.info("render()");
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

			if(cell.getTerrainType() == selectedTerrainType) {
				log.info("{}(): Cell already has the terrain type. x={} y={} terrain={}", text, mouseEvent.getX(), mouseEvent.getY(), cell.getTerrainType().getName());
				return;
			}

			int index = toCellMapper.getIndex(mouseEvent.getX(), mouseEvent.getY());

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
