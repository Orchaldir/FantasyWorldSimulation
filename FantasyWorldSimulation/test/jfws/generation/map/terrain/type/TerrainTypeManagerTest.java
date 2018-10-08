package jfws.generation.map.terrain.type;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TerrainTypeManagerTest extends SharedTestData {

	private TerrainTypeManager manager;

	@BeforeEach
	void  setup() {
		manager = new TerrainTypeManager();
	}

	// add & get

	@Test
	void testAddAndGetTerrainTypes() {
		manager.add(TERRAIN_TYPE_A);
		manager.add(TERRAIN_TYPE_B);
		manager.add(TERRAIN_TYPE_C);

		assertThat(manager.getOrDefault(NAME_A), is(equalTo(TERRAIN_TYPE_A)));
		assertThat(manager.getOrDefault(NAME_B), is(equalTo(TERRAIN_TYPE_B)));
		assertThat(manager.getOrDefault(NAME_C), is(equalTo(TERRAIN_TYPE_C)));
	}

	@Test
	void testGetUnknownTerrainType() {
		TerrainType type = manager.getOrDefault(NAME_A);

		assertThat(type, is(notNullValue()));
		assertThat(type, is(instanceOf(NullTerrainType.class)));
		assertThat(type.getName(), is(equalTo(NAME_A)));
		assertThat(type.getColor(), is(notNullValue()));
		assertThat(type.getColor(), is(equalTo(NullTerrainType.DEFAULT_COLOR)));
		assertTrue(type.isDefault());
	}

	@Test
	void testGetUnknownTerrainTypeTwice() {
		TerrainType type0 = manager.getOrDefault(NAME_A);
		TerrainType type1 = manager.getOrDefault(NAME_A);

		assertThat(type0, is(notNullValue()));
		assertThat(type0, is(sameInstance(type1)));
	}

}