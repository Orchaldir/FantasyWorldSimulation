package jfws.maps.sketch;

import jfws.util.map.CellMap2d;
import jfws.util.map.OutsideMapException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static jfws.maps.sketch.terrain.SharedTestData.TERRAIN_TYPE_A;
import static jfws.maps.sketch.terrain.SharedTestData.TERRAIN_TYPE_B;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChangeTerrainTypeCommandTest {

	public static final int INDEX = 1;

	private SketchMap sketchMap;
	private CellMap2d<SketchCell> cellMap;
	private SketchCell cell;

	private ChangeTerrainTypeCommand command;

	@BeforeEach
	public void setUp() {
		sketchMap = mock(SketchMap.class);
		cellMap = mock(CellMap2d.class);
		cell = mock(SketchCell.class);

		command = new ChangeTerrainTypeCommand(sketchMap, INDEX, TERRAIN_TYPE_B);
	}

	@Test
	public void testGetName() {
		assertThat(command.getName(), is(equalTo(ChangeTerrainTypeCommand.NAME)));
	}

	@Nested
	class TestExecute {

		@Test
		public void testExecute() {
			when(sketchMap.getCellMap()).thenReturn(cellMap);
			when(cellMap.getCell(INDEX)).thenReturn(cell);
			when(cell.getTerrainType()).thenReturn(TERRAIN_TYPE_A);

			command.execute();

			verify(sketchMap).getCellMap();
			verify(cellMap).getCell(INDEX);

			InOrder inOrder = inOrder(cell);
			inOrder.verify(cell).getTerrainType();
			inOrder.verify(cell).setTerrainType(TERRAIN_TYPE_B);
			inOrder.verifyNoMoreInteractions();
		}

		@Test
		public void testExecuteOutsideMap() {
			when(sketchMap.getCellMap()).thenReturn(cellMap);
			when(cellMap.getCell(INDEX)).thenThrow(OutsideMapException.class);

			assertThrows(OutsideMapException.class, () -> command.execute());

			verify(sketchMap).getCellMap();
			verify(cellMap).getCell(INDEX);

			verifyNoMoreInteractions(cell);
		}
	}

	@Nested
	class TestUnExecute {

		private void prepare() {
			when(sketchMap.getCellMap()).thenReturn(cellMap);
			when(cellMap.getCell(INDEX)).thenReturn(cell);
			when(cell.getTerrainType()).thenReturn(TERRAIN_TYPE_A);

			command.execute();

			reset(sketchMap);
			reset(cellMap);
			reset(cell);
		}

		@Test
		public void testUnExecute() {
			prepare();

			when(sketchMap.getCellMap()).thenReturn(cellMap);
			when(cellMap.getCell(INDEX)).thenReturn(cell);

			command.unExecute();

			verify(sketchMap).getCellMap();
			verify(cellMap).getCell(INDEX);
			verify(cell).setTerrainType(TERRAIN_TYPE_A);
		}

		@Test
		public void testWithoutExecute() {
			assertThrows(IllegalStateException.class, () -> command.unExecute());

			verifyNoMoreInteractions(sketchMap);
			verifyNoMoreInteractions(cellMap);
			verifyNoMoreInteractions(cell);
		}

		@Test
		public void testUnExecuteOutsideMap() {
			prepare();

			when(sketchMap.getCellMap()).thenReturn(cellMap);
			when(cellMap.getCell(INDEX)).thenThrow(OutsideMapException.class);

			assertThrows(OutsideMapException.class, () -> command.unExecute());

			verify(sketchMap).getCellMap();
			verify(cellMap).getCell(INDEX);

			verifyNoMoreInteractions(cell);
		}
	}

}