package jfws.maps.sketch;

import jfws.features.elevation.ElevationCell;
import jfws.maps.sketch.elevation.BaseElevationGenerator;
import jfws.maps.sketch.terrain.TerrainType;
import jfws.util.map.CellMap2d;
import jfws.util.map.OutsideMapException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static jfws.maps.sketch.terrain.SharedTestData.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class SketchMapTest {

	public static final int WIDTH = 4;
	public static final int HEIGHT = 5;
	public static final int SIZE = WIDTH * HEIGHT;

	protected SketchMap sketchMap;

	protected void assertCell(int index, TerrainType type, double elevation) throws OutsideMapException {
		SketchCell cell = sketchMap.getCellMap().getCell(index);
		assertThat(cell.getTerrainType(), is(sameInstance(type)));
		assertThat(cell.getElevation(), is(equalTo(elevation)));
	}

	protected void assertCells(TerrainType type, double elevation) throws OutsideMapException {
		for(int i = 0; i < SIZE; i++) {
			assertCell(i, type, elevation);
		}
	}

	@BeforeEach
	public void setUp() {
		sketchMap = new SketchMap(WIDTH, HEIGHT, TERRAIN_TYPE_A);
	}

	@Test
	public void testGetParentMap() {
		assertThat(sketchMap.getParentMap().isPresent(), is(false));
	}

	@Test
	public void testCreate() throws OutsideMapException {
		CellMap2d<SketchCell> cells = sketchMap.getCellMap();
		assertThat(cells, is(notNullValue()));
		assertThat(cells.getWidth(), is(equalTo(WIDTH)));
		assertThat(cells.getHeight(), is(equalTo(HEIGHT)));

		assertCells(TERRAIN_TYPE_A, ElevationCell.DEFAULT_ELEVATION);
	}

	@Test
	public void testGenerateElevation() throws OutsideMapException {
		sketchMap.getCellMap().getCell(0).setTerrainType(TERRAIN_TYPE_B);
		sketchMap.getCellMap().getCell(1).setTerrainType(TERRAIN_TYPE_C);

		sketchMap.generateElevation(new BaseElevationGenerator());

		assertCell(0, TERRAIN_TYPE_B, TERRAIN_TYPE_B.getBaseElevation());
		assertCell(1, TERRAIN_TYPE_C, TERRAIN_TYPE_C.getBaseElevation());
	}
}