package jfws.editor.map.tool;

import jfws.maps.sketch.ChangeTerrainTypeCommand;
import jfws.maps.sketch.SketchCell;
import jfws.maps.sketch.SketchMap;
import jfws.maps.sketch.terrain.TerrainType;
import jfws.util.command.Command;
import jfws.util.command.CommandHistory;
import jfws.util.map.OutsideMapException;
import jfws.util.map.ToCellMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ChangeTerrainTypeToolTest {

	public static final double X = 2.0;
	public static final double Y = 3.0;
	private CommandHistory commandHistory;

	private SketchMap sketchMap;
	private ToCellMapper<SketchCell> toCellMapper;
	private SketchCell cell;

	private TerrainType terrainType0;
	private TerrainType terrainType1;

	private ChangeTerrainTypeTool tool;

	@BeforeEach
	public void setUp() {
		commandHistory = mock(CommandHistory.class);
		toCellMapper = mock(ToCellMapper.class);
		sketchMap = mock(SketchMap.class);
		cell = mock(SketchCell.class);
		terrainType0 = mock(TerrainType.class);
		terrainType1 = mock(TerrainType.class);

		tool = new ChangeTerrainTypeTool(commandHistory, sketchMap, terrainType0);
	}

	private void verifyEmptyCommandHistory() {
		verifyZeroInteractions(commandHistory);
	}

	// sketch map

	@Test
	public void testSetSketchMap() {
		SketchMap newSketchMap = mock(SketchMap.class);

		tool.setSketchMap(newSketchMap);

		assertThat(tool.getSketchMap(), is(newSketchMap));

		verifyEmptyCommandHistory();
	}

	// terrain type

	@Test
	public void testGetTerrainType() {
		assertThat(tool.getTerrainType(), is(terrainType0));

		verifyEmptyCommandHistory();
	}

	@Test
	public void testChangeTerrainType() {
		tool.changeTerrainType(terrainType1);

		assertThat(tool.getTerrainType(), is(terrainType1));

		verifyEmptyCommandHistory();
	}

	@Test
	public void testChangeTerrainTypeToSame() {
		tool.changeTerrainType(terrainType0);

		assertThat(tool.getTerrainType(), is(terrainType0));

		verifyEmptyCommandHistory();
	}

	// use()

	@Test
	public void testUseOutsideMap() {
		when(sketchMap.getToCellMapper()).thenReturn(toCellMapper);
		when(toCellMapper.getCell(X, Y)).thenThrow(new OutsideMapException(0, 1, 2, false));

		assertFalse(tool.use(X, Y));

		verifyEmptyCommandHistory();
	}

	@Test
	public void testUseWithSameType() {
		when(sketchMap.getToCellMapper()).thenReturn(toCellMapper);
		when(toCellMapper.getCell(X, Y)).thenReturn(cell);
		when(cell.getTerrainType()).thenReturn(terrainType0);

		assertFalse(tool.use(X, Y));

		verifyEmptyCommandHistory();
	}

	@Test
	public void testUse() {
		ArgumentCaptor<Command> commandCaptor = ArgumentCaptor.forClass(Command.class);

		when(sketchMap.getToCellMapper()).thenReturn(toCellMapper);
		when(toCellMapper.getCell(X, Y)).thenReturn(cell);
		when(cell.getTerrainType()).thenReturn(terrainType1);

		assertTrue(tool.use(X, Y));

		verify(commandHistory, times(1)).execute(commandCaptor.capture());

		Command command = commandCaptor.getValue();

		assertThat(command, is(instanceOf(ChangeTerrainTypeCommand.class)));
	}

}