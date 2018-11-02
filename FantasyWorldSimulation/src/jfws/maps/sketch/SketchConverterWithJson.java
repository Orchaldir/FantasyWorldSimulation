package jfws.maps.sketch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jfws.maps.sketch.terrain.TerrainType;
import jfws.util.io.FileUtils;
import jfws.util.map.Map2d;
import jfws.util.map.OutsideMapException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SketchConverterWithJson implements SketchConverter {

	private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

	public static final String VERSION = "version";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String USED_TERRAIN_TYPES = "used_terrain_types";
	public static final String TERRAIN_TYPE_MAP = "terrain_type_map";

	private final FileUtils fileUtils;
	private final Map<TerrainType,Integer> usedTerrainTypes = new HashMap<>();

	public SketchConverterWithJson(FileUtils fileUtils) {
		this.fileUtils = fileUtils;
	}

	@Override
	public void save(File file, SketchMap map) throws IOException {
		try {
			log.info("save(): file={}", file.getPath());

			String text = convertToJson(map);

			fileUtils.writeWholeFile(file, text);
		} catch (IOException e) {
			log.warn("save(): IOException: {}", e.getMessage());
			throw e;
		}
	}

	public String convertToJson(SketchMap map) throws IOException {
		Map2d<SketchCell> cells = map.getCells();

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(VERSION, SketchMap.VERSION);
		jsonObject.addProperty(WIDTH, cells.getWidth());
		jsonObject.addProperty(HEIGHT, cells.getHeight());

		saveUsedTerrainTypes(jsonObject, cells);

		try {
			saveTerrainTypeMap(jsonObject, cells);
		} catch (OutsideMapException e) {
			throw new IOException("Outside map!", e);
		}

		return gson.toJson(jsonObject);
	}

	private void saveUsedTerrainTypes(JsonObject jsonObject, Map2d<SketchCell> cells) {
		usedTerrainTypes.clear();

		JsonArray jsonArray = new JsonArray();

		for (SketchCell cell : cells.getCells()) {
			TerrainType type = cell.getTerrainType();

			if(!usedTerrainTypes.containsKey(type)) {
				int id  = usedTerrainTypes.size();
				log.info("saveUsedTerrainTypes(): type={} id={}", type.getName(), id);
				usedTerrainTypes.put(type, id);

				jsonArray.add(type.getName());
			}
		}

		log.info("saveUsedTerrainTypes(): types={}", usedTerrainTypes.size());

		jsonObject.add(USED_TERRAIN_TYPES, jsonArray);
	}

	private void saveTerrainTypeMap(JsonObject jsonObject, Map2d<SketchCell> cells) throws OutsideMapException {
		JsonArray rowsArray = new JsonArray();

		for(int y = 0; y < cells.getHeight(); y++) {
			StringBuilder row = new StringBuilder();

			for(int x = 0; x < cells.getWidth(); x++) {
				SketchCell cell = cells.getCell(x, y);

				row.append(usedTerrainTypes.getOrDefault(cell.getTerrainType(), -1));


				if(x < cells.getWidth()-1) {
					row.append(',');
				}
			}

			rowsArray.add(row.toString());
		}

		jsonObject.add(TERRAIN_TYPE_MAP, rowsArray);
	}
}
