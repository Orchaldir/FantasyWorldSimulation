package jfws.generation.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

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
	void testGetUnknownTerrainTypes() {
		assertThat(manager.getOrDefault(NAME_A), is(equalTo(TerrainType.DEFAULT_TYPE)));
		assertThat(manager.getOrDefault(NAME_B), is(equalTo(TerrainType.DEFAULT_TYPE)));
		assertThat(manager.getOrDefault(NAME_C), is(equalTo(TerrainType.DEFAULT_TYPE)));
	}

}