package jfws.maps.sketch.terrain;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static jfws.maps.sketch.terrain.TerrainType.NO_GROUP;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class TerrainTypeConverterWithJsonTest extends SharedTestData {

	private TerrainTypeConverterWithJson converter;

	@BeforeEach
	void setUp() {
		converter = new TerrainTypeConverterWithJson();
	}

	private void assertTerrainType(Optional<TerrainType> optionalType, String name, Color color,
								   double baseElevation, double elevationVariation) {
		assertTrue(optionalType.isPresent());

		TerrainType type = optionalType.get();

		assertThat(type, is(notNullValue()));
		assertThat(type, is(not(TERRAIN_TYPE_A)));

		assertThat(type.getName(), is(equalTo(name)));

		assertThat(type.getColor(), is(notNullValue()));
		assertThat(type.getColor(), is(equalTo(color)));

		assertThat(type.getBaseElevation(), is(equalTo(baseElevation)));
		assertThat(type.getElevationVariation(), is(equalTo(elevationVariation)));
	}

	@Test
	void test() {
		String json = converter.save(Arrays.asList(TERRAIN_TYPE_A, TERRAIN_TYPE_B, TERRAIN_TYPE_C));
		List<TerrainType> types = converter.load(json);

		assertThat(types, is(notNullValue()));
		assertThat(types.size(), is(equalTo(3)));

		assertThat(types, containsInAnyOrder(
				allOf(
					hasProperty("name", is(NAME_A)),
					hasProperty("group", is(NO_GROUP)),
					hasProperty("color", is(equalTo(TERRAIN_TYPE_A.getColor()))),
					hasProperty("baseElevation", is(equalTo(TERRAIN_TYPE_A.getBaseElevation()))),
					hasProperty("elevationVariation", is(equalTo(TERRAIN_TYPE_A.getElevationVariation())))
				),
				allOf(
					hasProperty("name", is(NAME_B)),
					hasProperty("group", is(NO_GROUP)),
					hasProperty("color", is(equalTo(TERRAIN_TYPE_B.getColor()))),
					hasProperty("baseElevation", is(equalTo(TERRAIN_TYPE_B.getBaseElevation()))),
					hasProperty("elevationVariation", is(equalTo(TERRAIN_TYPE_B.getElevationVariation())))
				),
				allOf(
					hasProperty("name", is(NAME_C)),
					hasProperty("group", is(GROUP)),
					hasProperty("color", is(equalTo(TERRAIN_TYPE_C.getColor()))),
					hasProperty("baseElevation", is(equalTo(TERRAIN_TYPE_C.getBaseElevation()))),
					hasProperty("elevationVariation", is(equalTo(TERRAIN_TYPE_C.getElevationVariation())))
				)));
	}

	@Test
	void testTerrainType() {
		String json = converter.saveTerrainType(TERRAIN_TYPE_A);
		Optional<TerrainType> optionalType = converter.loadTerrainType(json);

		assertTerrainType(optionalType, NAME_A, TERRAIN_TYPE_A.getColor(),
				TERRAIN_TYPE_A.getBaseElevation(), TERRAIN_TYPE_A.getElevationVariation());

		assertThat(optionalType.get(), is(not(TERRAIN_TYPE_A)));
	}

	// load()

	@Test
	void testLoadEmptyString() {
		List<TerrainType> types = converter.load("");

		assertTrue(types.isEmpty());
	}

	@Test
	void testLoadWrongFormat() {
		List<TerrainType> types = converter.load("not json!");

		assertTrue(types.isEmpty());
	}

	@Test
	void testLoadNull() {
		assertThrows(NullPointerException.class, () -> converter.load(null));
	}

	// loadTerrainType()

	@Test
	void testLoadTerrainTypeEmptyString() {
		Optional<TerrainType> optionalType = converter.loadTerrainType("");

		assertFalse(optionalType.isPresent());
	}

	@Test
	void testLoadTerrainTypeWrongFormat() {
		Optional<TerrainType> optionalType = converter.loadTerrainType("not json!");

		assertFalse(optionalType.isPresent());
	}

	@Test
	void testLoadTerrainTypeWithoutName() {
		Optional<TerrainType> optionalType = converter.loadTerrainType("{\"color\": {\"red\": 255,\"green\": 0,\"blue\": 0}}");

		assertFalse(optionalType.isPresent());
	}

	@Test
	void testLoadTerrainTypeWithoutColor() {
		Optional<TerrainType> optionalType = converter.loadTerrainType("{\"name\":\"B\"}");

		assertTerrainType(optionalType, NAME_B, NullTerrainType.DEFAULT_COLOR, 0, 0);
	}

	@Test
	void testLoadTerrainTypeNull() {
		assertThrows(NullPointerException.class, () -> converter.loadTerrainType(null));
	}

}