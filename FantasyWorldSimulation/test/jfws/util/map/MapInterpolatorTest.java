package jfws.util.map;

import jfws.util.math.interpolation.Interpolator2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static jfws.util.map.MapInterpolator.TARGET_MAP_IS_SMALLER_EXCEPTION;
import static jfws.util.map.MapInterpolator.WIDTH_AND_HEIGHT_DO_NOT_MATCH_EXCEPTION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MapInterpolatorTest {

	public static final int SOURCE_WIDTH = 4;
	public static final int SOURCE_HEIGHT = 3;
	public static final int SOURCE_SIZE = SOURCE_WIDTH * SOURCE_HEIGHT;

	public static final int TARGET_CELL_SIZE = 4;

	public static final int TARGET_WIDTH = SOURCE_WIDTH * TARGET_CELL_SIZE;
	public static final int TARGET_HEIGHT = SOURCE_HEIGHT * TARGET_CELL_SIZE;
	public static final int TARGET_SIZE = TARGET_WIDTH * TARGET_HEIGHT;

	private Interpolator2d interpolator;
	private Map2d<Double> sourceMap;
	private Map2d<Double> targetMap;

	@BeforeEach
	public void setUp() {
		interpolator = mock(Interpolator2d.class);

		Double[] sourceArray = new Double[SOURCE_SIZE];
		sourceMap = new ArrayMap2d<>(SOURCE_WIDTH, SOURCE_HEIGHT, sourceArray);

		Double[] targetArray = new Double[TARGET_SIZE];
		targetMap = new ArrayMap2d<>(TARGET_WIDTH, TARGET_HEIGHT, targetArray);
	}

	// calculateCellSize()

	@Test
	public void testCalculateCellSize() {
		assertThat(MapInterpolator.calculateCellSize(sourceMap, targetMap), is(equalTo(TARGET_CELL_SIZE)));
	}

	@Test
	public void testCalculateCellSizeWithSourceLargerThanTarget() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> MapInterpolator.calculateCellSize(targetMap, sourceMap));

		assertThat(exception.getMessage(), is(equalTo(TARGET_MAP_IS_SMALLER_EXCEPTION)));
	}

	@Test
	public void testCalculateCellSizeWithWidthAndHeightToNotMatch() {
		int newHeight = TARGET_HEIGHT + 5;
		Double[] array = new Double[TARGET_WIDTH * newHeight];
		Map2d<Double> map = new ArrayMap2d<>(TARGET_WIDTH, newHeight, array);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> MapInterpolator.calculateCellSize(sourceMap, map));

		assertThat(exception.getMessage(), is(equalTo(WIDTH_AND_HEIGHT_DO_NOT_MATCH_EXCEPTION)));
	}
}