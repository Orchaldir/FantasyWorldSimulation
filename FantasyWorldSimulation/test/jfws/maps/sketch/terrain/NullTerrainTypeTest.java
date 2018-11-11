package jfws.maps.sketch.terrain;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class NullTerrainTypeTest {

	private static final String NAME = "name0";

	private NullTerrainType type = new NullTerrainType(NAME);

	@Test
	void testGetName() {
		assertThat(type.getName(), is(equalTo(NAME)));
	}

	@Test
	void testGetGroup() {
		assertThat(type.getGroup(), is(equalTo(NullTerrainType.DEFAULT_GROUP)));
	}

	@Test
	void testGetColor() {
		assertThat(type.getColor(), is(notNullValue()));
		assertThat(type.getColor(), is(equalTo(NullTerrainType.DEFAULT_COLOR)));
	}

	@Test
	void testGetBaseElevation() {
		assertThat(type.getBaseElevation(), is(equalTo(NullTerrainType.DEFAULT_BASE_ELEVATION)));
	}

	@Test
	void testGetElevationVariation() {
		assertThat(type.getElevationVariation(), is(equalTo(NullTerrainType.DEFAULT_ELEVATION_VARIATION)));
	}

	@Test
	void testIsDefault() {
		assertTrue(type.isDefault());
	}

	@Test
	void testToString() {
		assertThat(type.toString(), is(equalTo("NullTerrainType(name=name0)")));
	}

}