package jfws.editor.map;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
import jfws.generation.region.AbstractRegionMap;
import jfws.generation.region.terrain.TerrainType;
import jfws.generation.region.terrain.TerrainTypeConverter;
import jfws.generation.region.terrain.TerrainTypeJsonConverter;
import jfws.generation.region.terrain.TerrainTypeManager;
import jfws.util.io.ApacheFileUtils;
import jfws.util.io.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class MapEditorController {

	@FXML
	private ComboBox<String> terrainTypeComboBox;

	@FXML
	private Canvas sketchMapCanvas;

	private FileUtils fileUtils = new ApacheFileUtils();
	private TerrainTypeConverter converter = new TerrainTypeJsonConverter();
	private TerrainTypeManager terrainTypeManager = new TerrainTypeManager(fileUtils, converter);
	private final TerrainType defaultTerrainType;
	private TerrainType selectedTerrainType;
	private AbstractRegionMap abstractRegionMap;

	public MapEditorController() {
		log.info("MapEditorController()");

		terrainTypeManager.load(new File("data/terrain-types.json"));
		defaultTerrainType = terrainTypeManager.getOrDefault("Plain");
		selectedTerrainType = defaultTerrainType;

		abstractRegionMap = new AbstractRegionMap(20, 10, defaultTerrainType);
	}

	@FXML
	private void initialize() {
		log.info("initialize()");
		terrainTypeComboBox.setItems(FXCollections.observableArrayList(terrainTypeManager.getNames()));
		terrainTypeComboBox.getSelectionModel().select(defaultTerrainType.getName());

		GraphicsContext gc = sketchMapCanvas.getGraphicsContext2D();

		gc.setFill(Color.AQUA);
		gc.fillRect(10,10,100,100);
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
}
