package jfws.generation.map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerrainTypeTest extends SharedTestData {

	// isDefault()

	@Test
	void testIsDefault() {
		assertTrue(TerrainType.DEFAULT_TYPE.isDefault());

		assertFalse(TERRAIN_TYPE_A.isDefault());
		assertFalse(TERRAIN_TYPE_B.isDefault());
		assertFalse(TERRAIN_TYPE_C.isDefault());
	}

}