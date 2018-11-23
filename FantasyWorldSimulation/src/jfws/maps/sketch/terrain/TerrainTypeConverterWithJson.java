package jfws.maps.sketch.terrain;

import com.google.gson.*;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;

import static jfws.maps.sketch.terrain.TerrainType.NO_GROUP;

@Slf4j
public class TerrainTypeConverterWithJson implements TerrainTypeConverter {

	private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
	private final JsonParser parser = new JsonParser();

	// properties
	private static final String NAME_PROPERTY = "name";
	private static final String GROUP_PROPERTY = "group";
	private static final String COLOR_PROPERTY = "color";
	private static final String RED_PROPERTY = "red";
	private static final String GREEN_PROPERTY = "green";
	private static final String BLUE_PROPERTY = "blue";
	private static final String BASE_ELEVATION_PROPERTY = "base_elevation";
	private static final String ELEVATION_VARIATION_PROPERTY = "elevation_variation";
	private static final String HILL_NOISE_PROPERTY = "hill_noise";
	private static final int HILL_NOISE_INDEX = 0;

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
		jsonObject.addProperty(NAME_PROPERTY, type.getName());

		saveGroup(jsonObject, type);
		saveColor(jsonObject, type);

		jsonObject.addProperty(BASE_ELEVATION_PROPERTY, type.getBaseElevation());
		jsonObject.addProperty(ELEVATION_VARIATION_PROPERTY, type.getElevationVariation());

		double hillNoise = type.getNoiseAmplitude(HILL_NOISE_INDEX);

		if(hillNoise > 0) {
			jsonObject.addProperty(HILL_NOISE_PROPERTY, hillNoise);
		}

		return jsonObject;
	}

	private void saveGroup(JsonObject jsonObject, TerrainType type) {
		if(!type.getGroup().isEmpty()) {
			jsonObject.addProperty(GROUP_PROPERTY, type.getGroup());
		}
	}

	private void saveColor(JsonObject jsonObject, TerrainType type) {
		JsonObject jsonColor = new JsonObject();
		jsonColor.addProperty(RED_PROPERTY, type.getColor().getRed());
		jsonColor.addProperty(GREEN_PROPERTY, type.getColor().getGreen());
		jsonColor.addProperty(BLUE_PROPERTY, type.getColor().getBlue());

		jsonObject.add(COLOR_PROPERTY, jsonColor);
	}

	@Override
	public Map<String,TerrainType> load(String text) throws IOException {
		Map<String,TerrainType> types = new HashMap<>();

		try {
			JsonElement element = parser.parse(text);

			if (element.isJsonArray()) {
				JsonArray jsonArray = element.getAsJsonArray();

				for(int i = 0; i < jsonArray.size(); i++) {
					Optional<TerrainType> optionalTerrainType = loadTerrainType(jsonArray.get(i));

					if(optionalTerrainType.isPresent()) {
						TerrainType terrainType = optionalTerrainType.get();

						if(types.containsKey(terrainType.getName())) {
							log.error("load(): Name {} exists multiple times!", terrainType.getName());
							throw new IOException(String.format("Name %s exists multiple times!", terrainType.getName()));
						}

						types.put(terrainType.getName(), terrainType);
					}
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
				String name = jsonObject.get(NAME_PROPERTY).getAsString();
				String group = loadGroup(jsonObject);
				Color color = loadColor(jsonObject, name);
				double baseElevation = loadBaseElevation(jsonObject, name);
				double elevationVariation = loadElevationVariation(jsonObject, name);
				Double[] noiseAmplitudes = loadNoiseAmplitudes(jsonObject, name);

				return Optional.of(new TerrainTypeImpl(name, group, color, baseElevation, elevationVariation, noiseAmplitudes));
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
		return jsonObject.has(NAME_PROPERTY);
	}

	private String loadGroup(JsonObject jsonObject) {
		if (jsonObject.has(GROUP_PROPERTY)) {
			return  jsonObject.get(GROUP_PROPERTY).getAsString();
		}

		return NO_GROUP;
	}

	private Color loadColor(JsonObject jsonObject, String name) {
		JsonElement colorElement = jsonObject.get(COLOR_PROPERTY);

		if (isObject(colorElement)) {
			JsonObject colorObject = colorElement.getAsJsonObject();

			double red = readColorValue(colorObject, RED_PROPERTY);
			double green = readColorValue(colorObject, GREEN_PROPERTY);
			double blue = readColorValue(colorObject, BLUE_PROPERTY);

			try {
				return new Color(red, green, blue, 1.0);
			}
			catch(IllegalArgumentException e) {
				log.warn("loadColor(): TerrainType {} has an invalid color: {}", name, e.getMessage());
				return NullTerrainType.DEFAULT_COLOR;
			}
		}

		log.warn("loadColor(): TerrainType {} has no color!", name);

		return NullTerrainType.DEFAULT_COLOR;
	}

	private double loadBaseElevation(JsonObject jsonObject, String name) {
		JsonElement elevationElement = jsonObject.get(BASE_ELEVATION_PROPERTY);

		if (iDouble(elevationElement)) {
			return elevationElement.getAsDouble();
		}

		log.warn("loadBaseElevation(): TerrainType {} has no base elevation!", name);

		return NullTerrainType.DEFAULT_BASE_ELEVATION;
	}

	private double loadElevationVariation(JsonObject jsonObject, String name) {
		JsonElement elevationElement = jsonObject.get(ELEVATION_VARIATION_PROPERTY);

		if (iDouble(elevationElement)) {
			return elevationElement.getAsDouble();
		}

		log.warn("loadElevationVariation(): TerrainType {} has no elevation variation!", name);

		return NullTerrainType.DEFAULT_ELEVATION_VARIATION;
	}

	private Double[] loadNoiseAmplitudes(JsonObject jsonObject, String name) {
		Double[] noiseAmplitudes = { NullTerrainType.DEFAULT_NOISE_AMPLITUDE };

		JsonElement hillNoiseElement = jsonObject.get(HILL_NOISE_PROPERTY);

		if (iDouble(hillNoiseElement)) {
			noiseAmplitudes[HILL_NOISE_INDEX] = hillNoiseElement.getAsDouble();
		}

		log.debug("loadNoiseAmplitudes(): TerrainType {} has no hill noise.", name);

		return noiseAmplitudes;
	}

	private boolean isObject(JsonElement colorElement) {
		return colorElement != null && colorElement.isJsonObject();
	}

	private boolean iDouble(JsonElement colorElement) {
		return colorElement != null && colorElement.isJsonPrimitive();
	}

	private double readColorValue(JsonObject jsonObject, String name) {
		return jsonObject.get(name).getAsDouble();
	}
}
