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
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

class AddGeneratorStepTest {

	private static  final int INDEX = 1;

	private Generator generator;

	private Mesh<Void, Void, WorldCell> mesh;

	private Face<Void, Void, WorldCell> face0;
	private Face<Void, Void, WorldCell> face1;

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

		mesh = mock(Mesh.class);

		face0 = mock(Face.class);
		face1 = mock(Face.class);

		addGeneratorStep = spy(new AddGeneratorStep(generator, INDEX));

		cell = new WorldCell();
		cell.attributes[INDEX] = 4.5;
	}

	@Test
	public void testGenerate() {
		doNothing().when(addGeneratorStep).generateFace(any());
		when(mesh.getFaces()).thenReturn(List.of(face0, face1));

		addGeneratorStep.generate(mesh);

		verifyGenerateFace(face0);
		verifyGenerateFace(face1);
	}

	private void verifyGenerateFace(Face<Void, Void, WorldCell> face0) {
		verify(addGeneratorStep, times(1)).generateFace(eq(face0));
	}

	@Test
	public void testGenerateFace() {
		when(face0.getData()).thenReturn(cell);
		when(face0.getPointsInCCW()).thenReturn(List.of(CORNER0, CORNER1, CORNER2, CORNER3));
		when(generator.generate(CENTER)).thenReturn(1.2);

		addGeneratorStep.generateFace(face0);

		assertThat(cell.attributes[INDEX], is(equalTo(5.7)));

		verify(face0, times(1)).getData();
		verify(face0, times(1)).getPointsInCCW();
		verify(generator, times(1)).generate(eq(CENTER));
	}

}