package jfws.feature.world.generation;

import jfws.feature.world.WorldCell;
import jfws.util.math.generator.Generator;
import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.mesh.Face;
import jfws.util.math.geometry.mesh.Mesh;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

class CellGenerationStepTest {

	private static  final int INDEX = 1;

	private Generator generator;

	private Mesh<Void, Void, WorldCell> mesh;

	private static final Point2d POINT_0 = new Point2d(1, 2);
	private static final Point2d POINT_1 = new Point2d(3, 4);

	private Face<Void, Void, WorldCell> face0;
	private Face<Void, Void, WorldCell> face1;

	private WorldCell cell0;
	private WorldCell cell1;

	private AddGeneratorStep cellGenerationStep;

	@BeforeEach
	public void setUp() {
		generator = mock(Generator.class);

		mesh = mock(Mesh.class);

		face0 = mock(Face.class);
		face1 = mock(Face.class);

		cell0 = mock(WorldCell.class);
		cell1 = mock(WorldCell.class);

		cellGenerationStep = spy(new AddGeneratorStep(generator, INDEX));
	}

	@Test
	public void testGenerate() {
		doNothing().when(cellGenerationStep).generateCell(any(), any());
		when(mesh.getFaces()).thenReturn(List.of(face0, face1));
		when(face0.getPointsInCCW()).thenReturn(List.of(POINT_0));
		when(face1.getPointsInCCW()).thenReturn(List.of(POINT_1));
		when(face0.getData()).thenReturn(cell0);
		when(face1.getData()).thenReturn(cell1);

		cellGenerationStep.generate(mesh);

		verifyGenerateFace(cell0, POINT_0);
		verifyGenerateFace(cell1, POINT_1);
	}

	private void verifyGenerateFace(WorldCell cell, Point2d point) {
		verify(cellGenerationStep, times(1)).generateCell(eq(cell), eq(point));
	}

}