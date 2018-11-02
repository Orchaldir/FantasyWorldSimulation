package jfws.maps.sketch;

import com.google.gson.*;
import jfws.maps.sketch.terrain.TerrainType;
import jfws.maps.sketch.terrain.TerrainTypeManager;
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

	// json properties
	public static final String VERSION = "version";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String USED_TERRAIN_TYPES = "used_terrain_types";
	public static final String TERRAIN_TYPE_MAP = "terrain_type_map";

	// exceptions
	public static final String NOT_A_JSON_OBJECT = "Not a json object!";
	public static final String INVALID_MAP_SIZE = "Invalid map size!";
	public static final String NO_VERSION = "No version!";
	public static final String WRONG_VERSION_FORMAT = "Wrong version format!";
	public static final String WRONG_VERSION = "Wrong version!";
	public static final String NO_SIZE = "No size!";
	public static final String WRONG_SIZE_FORMAT = "Wrong size format!";

	private final FileUtils fileUtils;
	private final TerrainTypeManager terrainTypeManager;

	private final Map<TerrainType,Integer> usedTerrainTypes = new HashMap<>();

	private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
	private final JsonParser parser = new JsonParser();

	public SketchConverterWithJson(FileUtils fileUtils, TerrainTypeManager terrainTypeManager) {
		this.fileUtils = fileUtils;
		this.terrainTypeManager = terrainTypeManager;
	}

	private void throwException(String text) throws IOException {
		log.warn("throwException(): {}", text);
		throw new IOException(text);
	}

	// load

	@Override
	public SketchMap load(File file) throws IOException {
		try {
			String text = fileUtils.readWholeFile(file);

			return parseString(text);
		} catch (IOException e) {
			log.warn("load(): IOException: {}", e.getMessage());
			throw e;
		}
	}

	public SketchMap parseString(String text) throws IOException {
		try {
			return parseJsonElement(parser.parse(text));
		} catch(NullPointerException e) {
			log.warn("parseString(): NullPointerException: {}", e.getMessage());
			throw new IOException(e);
		} catch(JsonParseException e) {
			log.warn("parseString(): JsonParseException: {}", e.getMessage());
			throw new IOException(e);
		}
	}

	private SketchMap parseJsonElement(JsonElement jsonElement) throws IOException {
		if (!jsonElement.isJsonObject()) {
			throwException(NOT_A_JSON_OBJECT);
		}

		JsonObject jsonObject = jsonElement.getAsJsonObject();

		checkVersion(jsonObject);

		int width = getSize(jsonObject, WIDTH);
		int height = getSize(jsonObject, HEIGHT);

		if(width < 1 || height < 1) {
			throwException(INVALID_MAP_SIZE);
		}

		return null;
	}

	private void checkVersion(JsonObject jsonObject) throws IOException {
		if(!jsonObject.has(VERSION)) {
			throwException(NO_VERSION);
		}

		JsonElement versionElement = jsonObject.get(VERSION);

		if(!versionElement.isJsonPrimitive()) {
			throwException(WRONG_VERSION_FORMAT);
		}

		try {
			int version = versionElement.getAsInt();

			if(version != SketchMap.VERSION) {
				throwException(WRONG_VERSION);
			}
		} catch (NumberFormatException e) {
			throwException(WRONG_VERSION_FORMAT);
		}
	}

	private int getSize(JsonObject jsonObject, String property) throws IOException {
		if(!jsonObject.has(property)) {
			throwException(NO_SIZE);
		}

		try {
			return jsonObject.get(property).getAsInt();
		} catch (NumberFormatException e) {
			throwException(WRONG_SIZE_FORMAT);
		}

		return 0; // never reached?
	}

	// save

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
