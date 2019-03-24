package jfws.feature.world.generation;

import jfws.feature.world.WorldCell;
import jfws.util.math.generator.Generator;
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

	private Face<Void, Void, WorldCell> face0;
	private Face<Void, Void, WorldCell> face1;

	private CellGenerationStep cellGenerationStep;

	@BeforeEach
	public void setUp() {
		generator = mock(Generator.class);

		mesh = mock(Mesh.class);

		face0 = mock(Face.class);
		face1 = mock(Face.class);

		cellGenerationStep = spy(new AddGeneratorStep(generator, INDEX));
	}

	@Test
	public void testGenerate() {
		doNothing().when(cellGenerationStep).generateCell(any());
		when(mesh.getFaces()).thenReturn(List.of(face0, face1));

		cellGenerationStep.generate(mesh);

		verifyGenerateFace(face0);
		verifyGenerateFace(face1);
	}

	private void verifyGenerateFace(Face<Void, Void, WorldCell> face0) {
		verify(cellGenerationStep, times(1)).generateCell(eq(face0));
	}

}