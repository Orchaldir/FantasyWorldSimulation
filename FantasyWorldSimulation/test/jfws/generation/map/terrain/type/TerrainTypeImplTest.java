package jfws.generation.map.terrain.type;

import org.junit.jupiter.api.Test;

import java.awt.*;

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