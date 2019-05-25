package jfws.feature.world.generation;

import jfws.feature.world.WorldCell;
import jfws.util.math.generator.Generator;
import jfws.util.math.geometry.Point2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

class AddGeneratorStepTest {

	private static  final int INDEX = 1;

	private Generator generator;

	private AddGeneratorStep addGeneratorStep;

	private WorldCell cell;

	private static final Point2d CENTER = new Point2d(1,0.5);

	@BeforeEach
	public void setUp() {
		generator = mock(Generator.class);

		addGeneratorStep = new AddGeneratorStep(generator, INDEX);

		cell = new WorldCell();
		cell.setAttribute(INDEX, 4.5);
	}

	@Test
	public void testGenerateCell() {
		when(generator.generate(CENTER)).thenReturn(1.2);

		addGeneratorStep.generateCell(cell, CENTER);

		assertThat(cell.getAttribute(INDEX), is(equalTo(5.7)));

		verify(generator, times(1)).generate(eq(CENTER));
	}

}