package jfws.maps.sketch;

import jfws.util.map.OutsideMapException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static jfws.features.elevation.ElevationCell.DEFAULT_ELEVATION;
import static jfws.maps.sketch.terrain.SharedTestData.TERRAIN_TYPE_A;
import static jfws.maps.sketch.terrain.SharedTestData.TERRAIN_TYPE_B;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class ChangeTerrainTypeCommandTest extends SketchMapTest {

	public static final int INDEX = 1;
	private ChangeTerrainTypeCommand command;

	@BeforeEach
	@Override
	public void setUp() {
		sketchMap = new SketchMap(WIDTH, HEIGHT, TERRAIN_TYPE_A);
		command = new ChangeTerrainTypeCommand(sketchMap, INDEX, TERRAIN_TYPE_B);
	}

	@Test
	public void testGetName() {
		assertThat(command.getName(), is(equalTo(ChangeTerrainTypeCommand.NAME)));
	}

	@Test
	public void testExecute() throws OutsideMapException {
		command.execute();

		assertCell(0, TERRAIN_TYPE_A, DEFAULT_ELEVATION);
		assertCell(INDEX, TERRAIN_TYPE_B, DEFAULT_ELEVATION);
	}

	@Test
	public void testUnExecute() throws OutsideMapException {
		command.execute();
		command.unExecute();

		assertCell(0, TERRAIN_TYPE_A, DEFAULT_ELEVATION);
		assertCell(INDEX, TERRAIN_TYPE_A, DEFAULT_ELEVATION);
	}

	@Test
	public void testUnExecuteWithoutExecute() throws OutsideMapException {
		assertThrows(IllegalStateException.class, () -> command.unExecute());

		assertCell(0, TERRAIN_TYPE_A, DEFAULT_ELEVATION);
		assertCell(INDEX, TERRAIN_TYPE_A, DEFAULT_ELEVATION);
	}

}