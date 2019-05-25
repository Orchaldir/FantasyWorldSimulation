package jfws.feature.world.generation;

import jfws.feature.world.WorldCell;
import jfws.util.map.CellMap2d;
import jfws.util.map.ToCellMapper;
import jfws.util.math.generator.Generator;
import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.mesh.Face;
import jfws.util.math.geometry.mesh.Mesh;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class IterationTest {

	private static  final int INDEX = 1;
	private static  final int WIDTH = 2;
	private static  final int HEIGHT = 1;

	private Generator generator;

	private Mesh<Void, Void, WorldCell> mesh;

	private ToCellMapper<WorldCell> mapper;
	private CellMap2d<WorldCell> cellMap;

	private static final Point2d POINT_0 = new Point2d(1, 2);
	private static final Point2d POINT_1 = new Point2d(3, 4);

	private Face<Void, Void, WorldCell> face0;
	private Face<Void, Void, WorldCell> face1;

	private WorldCell cell0;
	private WorldCell cell1;

	@BeforeEach
	public void setUp() {
		generator = mock(Generator.class);

		mesh = mock(Mesh.class);

		mapper = mock(ToCellMapper.class);
		cellMap = mock(CellMap2d.class);

		face0 = mock(Face.class);
		face1 = mock(Face.class);

		cell0 = mock(WorldCell.class);
		cell1 = mock(WorldCell.class);
	}

	@Nested
	class TestAddGeneratorStep {

		private AddGeneratorStep addGeneratorStep;

		@BeforeEach
		public void setUp() {
			addGeneratorStep = spy(new AddGeneratorStep(generator, INDEX));
		}

		@Test
		public void testAddGeneratorStepWithMesh() {
			doNothing().when(addGeneratorStep).generateCell(any(), any());
			when(mesh.getFaces()).thenReturn(List.of(face0, face1));
			when(face0.getPointsInCCW()).thenReturn(List.of(POINT_0));
			when(face1.getPointsInCCW()).thenReturn(List.of(POINT_1));
			when(face0.getData()).thenReturn(cell0);
			when(face1.getData()).thenReturn(cell1);

			addGeneratorStep.generate(mesh);

			verify(addGeneratorStep, times(1)).generate(eq(mesh));
			verifyGenerateFace(cell0, POINT_0);
			verifyGenerateFace(cell1, POINT_1);
			verifyNoMoreInteractions(addGeneratorStep);
		}

		@Test
		public void testAddGeneratorStepWithCellMap() {
			doNothing().when(addGeneratorStep).generateCell(any(), any());
			when(mapper.getMap()).thenReturn(cellMap);
			when(cellMap.getWidth()).thenReturn(WIDTH);
			when(cellMap.getHeight()).thenReturn(HEIGHT);
			when(mapper.getCellOrigin(0, 0)).thenReturn(POINT_0);
			when(mapper.getCellOrigin(1, 0)).thenReturn(POINT_1);
			when(cellMap.getCell(0, 0)).thenReturn(cell0);
			when(cellMap.getCell(1, 0)).thenReturn(cell1);

			addGeneratorStep.generate(mapper);

			verify(addGeneratorStep, times(1)).generate(eq(mapper));
			verifyGenerateFace(cell0, POINT_0);
			verifyGenerateFace(cell1, POINT_1);
			verifyNoMoreInteractions(addGeneratorStep);
		}

		private void verifyGenerateFace(WorldCell cell, Point2d point) {
			verify(addGeneratorStep, times(1)).generateCell(eq(cell), eq(point));
		}
	}

	@Nested
	class TestModifyWithAttributeStep {

		private ModifyWithAttributeStep modifyWithAttributeStep;

		@BeforeEach
		public void setUp() {
			modifyWithAttributeStep = spy(new ModifyWithAttributeStep(1, INDEX, 0.1, 1.5, 10.0));
		}

		@Test
		public void testModifyWithAttributeStepWithMesh() {
			doNothing().when(modifyWithAttributeStep).generateCell(any());
			when(mesh.getFaces()).thenReturn(List.of(face0, face1));
			when(face0.getData()).thenReturn(cell0);
			when(face1.getData()).thenReturn(cell1);

			modifyWithAttributeStep.generate(mesh);

			verify(modifyWithAttributeStep, times(1)).generate(eq(mesh));
			verifyGenerateFace(cell0);
			verifyGenerateFace(cell1);
			verifyNoMoreInteractions(modifyWithAttributeStep);
		}

		@Test
		public void testModifyWithAttributeStepWithCellMap() {
			doNothing().when(modifyWithAttributeStep).generateCell(any());
			when(mapper.getMap()).thenReturn(cellMap);
			when(cellMap.getWidth()).thenReturn(WIDTH);
			when(cellMap.getHeight()).thenReturn(HEIGHT);
			when(cellMap.getCell(0, 0)).thenReturn(cell0);
			when(cellMap.getCell(1, 0)).thenReturn(cell1);

			modifyWithAttributeStep.generate(mapper);


			verify(modifyWithAttributeStep, times(1)).generate(eq(mapper));
			verifyGenerateFace(cell0);
			verifyGenerateFace(cell1);
			verifyNoMoreInteractions(modifyWithAttributeStep);
		}

		private void verifyGenerateFace(WorldCell cell) {
			verify(modifyWithAttributeStep, times(1)).generateCell(eq(cell));
		}
	}

}