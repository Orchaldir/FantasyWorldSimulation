package jfws.generation.map.terrain.type;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class TerrainTypeJsonConverter implements TerrainTypeConverter {

	private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
	private final JsonParser parser = new JsonParser();

	// properties
	private final static String NAME = "name";
	private final static String COLOR = "color";
	private final static String RED = "red";
	private final static String GREEN = "green";
	private final static String BLUE = "blue";

	@Override
	public String save(List<TerrainType> types) {
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

		saveColor(jsonObject, type);

		return jsonObject;
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
				Color color = loadColor(jsonObject, name);

				return Optional.of(new TerrainTypeImpl(name, color));
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

	private Color loadColor(JsonObject jsonObject, String name) {
		JsonElement colorElement = jsonObject.get(COLOR);

		if (hasColor(colorElement)) {
			JsonObject colorObject = colorElement.getAsJsonObject();

			int red = readColorValue(colorObject, RED);
			int green = readColorValue(colorObject, GREEN);
			int blue = readColorValue(colorObject, BLUE);

			return new Color(red, green, blue);
		}

		log.warn("loadColor(): TerrainType {} has no color!", name);

		return NullTerrainType.DEFAULT_COLOR;
	}

	private boolean hasColor(JsonElement colorElement) {
		return colorElement != null && colorElement.isJsonObject();
	}

	private int readColorValue(JsonObject jsonObject, String name) {
		JsonElement element = jsonObject.get(name);

		if (element != null) {
			return element.getAsInt();
		}

		return 0;
	}
}
