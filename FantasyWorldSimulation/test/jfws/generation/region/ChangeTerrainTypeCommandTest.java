package jfws.generation.region;

import jfws.util.map.OutsideMapException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

		assertTerrainType(0, TERRAIN_TYPE_A);
		assertTerrainType(INDEX, TERRAIN_TYPE_B);
	}

	@Test
	void testUnExecute() throws OutsideMapException {
		command.execute();
		command.unExecute();

		assertTerrainType(0, TERRAIN_TYPE_A);
		assertTerrainType(INDEX, TERRAIN_TYPE_A);
	}

	@Test
	void testUnExecuteWithoutExecute() throws OutsideMapException {
		assertThrows(IllegalStateException.class, () -> command.unExecute());

		assertTerrainType(0, TERRAIN_TYPE_A);
		assertTerrainType(INDEX, TERRAIN_TYPE_A);
	}

}