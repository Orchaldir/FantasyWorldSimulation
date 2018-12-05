package jfws.maps.sketch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static jfws.features.elevation.ElevationCell.DEFAULT_ELEVATION;
import static jfws.maps.sketch.terrain.SharedTestData.TERRAIN_TYPE_A;
import static jfws.maps.sketch.terrain.SharedTestData.TERRAIN_TYPE_B;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class ChangeTerrainTypeCommandTest extends SharedData{

	public static final int INDEX = 1;
	private ChangeTerrainTypeCommand command;

	@BeforeEach
	public void setUp() {
		sketchMap = new SketchMap(WIDTH, HEIGHT, TERRAIN_TYPE_A);
		command = new ChangeTerrainTypeCommand(sketchMap, INDEX, TERRAIN_TYPE_B);
	}

	@Test
	public void testGetName() {
		assertThat(command.getName(), is(equalTo(ChangeTerrainTypeCommand.NAME)));
	}

	@Test
	public void testExecute() {
		command.execute();

		assertCell(0, TERRAIN_TYPE_A, DEFAULT_ELEVATION);
		assertCell(INDEX, TERRAIN_TYPE_B, DEFAULT_ELEVATION);
	}

	@Test
	public void testExecuteOutsideMap() {
		command = new ChangeTerrainTypeCommand(sketchMap, 100, TERRAIN_TYPE_B);
		command.execute();

		assertCells(TERRAIN_TYPE_A, DEFAULT_ELEVATION);
	}

	@Test
	public void testUnExecute() {
		command.execute();
		command.unExecute();

		assertCells(TERRAIN_TYPE_A, DEFAULT_ELEVATION);
	}

	@Test
	public void testUnExecuteWithoutExecute() {
		assertThrows(IllegalStateException.class, () -> command.unExecute());

		assertCells(TERRAIN_TYPE_A, DEFAULT_ELEVATION);
	}

}