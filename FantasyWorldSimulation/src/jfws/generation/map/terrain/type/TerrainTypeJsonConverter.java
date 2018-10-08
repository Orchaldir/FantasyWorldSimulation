package jfws.generation.map.terrain.type;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
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
	public String write(TerrainType type) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(NAME, type.getName());

		writeColor(jsonObject, type);

		return gson.toJson(jsonObject);
	}

	private void writeColor(JsonObject jsonObject, TerrainType type) {
		JsonObject jsonColor = new JsonObject();
		jsonColor.addProperty(RED, type.getColor().getRed());
		jsonColor.addProperty(GREEN, type.getColor().getGreen());
		jsonColor.addProperty(BLUE, type.getColor().getBlue());

		jsonObject.add(COLOR, jsonColor);
	}

	@Override
	public Optional<TerrainType> load(String text) {
		log.debug("load(): text: {}", text);

		try {
			JsonElement element = parser.parse(text);

			if (element.isJsonObject()) {
				JsonObject jsonObject = element.getAsJsonObject();

				String name = jsonObject.get(NAME).getAsString();
				Color color = readColor(jsonObject, name);

				return Optional.of(new TerrainTypeImpl(name, color));
			}
		} catch(JsonParseException e) {
			log.warn("load(): JsonParseException: {}", e.getMessage());
		}

		return Optional.empty();
	}

	private Color readColor(JsonObject jsonObject, String name) {
		JsonElement colorElement = jsonObject.get(COLOR);

		if (hasColor(colorElement)) {
			JsonObject colorObject = colorElement.getAsJsonObject();

			int red = readColorValue(colorObject, RED);
			int green = readColorValue(colorObject, GREEN);
			int blue = readColorValue(colorObject, BLUE);

			return new Color(red, green, blue);
		}

		log.warn("readColor(): TerrainType {} has no color!", name);

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
