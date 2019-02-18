package jfws.app.editor.map;

import jfws.app.editor.map.tool.ChangeTerrainTypeTool;
import jfws.maps.region.RegionMap;
import jfws.maps.sketch.SketchCell;
import jfws.maps.sketch.SketchConverter;
import jfws.maps.sketch.SketchConverterWithJson;
import jfws.maps.sketch.SketchMap;
import jfws.maps.sketch.terrain.TerrainType;
import jfws.maps.sketch.terrain.TerrainTypeConverter;
import jfws.maps.sketch.terrain.TerrainTypeManager;
import jfws.util.command.CommandHistory;
import jfws.util.map.CellMap2d;
import jfws.util.map.ToCellMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MapStorageTest {

	public static final String TYPE_NAME_0 = "type0";
	public static final String TYPE_NAME_1 = "type1";
	public static final int WIDTH = 2;
	public static final int HEIGHT = 3;
	public static final int CELLS_PER_SKETCH_CELL = 4;

	private TerrainTypeConverter terrainTypeConverter;
	private TerrainTypeManager terrainTypeManager;
	private SketchConverter sketchConverter;
	private CommandHistory commandHistory;

	private TerrainType terrainType0;
	private TerrainType terrainType1;
	private SketchMap sketchMap;
	private CellMap2d<SketchCell> cellMap;
	private ToCellMapper<SketchCell> toCellMapper;

	private MapStorage mapStorage;

	@BeforeEach
	public void setUp() {
		terrainTypeConverter = mock(TerrainTypeConverter.class);
		terrainTypeManager = mock(TerrainTypeManager.class);
		sketchConverter = mock(SketchConverter.class);
		commandHistory = mock(CommandHistory.class);

		terrainType0 = mock(TerrainType.class);
		terrainType1 = mock(TerrainType.class);

		sketchMap = mock(SketchMap.class);
		cellMap = mock(CellMap2d.class);
		toCellMapper = mock(ToCellMapper.class);

		mapStorage = new MapStorage(terrainTypeConverter, terrainTypeManager, sketchConverter, commandHistory, CELLS_PER_SKETCH_CELL);
	}

	private void assertRegionMap(SketchMap sketchMap, int width, int height) {
		RegionMap regionMap = mapStorage.getRegionMap();

		assertNotNull(regionMap);
		assertThat(regionMap.getParentMap().get(), is(equalTo(sketchMap)));
		assertThat(regionMap.getCellMap().getWidth(), is(equalTo(width)));
		assertThat(regionMap.getCellMap().getHeight(), is(equalTo(height)));
	}

	@Test
	public void testConstructor() {
		mapStorage = new MapStorage(CELLS_PER_SKETCH_CELL);

		assertThat(mapStorage.getCellsPerSketchCell(), is(equalTo(CELLS_PER_SKETCH_CELL)));
		assertThat(mapStorage.getTerrainTypeConverter(), is(instanceOf(TerrainTypeConverter.class)));
		assertThat(mapStorage.getSketchConverter(), is(instanceOf(SketchConverterWithJson.class)));
	}

	@Test
	public void testCreateEmptyMap() {
		when(terrainTypeManager.getOrDefault(TYPE_NAME_0)).thenReturn(terrainType0);

		mapStorage.createEmptyMap(WIDTH, HEIGHT, TYPE_NAME_0);

		verify(terrainTypeManager).getOrDefault(TYPE_NAME_0);

		SketchMap sketchMap = mapStorage.getSketchMap();

		assertNotNull(sketchMap);
		assertThat(sketchMap.getCellMap().getWidth(), is(equalTo(WIDTH)));
		assertThat(sketchMap.getCellMap().getHeight(), is(equalTo(HEIGHT)));

		assertRegionMap(sketchMap, WIDTH*CELLS_PER_SKETCH_CELL, HEIGHT*CELLS_PER_SKETCH_CELL);
	}

	// createTool()

	@Test
	public void testCreateToolWithoutMap() {
		assertThrows(IllegalStateException.class, () -> mapStorage.createTool(TYPE_NAME_1));
	}

	@Test
	public void testCreateTool() {
		mapStorage.createEmptyMap(WIDTH, HEIGHT, TYPE_NAME_0);

		when(terrainTypeManager.getOrDefault(TYPE_NAME_1)).thenReturn(terrainType0);

		mapStorage.createTool(TYPE_NAME_1);

		verify(terrainTypeManager).getOrDefault(TYPE_NAME_1);

		ChangeTerrainTypeTool tool = mapStorage.getChangeTerrainTypeTool();

		assertNotNull(tool);
		assertThat(tool.getTerrainType(), is(equalTo(terrainType0)));

		assertNotNull(tool.getSketchMap());
		assertThat(tool.getSketchMap(), is(equalTo(mapStorage.getSketchMap())));
	}

	// changeTypeOfTool()

	@Test
	public void testChangeTypeOfToolWithoutTool() {
		assertThrows(IllegalStateException.class, () -> mapStorage.changeTypeOfTool(TYPE_NAME_1));
	}

	@Test
	public void testChangeTypeOfTool() {
		when(terrainTypeManager.getOrDefault(TYPE_NAME_0)).thenReturn(terrainType0);

		mapStorage.createEmptyMap(WIDTH, HEIGHT, TYPE_NAME_0);
		mapStorage.createTool(TYPE_NAME_0);

		when(terrainTypeManager.getOrDefault(TYPE_NAME_1)).thenReturn(terrainType1);

		mapStorage.changeTypeOfTool(TYPE_NAME_1);

		verify(terrainTypeManager).getOrDefault(TYPE_NAME_1);

		ChangeTerrainTypeTool tool = mapStorage.getChangeTerrainTypeTool();

		assertNotNull(tool);
		assertThat(tool.getTerrainType(), is(equalTo(terrainType1)));
	}

	// setSketchMap()

	@Test
	public void testSetSketchMap() {
		when(sketchMap.getToCellMapper()).thenReturn(toCellMapper);
		when(sketchMap.getCellMap()).thenReturn(cellMap);

		when(cellMap.getWidth()).thenReturn(WIDTH);
		when(cellMap.getHeight()).thenReturn(HEIGHT);

		mapStorage.setSketchMap(sketchMap);

		assertRegionMap(sketchMap, WIDTH*CELLS_PER_SKETCH_CELL, HEIGHT*CELLS_PER_SKETCH_CELL);
	}

	@Test
	public void testSetSketchMapWithTool() {
		when(terrainTypeManager.getOrDefault(TYPE_NAME_0)).thenReturn(terrainType0);

		mapStorage.createEmptyMap(WIDTH, HEIGHT, TYPE_NAME_0);
		mapStorage.createTool(TYPE_NAME_0);

		when(sketchMap.getToCellMapper()).thenReturn(toCellMapper);
		when(sketchMap.getCellMap()).thenReturn(cellMap);

		when(cellMap.getWidth()).thenReturn(WIDTH);
		when(cellMap.getHeight()).thenReturn(HEIGHT);

		mapStorage.setSketchMap(sketchMap);

		assertThat(mapStorage.getChangeTerrainTypeTool().getSketchMap(), is(equalTo(sketchMap)));
	}
}