package jfws.maps.sketch.terrain;

import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
	public void test() {
		String json = converter.save(Arrays.asList(TERRAIN_TYPE_A, TERRAIN_TYPE_B, TERRAIN_TYPE_C));
		List<TerrainType> types = converter.load(json);

		assertThat(types, is(notNullValue()));
		assertThat(types.size(), is(equalTo(3)));

		assertThat(types, containsInAnyOrder(
				getTerrainTypeMatcher(TERRAIN_TYPE_A),
				getTerrainTypeMatcher(TERRAIN_TYPE_B),
				getTerrainTypeMatcher(TERRAIN_TYPE_C)));
	}

	@Test
	public void testTerrainType() {
		String json = converter.saveTerrainType(TERRAIN_TYPE_A);
		Optional<TerrainType> optionalType = converter.loadTerrainType(json);

		assertTerrainType(optionalType, NAME_A, TERRAIN_TYPE_A.getColor(),
				TERRAIN_TYPE_A.getBaseElevation(), TERRAIN_TYPE_A.getElevationVariation());

		assertThat(optionalType.get(), is(not(TERRAIN_TYPE_A)));
	}

	// load()

	@Test
	public void testLoadEmptyString() {
		List<TerrainType> types = converter.load("");

		assertTrue(types.isEmpty());
	}

	@Test
	public void testLoadWrongFormat() {
		List<TerrainType> types = converter.load("not json!");

		assertTrue(types.isEmpty());
	}

	@Test
	public void testLoadNull() {
		assertThrows(NullPointerException.class, () -> converter.load(null));
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
		Optional<TerrainType> optionalType = converter.loadTerrainType("{\"color\": {\"red\": 255,\"green\": 0,\"blue\": 0}}");

		assertFalse(optionalType.isPresent());
	}

	@Test
	public void testLoadTerrainTypeWithoutColor() {
		Optional<TerrainType> optionalType = converter.loadTerrainType("{\"name\":\"B\"}");

		assertTerrainType(optionalType, NAME_B, NullTerrainType.DEFAULT_COLOR, 0, 0);
	}

	@Test
	public void testLoadTerrainTypeNull() {
		assertThrows(NullPointerException.class, () -> converter.loadTerrainType(null));
	}

}