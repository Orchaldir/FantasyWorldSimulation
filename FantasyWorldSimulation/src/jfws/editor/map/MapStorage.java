package jfws.editor.map;

import jfws.editor.map.tool.ChangeTerrainTypeTool;
import jfws.maps.region.RegionMap;
import jfws.maps.sketch.SketchConverter;
import jfws.maps.sketch.SketchConverterWithJson;
import jfws.maps.sketch.SketchMap;
import jfws.maps.sketch.terrain.TerrainType;
import jfws.maps.sketch.terrain.TerrainTypeConverter;
import jfws.maps.sketch.terrain.TerrainTypeConverterWithJson;
import jfws.maps.sketch.terrain.TerrainTypeManager;
import jfws.util.command.CommandHistory;
import jfws.util.io.ApacheFileUtils;
import jfws.util.io.FileUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Getter
@Slf4j
public class MapStorage {

	private TerrainTypeConverter terrainTypeConverter;
	private TerrainTypeManager terrainTypeManager;

	private SketchMap sketchMap;
	private SketchConverter sketchConverter;

	private final int cellsPerSketchCell;
	private RegionMap regionMap;

	private CommandHistory commandHistory;
	private ChangeTerrainTypeTool changeTerrainTypeTool;

	public MapStorage(int cellsPerSketchCell) {
		this.cellsPerSketchCell = cellsPerSketchCell;
		FileUtils fileUtils = new ApacheFileUtils();

		terrainTypeConverter = new TerrainTypeConverterWithJson();
		terrainTypeManager = new TerrainTypeManager(fileUtils, terrainTypeConverter);

		sketchConverter = new SketchConverterWithJson(fileUtils, terrainTypeManager);

		commandHistory = new CommandHistory();
	}

	public MapStorage(TerrainTypeConverter terrainTypeConverter, TerrainTypeManager terrainTypeManager,
					  SketchConverter sketchConverter, CommandHistory commandHistory, int cellsPerSketchCell) {
		this.terrainTypeConverter = terrainTypeConverter;
		this.terrainTypeManager = terrainTypeManager;
		this.sketchConverter = sketchConverter;
		this.commandHistory = commandHistory;
		this.cellsPerSketchCell = cellsPerSketchCell;
	}

	public void createEmptyMap(int width, int height, String defaultTerrainTypeName) {
		log.info("createEmptyMap(): width={} height={} defaultTerrainType={}", width, height, defaultTerrainTypeName);

		TerrainType defaultTerrainType = terrainTypeManager.getOrDefault(defaultTerrainTypeName);

		setSketchMap(new SketchMap(width, height, defaultTerrainType));
	}

	public void createTool(String terrainTypeName) {
		if(sketchMap == null) {
			throw  new IllegalStateException("No map!");
		}

		TerrainType terrainType = terrainTypeManager.getOrDefault(terrainTypeName);

		log.info("createTool(): terrainType={} isDefault={}", terrainTypeName, terrainType.isDefault());

		changeTerrainTypeTool = new ChangeTerrainTypeTool(commandHistory, sketchMap, terrainType);
	}

	public void changeTypeOfTool(String terrainTypeName) {
		if(sketchMap == null) {
			throw  new IllegalStateException("No map!");
		}

		TerrainType terrainType = terrainTypeManager.getOrDefault(terrainTypeName);

		log.info("changeTypeOfTool(): terrainType={} isDefault={}", terrainTypeName, terrainType.isDefault());

		changeTerrainTypeTool.changeTerrainType(terrainType);
	}

	public void setSketchMap(SketchMap newSketchMap) {
		log.info("setSketchMap()");
		sketchMap = newSketchMap;
		regionMap = RegionMap.fromSketchMap(sketchMap, cellsPerSketchCell);

		if(changeTerrainTypeTool != null) {
			changeTerrainTypeTool.setSketchMap(sketchMap);
		}
	}
}
