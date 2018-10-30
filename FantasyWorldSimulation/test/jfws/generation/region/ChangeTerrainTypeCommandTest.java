package jfws.generation.region;

import jfws.util.map.OutsideMapException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static jfws.generation.region.AbstractRegionCell.DEFAULT_ELEVATION;
import static jfws.generation.region.terrain.SharedTestData.TERRAIN_TYPE_A;
import static jfws.generation.region.terrain.SharedTestData.TERRAIN_TYPE_B;
import static org.junit.jupiter.api.Assertions.*;

class ChangeTerrainTypeCommandTest extends AbstractRegionMapTest{

	public static final int INDEX = 1;
	private ChangeTerrainTypeCommand command;

	@BeforeEach
	void setup() {
		abstractRegionMap = new AbstractRegionMap(WIDTH, HEIGHT, TERRAIN_TYPE_A);
		command = new ChangeTerrainTypeCommand(abstractRegionMap, INDEX, TERRAIN_TYPE_B);
	}

	@Test
	void testExecute() throws OutsideMapException {
		command.execute();

		assertCell(0, TERRAIN_TYPE_A, DEFAULT_ELEVATION);
		assertCell(INDEX, TERRAIN_TYPE_B, DEFAULT_ELEVATION);
	}

	@Test
	void testUnExecute() throws OutsideMapException {
		command.execute();
		command.unExecute();

		assertCell(0, TERRAIN_TYPE_A, DEFAULT_ELEVATION);
		assertCell(INDEX, TERRAIN_TYPE_A, DEFAULT_ELEVATION);
	}

	@Test
	void testUnExecuteWithoutExecute() throws OutsideMapException {
		assertThrows(IllegalStateException.class, () -> command.unExecute());

		assertCell(0, TERRAIN_TYPE_A, DEFAULT_ELEVATION);
		assertCell(INDEX, TERRAIN_TYPE_A, DEFAULT_ELEVATION);
	}

}