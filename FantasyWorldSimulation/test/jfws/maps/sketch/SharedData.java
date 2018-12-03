package jfws.maps.sketch;

import jfws.maps.sketch.terrain.TerrainType;
import jfws.util.map.OutsideMapException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SharedData {

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
}
