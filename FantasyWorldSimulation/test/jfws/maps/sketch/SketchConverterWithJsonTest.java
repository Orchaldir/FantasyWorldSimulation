package jfws.maps.sketch;

import jfws.maps.sketch.terrain.TerrainType;
import jfws.maps.sketch.terrain.TerrainTypeManager;
import jfws.util.map.Map2d;
import jfws.util.map.OutsideMapException;
import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;

import static jfws.maps.sketch.SketchConverterWithJson.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class SketchConverterWithJsonTest {

	private TerrainTypeManager manager;
	private SketchConverterWithJson converter;

	@BeforeEach
	void setup() {
		manager = new TerrainTypeManager(null, null);
		converter = new SketchConverterWithJson(null, manager);
	}

	private <T extends Throwable> void assertException(Class<T> expectedType, Executable executable, String message) {
		Throwable throwable = assertThrows(expectedType, executable);
		assertThat(throwable.getMessage(), is(equalTo(message)));
	}

	// load

	// save

	@Test
	void testParseStringWithNull() {
		assertThrows(IOException.class, () -> converter.parseString(null));
	}

	@Test
	void testParseEmptyString() {
		assertException(IOException.class, () -> converter.parseString(""), NOT_A_JSON_OBJECT);
	}

	@Test
	void testParseWrongString() {
		assertException(IOException.class, () -> converter.parseString("abc"), NOT_A_JSON_OBJECT);
	}

	// version

	@Test
	void testNoVersion() {
		assertException(IOException.class, () -> converter.parseString("{}"), NO_VERSION);
	}

	@Test
	void testWrongVersion() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":0}"), WRONG_VERSION);
	}

	@Test
	void testWrongVersionFormat() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":A}"), WRONG_VERSION_FORMAT);
	}

	// map size

	@Test
	void testNoWidth() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1}"), NO_SIZE);
	}

	@Test
	void testWrongWidthFormat() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":A}"), WRONG_SIZE_FORMAT);
	}

	@Test
	void tesNoHeight() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":1}"), NO_SIZE);
	}

	@Test
	void testWrongHeightFormat() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":A}"), WRONG_SIZE_FORMAT);
	}

	// used terrain types

	@Test
	void testNoUsedTerrainTypes() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3}"), NO_USED_TERRAIN_TYPES);
	}

	@Test
	void testEmptyUsedTerrainTypes() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[]}"), NO_USED_TERRAIN_TYPES);
	}

	@Test
	void testUsedTerrainTypesNotAnArray() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":3}"), WRONG_USED_TERRAIN_TYPES_FORMAT);
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":\"text\"}"), WRONG_USED_TERRAIN_TYPES_FORMAT);
	}

	// terrain type map

	@Test
	void testNoTerrainTypeMap() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[\"A\",\"B\"]}"), NO_TERRAIN_TYPE_MAP);
	}

	@Test
	void testTerrainTypeMapIsEmpty() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[\"A\",\"B\"],\"terrain_type_map\":[]}"), WRONG_TERRAIN_TYPE_MAP_SIZE);
	}

	@Test
	void testTerrainTypeMapIsNotAnArray() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[\"A\",\"B\"],\"terrain_type_map\":6}"), WRONG_TERRAIN_TYPE_MAP_FORMAT);
	}

	@Test
	void testTerrainTypeMapHasWrongNumberOfRows() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[\"A\",\"B\"]," +
				"\"terrain_type_map\":[\"0,0\",\"0,0\"]}"), WRONG_TERRAIN_TYPE_MAP_SIZE);
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[\"A\",\"B\"]," +
				"\"terrain_type_map\":[\"0,0\",\"0,0\",\"0,0\",\"0,0\"]}"), WRONG_TERRAIN_TYPE_MAP_SIZE);
	}

	@Test
	void testTerrainTypeMapRowHasWrongSize() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[\"A\",\"B\"]," +
				"\"terrain_type_map\":[\"0,0\",\"0,0,1\",\"0,0\"]}"), WRONG_TERRAIN_TYPE_MAP_SIZE);
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[\"A\",\"B\"]," +
				"\"terrain_type_map\":[\"0,0\",\"0,0\",\"0\"]}"), WRONG_TERRAIN_TYPE_MAP_SIZE);
	}

	@Test
	void testTerrainTypeMapRowHasWrongContent() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[\"A\",\"B\"]," +
				"\"terrain_type_map\":[\"0,0\",\"A,0\",\"0,0\"]}"), WRONG_TERRAIN_TYPE_ROW_FORMAT);
	}

	@Test
	void testTerrainTypeMapHasNotUsedTerrainType() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[\"A\",\"B\"]," +
				"\"terrain_type_map\":[\"0,0\",\"2,0\",\"0,0\"]}"), NOT_USED_TERRAIN_TYPE);
	}

	@Test
	void testParseString() throws IOException, OutsideMapException {
		SketchMap sketchMap = converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[\"A\",\"B\"]," +
				"\"terrain_type_map\":[\"0,0\",\"0,1\",\"0,0\"]}");

		assertNotNull(sketchMap);

		Map2d<SketchCell> cells = sketchMap.getCells();

		assertNotNull(cells);
		assertThat(cells.getWidth(), is(2));
		assertThat(cells.getHeight(), is(3));

		TerrainType terrainTypeA = manager.getOrDefault("A");
		TerrainType terrainTypeB = manager.getOrDefault("B");

		for(int i = 0; i < cells.getSize(); i++) {
			SketchCell cell = sketchMap.getCells().getCell(i);

			if(i == 3) {
				assertThat(cell.getTerrainType(), is(equalTo(terrainTypeB)));
			}
			else {
				assertThat(cell.getTerrainType(), is(equalTo(terrainTypeA)));
			}
		}
	}
}