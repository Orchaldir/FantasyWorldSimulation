package jfws.maps.sketch;

import com.google.gson.*;
import jfws.maps.sketch.terrain.TerrainType;
import jfws.maps.sketch.terrain.TerrainTypeManager;
import jfws.util.io.FileUtils;
import jfws.util.map.ArrayCellMap2D;
import jfws.util.map.CellMap2d;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static jfws.features.elevation.ElevationCell.DEFAULT_ELEVATION;

@Slf4j
public class SketchConverterWithJson implements SketchConverter {

	// json properties
	public static final String VERSION = "version";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String USED_TERRAIN_TYPES = "used_terrain_types";
	public static final String TERRAIN_TYPE_MAP = "terrain_type_map";
	public static final String TERRAIN_TYPE_ROW_SEPARATOR = ",";

	// exceptions

	public static final String NOT_A_JSON_OBJECT = "Not a json object!";
	public static final String INVALID_MAP_SIZE = "Invalid map size!";
	public static final String NO_VERSION = createNoPropertyException(VERSION);
	public static final String WRONG_VERSION_FORMAT = createWrongFormatException(VERSION);
	public static final String WRONG_VERSION = "Wrong version!";
	public static final String NO_SIZE = "No size!";
	public static final String WRONG_SIZE_FORMAT = "Wrong size format!";
	public static final String NO_USED_TERRAIN_TYPES = createNoPropertyException(USED_TERRAIN_TYPES);
	public static final String WRONG_USED_TERRAIN_TYPES_FORMAT = createWrongFormatException(USED_TERRAIN_TYPES);
	public static final String NO_TERRAIN_TYPE_MAP = createNoPropertyException(TERRAIN_TYPE_MAP);
	public static final String WRONG_TERRAIN_TYPE_MAP_FORMAT = createWrongFormatException(TERRAIN_TYPE_MAP);
	public static final String WRONG_TERRAIN_TYPE_MAP_SIZE = String.format("Property %s has wrong size!", TERRAIN_TYPE_MAP);
	public static final String WRONG_TERRAIN_TYPE_ROW_FORMAT = "Wrong terrain type row format!";
	public static final String NOT_USED_TERRAIN_TYPE = "Contains not used terrain type!";

	// members
	private final FileUtils fileUtils;
	private final TerrainTypeManager terrainTypeManager;

	// temporary map data
	private final Map<TerrainType,Integer> terrainTypeToIdMap = new HashMap<>();
	private final Map<Integer,TerrainType> idToTerrainTypeMap = new HashMap<>();
	private int width;
	private int height;

	// json
	private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
	private final JsonParser parser = new JsonParser();

	private static String createNoPropertyException(String property) {
		return String.format("Map does not contain property %s!", property);
	}

	private static String createWrongFormatException(String property) {
		return String.format("Property %s has a wrong format!", property);
	}

	public SketchConverterWithJson(FileUtils fileUtils, TerrainTypeManager terrainTypeManager) {
		this.fileUtils = fileUtils;
		this.terrainTypeManager = terrainTypeManager;
	}

	private IOException createException(String text) {
		log.warn("createException(): {}", text);
		return new IOException(text);
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

	protected SketchMap parseString(String text) throws IOException {
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
			throw createException(NOT_A_JSON_OBJECT);
		}

		JsonObject jsonObject = jsonElement.getAsJsonObject();

		checkVersion(jsonObject);
		parseMapSize(jsonObject);

		parseUsedTerrainTypes(jsonObject);

		ArrayCellMap2D<SketchCell> cellMap = parseTerrainTypeMap(jsonObject);

		return new SketchMap(cellMap);
	}

	private void checkVersion(JsonObject jsonObject) throws IOException {
		if(!jsonObject.has(VERSION)) {
			throw createException(NO_VERSION);
		}

		JsonElement versionElement = jsonObject.get(VERSION);

		if(!versionElement.isJsonPrimitive()) {
			throw createException(WRONG_VERSION_FORMAT);
		}

		try {
			int version = versionElement.getAsInt();

			if(version != SketchMap.VERSION) {
				throw createException(WRONG_VERSION);
			}
		} catch (NumberFormatException e) {
			throw createException(WRONG_VERSION_FORMAT);
		}
	}

	private void parseMapSize(JsonObject jsonObject) throws IOException {
		width = getSize(jsonObject, WIDTH);
		height = getSize(jsonObject, HEIGHT);

		if(width < 1 || height < 1) {
			throw createException(INVALID_MAP_SIZE);
		}
	}

	private int getSize(JsonObject jsonObject, String property) throws IOException {
		if(!jsonObject.has(property)) {
			throw createException(NO_SIZE);
		}

		try {
			return jsonObject.get(property).getAsInt();
		} catch (NumberFormatException e) {
			throw createException(WRONG_SIZE_FORMAT);
		}
	}

	private void parseUsedTerrainTypes(JsonObject jsonObject) throws IOException {
		idToTerrainTypeMap.clear();

		if(!jsonObject.has(USED_TERRAIN_TYPES)) {
			throw createException(NO_USED_TERRAIN_TYPES);
		}

		JsonElement usedTerrainTypesElement = jsonObject.get(USED_TERRAIN_TYPES);

		if(!usedTerrainTypesElement.isJsonArray()) {
			throw createException(WRONG_USED_TERRAIN_TYPES_FORMAT);
		}

		JsonArray jsonArray = usedTerrainTypesElement.getAsJsonArray();

		for(JsonElement element : jsonArray) {
			String name = element.getAsString();
			TerrainType type = terrainTypeManager.getOrDefault(name);

			if(type.isDefault()) {
				log.warn("parseUsedTerrainTypes(): Terrain type {} does not exist.", name);
			}

			idToTerrainTypeMap.put(idToTerrainTypeMap.size(), type);
		}

		if(idToTerrainTypeMap.size() == 0) {
			throw createException(NO_USED_TERRAIN_TYPES);
		}

		log.info("parseUsedTerrainTypes(): Contains {} terrain types.", idToTerrainTypeMap.size());
	}

	private ArrayCellMap2D<SketchCell> parseTerrainTypeMap(JsonObject jsonObject) throws IOException {
		if(!jsonObject.has(TERRAIN_TYPE_MAP)) {
			throw createException(NO_TERRAIN_TYPE_MAP);
		}

		JsonElement usedTerrainTypesElement = jsonObject.get(TERRAIN_TYPE_MAP);

		if(!usedTerrainTypesElement.isJsonArray()) {
			throw createException(WRONG_TERRAIN_TYPE_MAP_FORMAT);
		}

		JsonArray jsonArray = usedTerrainTypesElement.getAsJsonArray();

		if(jsonArray.size() != height) {
			throw createException(WRONG_TERRAIN_TYPE_MAP_SIZE);
		}

		int size = width * height;
		SketchCell[] cellArray = new SketchCell[size];
		ArrayCellMap2D<SketchCell> cellMap = new ArrayCellMap2D<>(width, height, cellArray);

		for(int y = 0; y < height; y++) {
			parseTerrainTypeRow(jsonArray, cellArray, cellMap, y);
		}

		return cellMap;
	}

	private void parseTerrainTypeRow(JsonArray jsonArray, SketchCell[] cellArray, ArrayCellMap2D<SketchCell> cellMap, int y) throws IOException {
		JsonElement element = jsonArray.get(y);
		String rowString = element.getAsString();
		String[] rowParts = rowString.split(TERRAIN_TYPE_ROW_SEPARATOR);

		if(rowParts.length != width) {
			throw createException(WRONG_TERRAIN_TYPE_MAP_SIZE);
		}

		for(int x = 0; x < width; x++) {
			try {
				parseTerrainTypeCell(cellArray, cellMap.getIndex(x, y), rowParts[x]);
			}
			catch (NumberFormatException e) {
				throw createException(WRONG_TERRAIN_TYPE_ROW_FORMAT);
			}
		}
	}

	private void parseTerrainTypeCell(SketchCell[] cellArray, int index, String rowPart) throws IOException {
		int terrainTypeId = Integer.parseInt(rowPart);

		TerrainType type = idToTerrainTypeMap.get(terrainTypeId);

		if(type == null) {
			throw createException(NOT_USED_TERRAIN_TYPE);
		}

		cellArray[index] = new SketchCell(type, DEFAULT_ELEVATION);
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

	protected String convertToJson(SketchMap map) throws IOException {
		CellMap2d<SketchCell> cells = map.getCellMap();

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(VERSION, SketchMap.VERSION);
		jsonObject.addProperty(WIDTH, cells.getWidth());
		jsonObject.addProperty(HEIGHT, cells.getHeight());

		saveUsedTerrainTypes(jsonObject, cells);
		saveTerrainTypeMap(jsonObject, cells);

		return gson.toJson(jsonObject);
	}

	private void saveUsedTerrainTypes(JsonObject jsonObject, CellMap2d<SketchCell> cells) {
		terrainTypeToIdMap.clear();

		JsonArray jsonArray = new JsonArray();

		for (SketchCell cell : cells.getCells()) {
			TerrainType type = cell.getTerrainType();

			if(!terrainTypeToIdMap.containsKey(type)) {
				int id  = terrainTypeToIdMap.size();
				log.info("saveUsedTerrainTypes(): type={} id={}", type.getName(), id);
				terrainTypeToIdMap.put(type, id);

				jsonArray.add(type.getName());
			}
		}

		log.info("saveUsedTerrainTypes(): Contains {} terrain types.", terrainTypeToIdMap.size());

		jsonObject.add(USED_TERRAIN_TYPES, jsonArray);
	}

	private void saveTerrainTypeMap(JsonObject jsonObject, CellMap2d<SketchCell> cells) {
		JsonArray rowsArray = new JsonArray();

		for(int y = 0; y < cells.getHeight(); y++) {
			StringBuilder row = new StringBuilder();

			for(int x = 0; x < cells.getWidth(); x++) {
				SketchCell cell = cells.getCell(x, y);

				row.append(terrainTypeToIdMap.getOrDefault(cell.getTerrainType(), -1));

				if(x < cells.getWidth()-1) {
					row.append(TERRAIN_TYPE_ROW_SEPARATOR);
				}
			}

			rowsArray.add(row.toString());
		}

		jsonObject.add(TERRAIN_TYPE_MAP, rowsArray);
	}
}
