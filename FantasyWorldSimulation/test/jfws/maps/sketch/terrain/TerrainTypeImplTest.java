package jfws.maps.sketch.terrain;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class TerrainTypeImplTest extends SharedTestData {

	// isDefault()

	@Test
	public void testIsDefault() {
		assertFalse(TERRAIN_TYPE_A.isDefault());
		assertFalse(TERRAIN_TYPE_B.isDefault());
		assertFalse(TERRAIN_TYPE_C.isDefault());
	}


	@Test
	public void testToString() {
		assertThat(TERRAIN_TYPE_A.toString(), is(equalTo("TerrainTypeImpl(name=A)")));
	}
}