package jfws.editor.map;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import jfws.generation.map.terrain.type.TerrainTypeConverter;
import jfws.generation.map.terrain.type.TerrainTypeJsonConverter;
import jfws.generation.map.terrain.type.TerrainTypeManager;
import jfws.util.io.ApacheFileUtils;
import jfws.util.io.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class MapEditorController {

	@FXML
	private ComboBox<String> terrainTypeComboBox;

	private FileUtils fileUtils = new ApacheFileUtils();
	private TerrainTypeConverter converter = new TerrainTypeJsonConverter();
	private TerrainTypeManager terrainTypeManager = new TerrainTypeManager(fileUtils, converter);

	public MapEditorController() {
		log.info("MapEditorController()");

		terrainTypeManager.load(new File("data/terrain-types.json"));
	}

	@FXML
	private void initialize() {
		log.info("initialize()");
		terrainTypeComboBox.setItems(FXCollections.observableArrayList(terrainTypeManager.getNames()));
	}

	@FXML
	public void onTerrainTypeSelected() {
		log.info("onTerrainTypeSelected(): terrainType={}", terrainTypeComboBox.getSelectionModel().getSelectedItem());
	}
}
