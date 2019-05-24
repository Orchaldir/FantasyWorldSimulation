package jfws.feature.world.generation;

import jfws.feature.world.WorldCell;
import jfws.util.math.geometry.mesh.Face;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static jfws.feature.world.WorldCell.ELEVATION;
import static jfws.feature.world.WorldCell.RAINFALL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ModifyWithAttributeStepTest {

	public static final int SOURCE_INDEX = ELEVATION;
	public static final int TARGET_INDEX = RAINFALL;

	private Face<Void, Void, WorldCell> face;
	private ModifyWithAttributeStep step;
	private WorldCell cell;

	@BeforeEach
	public void setUp() {

		face = mock(Face.class);

		step = new ModifyWithAttributeStep(SOURCE_INDEX, TARGET_INDEX, 0.1, 1.5, 10.0);

		cell = new WorldCell();
		when(face.getData()).thenReturn(cell);
	}

	@Test
	public void testGenerateCell() {
		testGenerateCell(0.5, 1.2, 6.2);
	}

	@Test
	public void testGenerateCellBelowMin() {
		testGenerateCell(0.05, 1.2, 2.2);
	}

	@Test
	public void testGenerateCellAboveMax() {
		testGenerateCell(1.6, 1.2, 16.2);
	}

	private void testGenerateCell(double sourceValue, double targetAtStart, double targetAfterwards) {
		cell.setAttribute(SOURCE_INDEX, sourceValue);
		cell.setAttribute(TARGET_INDEX, targetAtStart);

		step.generateCell(face.getData());

		assertThat(cell.getAttribute(SOURCE_INDEX), is(equalTo(sourceValue)));
		assertThat(cell.getAttribute(TARGET_INDEX), is(equalTo(targetAfterwards)));
	}
}