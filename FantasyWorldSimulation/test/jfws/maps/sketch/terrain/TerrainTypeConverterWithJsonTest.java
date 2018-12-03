package jfws.maps.sketch.terrain;

import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static jfws.maps.sketch.terrain.NullTerrainType.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class TerrainTypeConverterWithJsonTest extends SharedTestData {

	private TerrainTypeConverterWithJson converter;

	@BeforeEach
	public void setUp() {
		converter = new TerrainTypeConverterWithJson();
	}

	private void assertTerrainType(Optional<TerrainType> optionalType, String name, String group, Color color,
								   double baseElevation, double elevationVariation) {
		assertTrue(optionalType.isPresent());

		TerrainType type = optionalType.get();

		assertThat(type, is(notNullValue()));

		assertThat(type.getName(), is(equalTo(name)));
		assertThat(type.getGroup(), is(equalTo(group)));

		assertThat(type.getColor(), is(notNullValue()));
		assertThat(type.getColor(), is(equalTo(color)));

		assertThat(type.getBaseElevation(), is(equalTo(baseElevation)));
		assertThat(type.getElevationVariation(), is(equalTo(elevationVariation)));
	}

	private void assertTerrainType(Optional<TerrainType> optionalType, TerrainType type) {
		assertTerrainType(optionalType, type.getName(), type.getGroup(), type.getColor(),
				type.getBaseElevation(), type.getElevationVariation());
		assertThat(optionalType.get(), is(not(type)));
	}

	private Matcher<TerrainType> getTerrainTypeMatcher(TerrainType type) {
		return allOf(
				hasProperty("name", is(equalTo(type.getName()))),
				hasProperty("group", is(equalTo(type.getGroup()))),
				hasProperty("color", is(equalTo(type.getColor()))),
				hasProperty("baseElevation", is(equalTo(type.getBaseElevation()))),
				hasProperty("elevationVariation", is(equalTo(type.getElevationVariation())))
		);
	}

	@Test
	public void test() throws IOException {
		String json = converter.save(Arrays.asList(TERRAIN_TYPE_A, TERRAIN_TYPE_B, TERRAIN_TYPE_C));
		Map<String,TerrainType> types = converter.load(json);

		assertThat(types, is(notNullValue()));
		assertThat(types.size(), is(equalTo(3)));

		assertThat(types, hasEntry(equalTo(NAME_A), getTerrainTypeMatcher(TERRAIN_TYPE_A)));
		assertThat(types, hasEntry(equalTo(NAME_B), getTerrainTypeMatcher(TERRAIN_TYPE_B)));
		assertThat(types, hasEntry(equalTo(NAME_C), getTerrainTypeMatcher(TERRAIN_TYPE_C)));

		assertThat(types.get(NAME_A).getNoiseAmplitude(HILL_NOISE_INDEX), is(equalTo(HILL_NOISE)));
	}

	@Test
	public void testTerrainType() {
		String json = converter.saveTerrainType(TERRAIN_TYPE_A);
		Optional<TerrainType> optionalType = converter.loadTerrainType(json);

		assertTerrainType(optionalType, TERRAIN_TYPE_A);
	}

	// load()

	@Test
	public void testLoadEmptyString() throws IOException {
		Map<String,TerrainType> types = converter.load("");

		assertTrue(types.isEmpty());
	}

	@Test
	public void testLoadWrongFormat() throws IOException {
		Map<String,TerrainType> types = converter.load("not json!");

		assertTrue(types.isEmpty());
	}

	@Test
	public void testLoadNull() {
		assertThrows(NullPointerException.class, () -> converter.load(null));
	}

	@Test
	public void testLoadWithDuplicateName() {
		assertThrows(IOException.class, () -> converter.load("[{\"name\":\"A\", \"group\":\"second\"},{\"name\":\"A\", \"group\":\"G1\"}]"));
	}

	// loadTerrainType()

	@Test
	public void testLoadTerrainTypeEmptyString() {
		Optional<TerrainType> optionalType = converter.loadTerrainType("");

		assertFalse(optionalType.isPresent());
	}

	@Test
	public void testLoadTerrainTypeWrongFormat() {
		Optional<TerrainType> optionalType = converter.loadTerrainType("not json!");

		assertFalse(optionalType.isPresent());
	}

	@Test
	public void testLoadTerrainTypeWithoutName() {
		Optional<TerrainType> optionalType = converter.loadTerrainType("{\"color\": {\"red\": 1.0,\"green\": 0,\"blue\": 0}}");

		assertFalse(optionalType.isPresent());
	}

	@Test
	public void testLoadTerrainTypeWithoutGroup() {
		Optional<TerrainType> optionalType = converter.loadTerrainType("{\"name\":\"B\", \"color\": {\"red\": 1.0,\"green\": 0,\"blue\": 0}, \"base_elevation\":111.1, \"elevation_variation\":2.5}");

		assertTerrainType(optionalType, NAME_B, NO_GROUP, Color.RED,
				BASE_ELEVATION, ELEVATION_VARIATION);
	}

	@Test
	public void testLoadTerrainTypeWithoutColor() {
		Optional<TerrainType> optionalType = converter.loadTerrainType("{\"name\":\"B\", \"group\":\"second\", \"base_elevation\":111.1, \"elevation_variation\":2.5}");

		assertTerrainType(optionalType, NAME_B, GROUP0, DEFAULT_COLOR, BASE_ELEVATION, ELEVATION_VARIATION);
	}

	@Test
	public void testLoadTerrainTypeWithNegativeColorValue() {
		Optional<TerrainType> optionalType = converter.loadTerrainType("{\"name\":\"B\", \"group\":\"second\", \"color\": {\"red\": -0.5,\"green\": 0,\"blue\": 0}, \"base_elevation\":111.1, \"elevation_variation\":2.5}");

		assertTerrainType(optionalType, NAME_B, GROUP0, DEFAULT_COLOR, BASE_ELEVATION, ELEVATION_VARIATION);
	}

	@Test
	public void testLoadTerrainTypeWithTooHighColorValue() {
		Optional<TerrainType> optionalType = converter.loadTerrainType("{\"name\":\"B\", \"group\":\"second\", \"color\": {\"red\": 255,\"green\": 0,\"blue\": 0}, \"base_elevation\":111.1, \"elevation_variation\":2.5}");

		assertTerrainType(optionalType, NAME_B, GROUP0, DEFAULT_COLOR, BASE_ELEVATION, ELEVATION_VARIATION);
	}

	@Test
	public void testLoadTerrainTypeWithoutBaseElevation() {
		Optional<TerrainType> optionalType = converter.loadTerrainType("{\"name\":\"B\", \"group\":\"second\", \"color\": {\"red\": 1.0,\"green\": 0,\"blue\": 0}, \"elevation_variation\":2.5}");

		assertTerrainType(optionalType, NAME_B, GROUP0, Color.RED, DEFAULT_BASE_ELEVATION, ELEVATION_VARIATION);
	}

	@Test
	public void testLoadTerrainTypeWithoutElevationVariation() {
		Optional<TerrainType> optionalType = converter.loadTerrainType("{\"name\":\"B\", \"group\":\"second\", \"color\": {\"red\": 1.0,\"green\": 0,\"blue\": 0}, \"base_elevation\":111.1}");

		assertTerrainType(optionalType, NAME_B, GROUP0, Color.RED, BASE_ELEVATION, DEFAULT_ELEVATION_VARIATION);
	}

	@Test
	public void testLoadTerrainTypeNull() {
		assertThrows(NullPointerException.class, () -> converter.loadTerrainType(null));
	}

}