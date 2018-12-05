package jfws.util.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static jfws.util.map.MapInterpolator.TARGET_MAP_IS_SMALLER_EXCEPTION;
import static jfws.util.map.MapInterpolator.WIDTH_AND_HEIGHT_DO_NOT_MATCH_EXCEPTION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

class MapInterpolatorTest {

	public static final int SOURCE_WIDTH = 4;
	public static final int SOURCE_HEIGHT = 3;
	public static final int SOURCE_SIZE = SOURCE_WIDTH * SOURCE_HEIGHT;

	public static final int TARGET_CELL_SIZE = 4;

	public static final int TARGET_WIDTH = SOURCE_WIDTH * TARGET_CELL_SIZE;
	public static final int TARGET_HEIGHT = SOURCE_HEIGHT * TARGET_CELL_SIZE;
	public static final int TARGET_SIZE = TARGET_WIDTH * TARGET_HEIGHT;

	private CellMap2d<Double> sourceMap;
	private CellMap2d<Double> targetMap;

	private CellInterpolator cellInterpolator;
	private MapInterpolator mapInterpolator;

	@BeforeEach
	public void setUp() {
		Double[] sourceArray = new Double[SOURCE_SIZE];
		sourceMap = new ArrayCellMap2D<>(SOURCE_WIDTH, SOURCE_HEIGHT, sourceArray);

		Double[] targetArray = new Double[TARGET_SIZE];
		targetMap = new ArrayCellMap2D<>(TARGET_WIDTH, TARGET_HEIGHT, targetArray);

		cellInterpolator = mock(CellInterpolator.class);
		mapInterpolator = new MapInterpolator(cellInterpolator);
	}

	// calculateCellSize()

	@Test
	public void testCalculateCellSize() {
		assertThat(mapInterpolator.calculateCellSize(sourceMap, targetMap), is(equalTo(TARGET_CELL_SIZE)));
	}

	@Test
	public void testCalculateCellSizeWithSourceLargerThanTarget() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> mapInterpolator.calculateCellSize(targetMap, sourceMap));

		assertThat(exception.getMessage(), is(equalTo(TARGET_MAP_IS_SMALLER_EXCEPTION)));
	}

	@Test
	public void testCalculateCellSizeWithWidthAndHeightToNotMatch() {
		int newHeight = TARGET_HEIGHT + 5;
		Double[] array = new Double[TARGET_WIDTH * newHeight];
		CellMap2d<Double> map = new ArrayCellMap2D<>(TARGET_WIDTH, newHeight, array);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> mapInterpolator.calculateCellSize(sourceMap, map));

		assertThat(exception.getMessage(), is(equalTo(WIDTH_AND_HEIGHT_DO_NOT_MATCH_EXCEPTION)));
	}

	// interpolate()

	@Test
	public void testInterpolate() {
		InOrder inOrder = inOrder(cellInterpolator);

		mapInterpolator.interpolate(sourceMap, targetMap);

		for(int y = 0; y < SOURCE_HEIGHT; y++) {
			for (int x = 0; x < SOURCE_WIDTH; x++) {
				inOrder.verify(cellInterpolator).prepareSourceValues(sourceMap, x, y);
				inOrder.verify(cellInterpolator).interpolateCell(targetMap, x, y, TARGET_CELL_SIZE);
			}
		}

		inOrder.verifyNoMoreInteractions();
	}
}