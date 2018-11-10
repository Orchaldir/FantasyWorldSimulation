package jfws.maps.sketch.terrain;

import com.google.gson.*;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static jfws.maps.sketch.terrain.TerrainType.NO_GROUP;

@Slf4j
public class TerrainTypeConverterWithJson implements TerrainTypeConverter {

	private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
	private final JsonParser parser = new JsonParser();

	// properties
	private final static String NAME = "name";
	private final static String GROUP = "group";
	private final static String COLOR = "color";
	private final static String RED = "red";
	private final static String GREEN = "green";
	private final static String BLUE = "blue";
	private final static String BASE_ELEVATION = "base_elevation";
	private final static String ELEVATION_VARIATION = "elevation_variation";

	@Override
	public String save(Collection<TerrainType> types) {
		JsonArray jsonArray = new JsonArray();

		for(TerrainType type : types) {
			jsonArray.add(getJsonObject(type));
		}

		return gson.toJson(jsonArray);
	}

	@Override
	public String saveTerrainType(TerrainType type) {
		return gson.toJson(getJsonObject(type));
	}

	private JsonObject getJsonObject(TerrainType type) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(NAME, type.getName());

		saveGroup(jsonObject, type);
		saveColor(jsonObject, type);

		jsonObject.addProperty(BASE_ELEVATION, type.getBaseElevation());
		jsonObject.addProperty(ELEVATION_VARIATION, type.getElevationVariation());

		return jsonObject;
	}

	private void saveGroup(JsonObject jsonObject, TerrainType type) {
		if(!type.getGroup().isEmpty()) {
			jsonObject.addProperty(GROUP, type.getGroup());
		}
	}

	private void saveColor(JsonObject jsonObject, TerrainType type) {
		JsonObject jsonColor = new JsonObject();
		jsonColor.addProperty(RED, type.getColor().getRed());
		jsonColor.addProperty(GREEN, type.getColor().getGreen());
		jsonColor.addProperty(BLUE, type.getColor().getBlue());

		jsonObject.add(COLOR, jsonColor);
	}

	@Override
	public List<TerrainType> load(String text) {
		List<TerrainType> types = new ArrayList<>();

		try {
			JsonElement element = parser.parse(text);

			if (element.isJsonArray()) {
				JsonArray jsonArray = element.getAsJsonArray();

				for(int i = 0; i < jsonArray.size(); i++) {
					loadTerrainType(jsonArray.get(i)).ifPresent(type -> types.add(type));
				}
			}
			else {
				log.warn("load(): Does not contain a JsonArray!");
			}
		} catch(JsonParseException e) {
			log.warn("load(): JsonParseException: {}", e.getMessage());
		}

		return types;
	}

	@Override
	public Optional<TerrainType> loadTerrainType(String text) {
		try {
			return loadTerrainType(parser.parse(text));
		} catch(JsonParseException e) {
			log.warn("loadTerrainType(): JsonParseException: {}", e.getMessage());
		}

		return Optional.empty();
	}

	private Optional<TerrainType> loadTerrainType(JsonElement element) {
		if (element.isJsonObject()) {
			JsonObject jsonObject = element.getAsJsonObject();

			if(hasRequiredFields(jsonObject)) {
				String name = jsonObject.get(NAME).getAsString();
				String group = loadGroup(jsonObject);
				Color color = loadColor(jsonObject, name);
				double baseElevation = loadBaseElevation(jsonObject, name);
				double elevationVariation = loadElevationVariation(jsonObject, name);

				return Optional.of(new TerrainTypeImpl(name, group, color, baseElevation, elevationVariation));
			}
			else {
				log.warn("loadTerrainType(): JsonObject has no name!");
			}
		}
		else {
			log.warn("loadTerrainType(): JsonElement is not a JsonObject!");
		}

		return Optional.empty();
	}

	private boolean hasRequiredFields(JsonObject jsonObject) {
		return jsonObject.has(NAME);
	}

	private String loadGroup(JsonObject jsonObject) {
		if (jsonObject.has(GROUP)) {
			return  jsonObject.get(GROUP).getAsString();
		}

		return NO_GROUP;
	}

	private Color loadColor(JsonObject jsonObject, String name) {
		JsonElement colorElement = jsonObject.get(COLOR);

		if (isObject(colorElement)) {
			JsonObject colorObject = colorElement.getAsJsonObject();

			double red = readColorValue(colorObject, RED);
			double green = readColorValue(colorObject, GREEN);
			double blue = readColorValue(colorObject, BLUE);

			return new Color(red, green, blue, 1.0);
		}

		log.warn("loadColor(): TerrainType {} has no color!", name);

		return NullTerrainType.DEFAULT_COLOR;
	}

	private double loadBaseElevation(JsonObject jsonObject, String name) {
		JsonElement elevationElement = jsonObject.get(BASE_ELEVATION);

		if (iDouble(elevationElement)) {
			return elevationElement.getAsDouble();
		}

		log.warn("loadBaseElevation(): TerrainType {} has no base elevation!", name);

		return NullTerrainType.DEFAULT_BASE_ELEVATION;
	}

	private double loadElevationVariation(JsonObject jsonObject, String name) {
		JsonElement elevationElement = jsonObject.get(ELEVATION_VARIATION);

		if (iDouble(elevationElement)) {
			return elevationElement.getAsDouble();
		}

		log.warn("loadElevationVariation(): TerrainType {} has no elevation variation!", name);

		return NullTerrainType.DEFAULT_ELEVATION_VARIATION;
	}

	private boolean isObject(JsonElement colorElement) {
		return colorElement != null && colorElement.isJsonObject();
	}

	private boolean iDouble(JsonElement colorElement) {
		return colorElement != null && colorElement.isJsonPrimitive();
	}

	private double readColorValue(JsonObject jsonObject, String name) {
		JsonElement element = jsonObject.get(name);

		if (element != null) {
			return element.getAsDouble();
		}

		return 0;
	}
}
