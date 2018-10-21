package jfws.generation.region;

import jfws.util.map.ArrayMap2d;
import jfws.util.map.OutsideMapException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static jfws.generation.region.terrain.SharedTestData.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class AbstractRegionMapTest {

	public static final int WIDTH = 4;
	public static final int HEIGHT = 5;
	public static final int SIZE = WIDTH * HEIGHT;

	private AbstractRegionMap abstractRegionMap;

	@BeforeEach
	void setup() {
		abstractRegionMap = new AbstractRegionMap(WIDTH, HEIGHT, TERRAIN_TYPE_A);
	}

	@Test
	void testCreate() throws OutsideMapException {
		ArrayMap2d<AbstractRegionCell> cells = abstractRegionMap.getCells();
		assertThat(cells, is(notNullValue()));
		assertThat(cells.getWidth(), is(equalTo(WIDTH)));
		assertThat(cells.getHeight(), is(equalTo(HEIGHT)));

		for(int i = 0; i < SIZE; i++) {
			assertThat(cells.getCell(i).getTerrainType(), is(sameInstance(TERRAIN_TYPE_A)));
		}
	}
}