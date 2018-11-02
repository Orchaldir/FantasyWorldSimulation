package jfws.maps.sketch.terrain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerrainTypeImplTest extends SharedTestData {

	// isDefault()

	@Test
	void testIsDefault() {
		assertFalse(TERRAIN_TYPE_A.isDefault());
		assertFalse(TERRAIN_TYPE_B.isDefault());
		assertFalse(TERRAIN_TYPE_C.isDefault());
	}

}