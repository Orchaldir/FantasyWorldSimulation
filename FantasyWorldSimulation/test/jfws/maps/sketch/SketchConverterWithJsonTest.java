package jfws.maps.sketch;

import jfws.maps.sketch.terrain.TerrainType;
import jfws.maps.sketch.terrain.TerrainTypeManager;
import jfws.util.io.FileUtils;
import jfws.util.map.CellMap2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.IOException;

import static jfws.maps.sketch.SketchConverterWithJson.*;
import static jfws.maps.sketch.terrain.SharedTestData.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SketchConverterWithJsonTest {

	public static final int WIDTH = 3;
	public static final int HEIGHT = 2;
	public static final String FILE_CONTENT = "test";

	private FileUtils fileUtils;
	private File file;

	private TerrainTypeManager manager;
	private SketchConverterWithJson converter;
	private SketchMap map;

	@BeforeEach
	public void setUp() {
		fileUtils = mock(FileUtils.class);
		file = mock(File.class);

		manager = new TerrainTypeManager(null, null);
		converter = new SketchConverterWithJson(fileUtils, manager);

		map = mock(SketchMap.class);
	}

	private <T extends Throwable> void assertException(Class<T> expectedType, Executable executable, String message) {
		Throwable throwable = assertThrows(expectedType, executable);
		assertThat(throwable.getMessage(), is(equalTo(message)));
	}

	// load()

	@Test
	public void testLoadWithIoException() throws IOException {
		when(fileUtils.readWholeFile(file)).thenThrow(new IOException());

		assertThrows(IOException.class, () -> converter.load(file));

		verify(fileUtils).readWholeFile(file);
	}

	@Test
	public void testLoad() throws IOException {
		SketchConverterWithJson converterSpy = spy(converter);
		SketchMap map = mock(SketchMap.class);

		when(fileUtils.readWholeFile(file)).thenReturn(FILE_CONTENT);
		doReturn(map).when(converterSpy).parseString(FILE_CONTENT);

		assertThat(converterSpy.load(file), is(map));

		verify(fileUtils).readWholeFile(file);
		verify(converterSpy).parseString(FILE_CONTENT);
	}

	// parseString()

	@Test
	public void testParseStringWithNull() {
		assertThrows(IOException.class, () -> converter.parseString(null));
	}

	@Test
	public void testParseEmptyString() {
		assertException(IOException.class, () -> converter.parseString(""), NOT_A_JSON_OBJECT);
	}

	@Test
	public void testParseWrongString() {
		assertException(IOException.class, () -> converter.parseString("abc"), NOT_A_JSON_OBJECT);
	}

	@Test
	public void testParseString() throws IOException {
		SketchMap sketchMap = converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[\"A\",\"B\"]," +
				"\"terrain_type_map\":[\"0,0\",\"0,1\",\"0,0\"]}");

		assertNotNull(sketchMap);

		CellMap2d<SketchCell> cells = sketchMap.getCellMap();

		assertNotNull(cells);
		assertThat(cells.getWidth(), is(2));
		assertThat(cells.getHeight(), is(3));

		TerrainType terrainTypeA = manager.getOrDefault("A");
		TerrainType terrainTypeB = manager.getOrDefault("B");

		for(int i = 0; i < cells.getSize(); i++) {
			SketchCell cell = sketchMap.getCellMap().getCell(i);

			if(i == 3) {
				assertThat(cell.getTerrainType(), is(equalTo(terrainTypeB)));
			}
			else {
				assertThat(cell.getTerrainType(), is(equalTo(terrainTypeA)));
			}
		}
	}

	// version

	@Test
	public void testNoVersion() {
		assertException(IOException.class, () -> converter.parseString("{}"), NO_VERSION);
	}

	@Test
	public void testWrongVersion() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":0}"), WRONG_VERSION);
	}

	@Test
	public void testWrongVersionFormat() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":A}"), WRONG_VERSION_FORMAT);
		assertException(IOException.class, () -> converter.parseString("{\"version\":[1,2]}"), WRONG_VERSION_FORMAT);
	}

	// map size

	@Test
	public void testNoWidth() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1}"), NO_SIZE);
	}

	@Test
	public void testWrongWidthFormat() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":A}"), WRONG_SIZE_FORMAT);
	}

	@Test
	public void testInvalidWidth() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":-1,\"height\":3}"), INVALID_MAP_SIZE);
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":0,\"height\":3}"), INVALID_MAP_SIZE);
	}

	@Test
	public void tesNoHeight() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":1}"), NO_SIZE);
	}

	@Test
	public void testWrongHeightFormat() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":A}"), WRONG_SIZE_FORMAT);
	}

	@Test
	public void testInvalidHeight() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":3,\"height\":-1}"), INVALID_MAP_SIZE);
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":3,\"height\":0}"), INVALID_MAP_SIZE);
	}

	// used terrain types

	@Test
	public void testNoUsedTerrainTypes() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3}"), NO_USED_TERRAIN_TYPES);
	}

	@Test
	public void testEmptyUsedTerrainTypes() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[]}"), NO_USED_TERRAIN_TYPES);
	}

	@Test
	public void testUsedTerrainTypesNotAnArray() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":3}"), WRONG_USED_TERRAIN_TYPES_FORMAT);
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":\"text\"}"), WRONG_USED_TERRAIN_TYPES_FORMAT);
	}

	// terrain type map

	@Test
	public void testNoTerrainTypeMap() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[\"A\",\"B\"]}"), NO_TERRAIN_TYPE_MAP);
	}

	@Test
	public void testTerrainTypeMapIsEmpty() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[\"A\",\"B\"],\"terrain_type_map\":[]}"), WRONG_TERRAIN_TYPE_MAP_SIZE);
	}

	@Test
	public void testTerrainTypeMapIsNotAnArray() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[\"A\",\"B\"],\"terrain_type_map\":6}"), WRONG_TERRAIN_TYPE_MAP_FORMAT);
	}

	@Test
	public void testTerrainTypeMapHasWrongNumberOfRows() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[\"A\",\"B\"]," +
				"\"terrain_type_map\":[\"0,0\",\"0,0\"]}"), WRONG_TERRAIN_TYPE_MAP_SIZE);
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[\"A\",\"B\"]," +
				"\"terrain_type_map\":[\"0,0\",\"0,0\",\"0,0\",\"0,0\"]}"), WRONG_TERRAIN_TYPE_MAP_SIZE);
	}

	@Test
	public void testTerrainTypeMapRowHasWrongSize() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[\"A\",\"B\"]," +
				"\"terrain_type_map\":[\"0,0\",\"0,0,1\",\"0,0\"]}"), WRONG_TERRAIN_TYPE_MAP_SIZE);
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[\"A\",\"B\"]," +
				"\"terrain_type_map\":[\"0,0\",\"0,0\",\"0\"]}"), WRONG_TERRAIN_TYPE_MAP_SIZE);
	}

	@Test
	public void testTerrainTypeMapRowHasWrongContent() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[\"A\",\"B\"]," +
				"\"terrain_type_map\":[\"0,0\",\"A,0\",\"0,0\"]}"), WRONG_TERRAIN_TYPE_ROW_FORMAT);
	}

	@Test
	public void testTerrainTypeMapHasNotUsedTerrainType() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":3,\"used_terrain_types\":[\"A\",\"B\"]," +
				"\"terrain_type_map\":[\"0,0\",\"2,0\",\"0,0\"]}"), NOT_USED_TERRAIN_TYPE);
	}

	// save()

	@Test
	public void tesSaveWithIoException() throws IOException {
		SketchConverterWithJson converterSpy = spy(converter);

		doReturn(FILE_CONTENT).when(converterSpy).convertToJson(map);
		doThrow(new IOException()).when(fileUtils).writeWholeFile(file, FILE_CONTENT);

		assertThrows(IOException.class, () -> converterSpy.save(file, map));

		verify(converterSpy).convertToJson(map);
		verify(fileUtils).writeWholeFile(file, FILE_CONTENT);
	}

	@Test
	public void testSave() throws IOException {
		SketchConverterWithJson converterSpy = spy(converter);

		doReturn(FILE_CONTENT).when(converterSpy).convertToJson(map);

		converterSpy.save(file, map);

		verify(converterSpy).convertToJson(map);
		verify(fileUtils).writeWholeFile(file, FILE_CONTENT);
	}

	// test

	@Test
	public void test() throws IOException {
		manager.add(TERRAIN_TYPE_A);
		manager.add(TERRAIN_TYPE_B);
		manager.add(TERRAIN_TYPE_C);

		SketchMap sketchMap =  new SketchMap(WIDTH, HEIGHT, TERRAIN_TYPE_A);
		CellMap2d<SketchCell> cellMap = sketchMap.getCellMap();

		cellMap.getCell(1, 0).setTerrainType(TERRAIN_TYPE_B);
		cellMap.getCell(2, 0).setTerrainType(TERRAIN_TYPE_C);
		cellMap.getCell(0, 1).setTerrainType(TERRAIN_TYPE_C);
		cellMap.getCell(2, 1).setTerrainType(TERRAIN_TYPE_B);

		String text = converter.convertToJson(sketchMap);
		SketchMap loadedSketchMap = converter.parseString(text);

		assertNotNull(loadedSketchMap);
		assertThat(loadedSketchMap, is(not(sketchMap)));

		cellMap = loadedSketchMap.getCellMap();

		assertThat(cellMap.getWidth(), is(WIDTH));
		assertThat(cellMap.getHeight(), is(HEIGHT));

		assertThat(cellMap.getCell(0,0).getTerrainType(), is(equalTo(TERRAIN_TYPE_A)));
		assertThat(cellMap.getCell(1,0).getTerrainType(), is(equalTo(TERRAIN_TYPE_B)));
		assertThat(cellMap.getCell(2,0).getTerrainType(), is(equalTo(TERRAIN_TYPE_C)));

		assertThat(cellMap.getCell(0,1).getTerrainType(), is(equalTo(TERRAIN_TYPE_C)));
		assertThat(cellMap.getCell(1,1).getTerrainType(), is(equalTo(TERRAIN_TYPE_A)));
		assertThat(cellMap.getCell(2,1).getTerrainType(), is(equalTo(TERRAIN_TYPE_B)));
	}
}