package jfws.util.map;

import jfws.util.math.interpolation.Interpolator2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsArrayContainingInOrder.arrayContaining;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CellInterpolatorTest {

	public static final int SOURCE_WIDTH = 5;
	public static final int SOURCE_HEIGHT = 4;
	public static final int SOURCE_SIZE = SOURCE_WIDTH * SOURCE_HEIGHT;

	public static final int TARGET_CELL_SIZE = 2;

	public static final int TARGET_WIDTH = SOURCE_WIDTH * TARGET_CELL_SIZE;
	public static final int TARGET_HEIGHT = SOURCE_HEIGHT * TARGET_CELL_SIZE;
	public static final int TARGET_SIZE = TARGET_WIDTH * TARGET_HEIGHT;

	public static final double INTERPOLATED_VALUE_0 = 0.1;
	public static final double INTERPOLATED_VALUE_1 = 0.2;
	public static final double INTERPOLATED_VALUE_2 = 0.3;
	public static final double INTERPOLATED_VALUE_3 = 0.4;

	public static final int TARGET_X0 = 4;
	public static final int TARGET_X1 = TARGET_X0+1;
	public static final int TARGET_Y0 = 2;
	public static final int TARGET_Y1 = TARGET_Y0+1;

	private CellMap2d<Double> sourceMap;
	private CellMap2d<Double> targetMap;

	private Interpolator2d interpolator;
	private SimpleCellInterpolator cellInterpolator;
	private SimpleCellInterpolator cellInterpolatorSpy;

	@BeforeEach
	public void setUp() {
		Double[] sourceArray = {
				 0.0,  1.0,  2.0,  3.0,  4.0,
				10.0, 11.0, 12.0, 13.0, 14.0,
				20.0, 21.0, 22.0, 23.0, 24.0,
				30.0, 31.0, 32.0, 33.0, 34.0
		};
		sourceMap = new ArrayCellMap2D<>(SOURCE_WIDTH, SOURCE_HEIGHT, sourceArray);

		Double[] targetArray = new Double[TARGET_SIZE];
		targetMap = new ArrayCellMap2D<>(TARGET_WIDTH, TARGET_HEIGHT, targetArray);

		interpolator = mock(Interpolator2d.class);
		cellInterpolator = new SimpleCellInterpolator(interpolator);
		cellInterpolatorSpy = spy(cellInterpolator);
	}

	private void assertSourceValue(double expected0, double expected1, double expected2, double expected3, double[] actualValues) {
		double[] expectedValues = { expected0, expected1, expected2, expected3 };
		assertArrayEquals(expectedValues, actualValues, 0.01);
	}

	// prepareSourceValues()

	@Test
	public void testPrepareSourceValues() throws OutsideMapException {
		cellInterpolator.prepareSourceValues(sourceMap, 1, 1);

		double[][] sourceValues = cellInterpolator.getSourceValues();

		assertSourceValue(0.0,  10.0,  20.0,  30.0, sourceValues[0]);
		assertSourceValue(1.0,  11.0,  21.0,  31.0, sourceValues[1]);
		assertSourceValue(2.0,  12.0,  22.0,  32.0, sourceValues[2]);
		assertSourceValue(3.0,  13.0,  23.0,  33.0, sourceValues[3]);
	}

	@Test
	public void testPrepareSourceValuesWithMinCorner() throws OutsideMapException {
		cellInterpolator.prepareSourceValues(sourceMap, 0, 0);

		double[][] sourceValues = cellInterpolator.getSourceValues();

		assertSourceValue(0.0,  0.0,  10.0,  20.0, sourceValues[0]);
		assertSourceValue(0.0,  0.0,  10.0,  20.0, sourceValues[1]);
		assertSourceValue(1.0,  1.0,  11.0,  21.0, sourceValues[2]);
		assertSourceValue(2.0,  2.0,  12.0,  22.0, sourceValues[3]);
	}

	@Test
	public void testPrepareSourceValuesWithMaxCorner() throws OutsideMapException {
		cellInterpolator.prepareSourceValues(sourceMap, 3, 2);

		double[][] sourceValues = cellInterpolator.getSourceValues();

		assertSourceValue(12.0, 22.0, 32.0,  32.0, sourceValues[0]);
		assertSourceValue(13.0, 23.0, 33.0,  33.0, sourceValues[1]);
		assertSourceValue(14.0, 24.0, 34.0,  34.0, sourceValues[2]);
		assertSourceValue(14.0, 24.0, 34.0,  34.0, sourceValues[3]);
	}

	// interpolateCell()

	@Test
	public void testInterpolateCell() throws OutsideMapException {
		double[][] sourceValues = cellInterpolator.getSourceValues();

		when(interpolator.interpolate(sourceValues, 0.0, 0.0)).thenReturn(INTERPOLATED_VALUE_0);
		when(interpolator.interpolate(sourceValues, 0.5, 0.0)).thenReturn(INTERPOLATED_VALUE_1);
		when(interpolator.interpolate(sourceValues, 0.0, 0.5)).thenReturn(INTERPOLATED_VALUE_2);
		when(interpolator.interpolate(sourceValues, 0.5, 0.5)).thenReturn(INTERPOLATED_VALUE_3);

		cellInterpolatorSpy.interpolateCell(targetMap, 2, 1, TARGET_CELL_SIZE);

		verify(cellInterpolatorSpy).setTargetValue(targetMap, TARGET_X0, TARGET_Y0, INTERPOLATED_VALUE_0);
		verify(cellInterpolatorSpy).setTargetValue(targetMap, TARGET_X1, TARGET_Y0, INTERPOLATED_VALUE_1);
		verify(cellInterpolatorSpy).setTargetValue(targetMap, TARGET_X0, TARGET_Y1, INTERPOLATED_VALUE_2);
		verify(cellInterpolatorSpy).setTargetValue(targetMap, TARGET_X1, TARGET_Y1, INTERPOLATED_VALUE_3);
	}

	//

	private class SimpleCellInterpolator extends CellInterpolator<Double,Double> {

		public SimpleCellInterpolator(Interpolator2d interpolator) {
			super(interpolator);
		}

		public double[][] getSourceValues() {
			return  sourceValues;
		}

		@Override
		public double getSourceValue(Double sourceCell) {
			return sourceCell;
		}

		@Override
		public void setTargetValue(CellMap2d<Double> targetMap, int targetX, int targetY, double targetValue) throws OutsideMapException {

		}
	}

}