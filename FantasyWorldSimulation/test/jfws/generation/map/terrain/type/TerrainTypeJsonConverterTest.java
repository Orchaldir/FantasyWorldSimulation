package jfws.generation.map.terrain.type;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class TerrainTypeJsonConverterTest extends SharedTestData {

	private TerrainTypeJsonConverter converter;

	@BeforeEach
	void setup() {
		converter = new TerrainTypeJsonConverter();
	}

	@Test
	void test() {
		String json = converter.write(TERRAIN_TYPE_A);
		Optional<TerrainType> optionalType = converter.load(json);

		assertTrue(optionalType.isPresent());

		TerrainType type = optionalType.get();

		assertThat(type, is(notNullValue()));
		assertThat(type, is(not(TERRAIN_TYPE_A)));

		assertThat(type.getName(), is(equalTo(NAME_A)));

		assertThat(type.getColor(), is(notNullValue()));
		assertThat(type.getColor(), is(equalTo(TERRAIN_TYPE_A.getColor())));
	}

	// load()

	@Test
	void testLoadEmptyString() {
		Optional<TerrainType> optionalType = converter.load("");

		assertFalse(optionalType.isPresent());
	}

	@Test
	void testLoadWrongFormat() {
		Optional<TerrainType> optionalType = converter.load("not json!");

		assertFalse(optionalType.isPresent());
	}

	@Test
	void testLoadNull() {
		assertThrows(NullPointerException.class, () -> converter.load(null));
	}

}