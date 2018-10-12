package jfws.generation.region;

import jfws.util.map.OutsideMapException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static jfws.generation.region.terrain.SharedTestData.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class RegionMapTest {

	public static final int WIDTH = 4;
	public static final int HEIGHT = 5;
	public static final int SIZE = WIDTH * HEIGHT;

	private RegionMap regionMap;

	@BeforeEach
	void setup() {
		regionMap = RegionMap.create(WIDTH, HEIGHT, TERRAIN_TYPE_A);
	}

	@Test
	void testCreate() throws OutsideMapException {
		assertThat(regionMap, is(notNullValue()));
		assertThat(regionMap.getWidth(), is(equalTo(WIDTH)));
		assertThat(regionMap.getHeight(), is(equalTo(HEIGHT)));

		for(int i = 0; i < SIZE; i++) {
			assertThat(regionMap.getCell(i), is(sameInstance(TERRAIN_TYPE_A)));
		}
	}
}