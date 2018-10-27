package jfws.editor.map;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import jfws.generation.region.AbstractRegionCell;
import jfws.generation.region.AbstractRegionMap;
import jfws.generation.region.terrain.TerrainType;
import jfws.generation.region.terrain.TerrainTypeConverter;
import jfws.generation.region.terrain.TerrainTypeJsonConverter;
import jfws.generation.region.terrain.TerrainTypeManager;
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

	private FileUtils fileUtils = new ApacheFileUtils();

	private CanvasRenderer canvasRenderer;

	private TerrainTypeConverter converter = new TerrainTypeJsonConverter();
	private TerrainTypeManager terrainTypeManager = new TerrainTypeManager(fileUtils, converter);
	private final TerrainType defaultTerrainType, mountainTerrainType;
	private TerrainType selectedTerrainType;
	private AbstractRegionMap abstractRegionMap;
	private ToCellMapper<AbstractRegionCell> toCellMapper;
	private MapRenderer<AbstractRegionCell> mapRenderer;

	public MapEditorController() throws OutsideMapException {
		log.info("MapEditorController()");

		terrainTypeManager.load(new File("data/terrain-types.json"));
		defaultTerrainType = terrainTypeManager.getOrDefault("Plain");
		mountainTerrainType = terrainTypeManager.getOrDefault("Mountain");
		selectedTerrainType = defaultTerrainType;

		abstractRegionMap = new AbstractRegionMap(20, 10, defaultTerrainType);
		abstractRegionMap.getCells().getCell(5, 3).setTerrainType(mountainTerrainType);

		toCellMapper = new ToCellMapper<>(abstractRegionMap.getCells(), 10);
	}

	@FXML
	private void initialize() {
		log.info("initialize()");
		terrainTypeComboBox.setItems(FXCollections.observableArrayList(terrainTypeManager.getNames()));
		terrainTypeComboBox.getSelectionModel().select(defaultTerrainType.getName());

		canvasRenderer = new CanvasRenderer(sketchMapCanvas.getGraphicsContext2D());
		mapRenderer = new MapRenderer<>(AbstractRegionCell.TERRAIN_COLOR_SELECTOR, canvasRenderer, toCellMapper);

		render();
	}

	private void render() {
		try {
			mapRenderer.render();
		} catch (OutsideMapException e) {
			e.printStackTrace();
		}
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
	public void onMouseClicked(MouseEvent e) {
		try {
			AbstractRegionCell cell = toCellMapper.getCell(e.getX(), e.getY());

			log.info("onMouseClicked(): x={} y={} oldTerrain={}", e.getX(), e.getY(), cell.getTerrainType().getName());

			cell.setTerrainType(selectedTerrainType);

			render();
		} catch (OutsideMapException e1) {
			log.info("onMouseClicked(): Outsid map! x={} y={}", e.getX(), e.getY());
		}
	}
}
