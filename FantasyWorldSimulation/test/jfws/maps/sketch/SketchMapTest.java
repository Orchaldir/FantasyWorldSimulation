package jfws.maps.sketch;

import jfws.features.elevation.ElevationCell;
import jfws.maps.sketch.elevation.ElevationGenerator;
import jfws.util.map.CellMap2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static jfws.maps.sketch.terrain.SharedTestData.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

class SketchMapTest extends SharedData {

	protected ElevationGenerator elevationGenerator;

	@BeforeEach
	public void setUp() {
		elevationGenerator = mock(ElevationGenerator.class);
		sketchMap = new SketchMap(WIDTH, HEIGHT, TERRAIN_TYPE_A);
	}

	@Test
	public void testGetParentMap() {
		assertThat(sketchMap.getParentMap().isPresent(), is(false));
	}

	@Test
	public void testCreate() {
		CellMap2d<SketchCell> cells = sketchMap.getCellMap();
		assertThat(cells, is(notNullValue()));
		assertThat(cells.getWidth(), is(equalTo(WIDTH)));
		assertThat(cells.getHeight(), is(equalTo(HEIGHT)));

		assertCells(TERRAIN_TYPE_A, ElevationCell.DEFAULT_ELEVATION);
	}

	@Test
	public void testGenerateElevation() {
		sketchMap.getCellMap().getCell(0).setTerrainType(TERRAIN_TYPE_B);
		sketchMap.getCellMap().getCell(1).setTerrainType(TERRAIN_TYPE_C);

		when(elevationGenerator.generate(TERRAIN_TYPE_A)).thenReturn(0.0);
		when(elevationGenerator.generate(TERRAIN_TYPE_B)).thenReturn(100.0);
		when(elevationGenerator.generate(TERRAIN_TYPE_C)).thenReturn(200.0);

		InOrder orderVerifier = inOrder(elevationGenerator);

		sketchMap.generateElevation(elevationGenerator);

		orderVerifier.verify(elevationGenerator).prepare();
		orderVerifier.verify(elevationGenerator).generate(TERRAIN_TYPE_B);
		orderVerifier.verify(elevationGenerator).generate(TERRAIN_TYPE_C);

		assertCell(0, TERRAIN_TYPE_B, 100.0);
		assertCell(1, TERRAIN_TYPE_C, 200.0);
	}
}