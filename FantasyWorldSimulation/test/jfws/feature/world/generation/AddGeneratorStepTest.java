package jfws.feature.world.generation;

import jfws.feature.world.WorldCell;
import jfws.util.math.generator.Generator;
import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.mesh.Face;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

class AddGeneratorStepTest {

	private static  final int INDEX = 1;

	private Generator generator;

	private Face<Void, Void, WorldCell> face;

	private AddGeneratorStep addGeneratorStep;

	private WorldCell cell;

	private static final Point2d CORNER0 = new Point2d(0,0);
	private static final Point2d CORNER1 = new Point2d(2,0);
	private static final Point2d CORNER2 = new Point2d(2,1);
	private static final Point2d CORNER3 = new Point2d(0,1);
	private static final Point2d CENTER = new Point2d(1,0.5);

	@BeforeEach
	public void setUp() {
		generator = mock(Generator.class);

		face = mock(Face.class);

		addGeneratorStep = new AddGeneratorStep(generator, INDEX);

		cell = new WorldCell();
		cell.setAttribute(INDEX, 4.5);
	}

	@Test
	public void testGenerateCell() {
		when(face.getData()).thenReturn(cell);
		when(face.getPointsInCCW()).thenReturn(List.of(CORNER0, CORNER1, CORNER2, CORNER3));
		when(generator.generate(CENTER)).thenReturn(1.2);

		addGeneratorStep.generateCell(face.getData(), Point2d.calculateCentroid(face.getPointsInCCW()));

		assertThat(cell.getAttribute(INDEX), is(equalTo(5.7)));

		verify(face, times(1)).getData();
		verify(face, times(1)).getPointsInCCW();
		verify(generator, times(1)).generate(eq(CENTER));
	}

}