package jfws.generation.region;

import jfws.generation.region.terrain.TerrainType;
import jfws.util.map.Map2d;
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

	protected AbstractRegionMap abstractRegionMap;

	void assertCell(int index, TerrainType type, double elevation) throws OutsideMapException {
		AbstractRegionCell cell = abstractRegionMap.getCells().getCell(index);
		assertThat(cell.getTerrainType(), is(sameInstance(type)));
		assertThat(cell.getElevation(), is(equalTo(elevation)));
	}

	@BeforeEach
	void setup() {
		abstractRegionMap = new AbstractRegionMap(WIDTH, HEIGHT, TERRAIN_TYPE_A);
	}

	@Test
	void testCreate() throws OutsideMapException {
		Map2d<AbstractRegionCell> cells = abstractRegionMap.getCells();
		assertThat(cells, is(notNullValue()));
		assertThat(cells.getWidth(), is(equalTo(WIDTH)));
		assertThat(cells.getHeight(), is(equalTo(HEIGHT)));

		for(int i = 0; i < SIZE; i++) {
			assertCell(i, TERRAIN_TYPE_A, AbstractRegionCell.DEFAULT_ELEVATION);
		}
	}
}