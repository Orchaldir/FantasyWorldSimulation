package jfws.util.math.interpolation;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BiFourValueInterpolatorTest {

	public static final double FACTOR_X = 0.4;
	public static final double FACTOR_Y = 0.6;

	public static final double RESULT_0 = 2.0;
	public static final double RESULT_1 = 4.0;
	public static final double RESULT_2 = 6.0;
	public static final double RESULT_3 = 8.0;
	public static final double RESULT = 10.0;

	@Test
	public void testCreateBicubicInterpolator() {
		BiFourValueInterpolator interpolator = BiFourValueInterpolator.createBicubicInterpolator();

		assertNotNull(interpolator);
		assertNotNull(interpolator.getInterpolator());
		assertThat(interpolator.getInterpolator(), is(instanceOf(CubicInterpolator.class)));
	}

	@Test
	public void testInterpolate() {
		double[][] values = {{0,1,2,3}, {4,5,6,7}, {8,9,10,11}, {12,12,14,15}};
		Interpolator1d mockInterpolator = Mockito.mock(TwoValueInterpolator.class);

		when(mockInterpolator.interpolate(values[0], FACTOR_Y)).thenReturn(RESULT_0);
		when(mockInterpolator.interpolate(values[1], FACTOR_Y)).thenReturn(RESULT_1);
		when(mockInterpolator.interpolate(values[2], FACTOR_Y)).thenReturn(RESULT_2);
		when(mockInterpolator.interpolate(values[3], FACTOR_Y)).thenReturn(RESULT_3);

		double[] interpolatedValues = {RESULT_0, RESULT_1, RESULT_2, RESULT_3};

		when(mockInterpolator.interpolate(interpolatedValues, FACTOR_X)).thenReturn(RESULT);

		BiFourValueInterpolator interpolator = new BiFourValueInterpolator(mockInterpolator);

		assertThat(interpolator.interpolate(values, FACTOR_X, FACTOR_Y), is(closeTo(RESULT, 0.001)));

		verify(mockInterpolator).interpolate(values[0], FACTOR_Y);
		verify(mockInterpolator).interpolate(values[1], FACTOR_Y);
		verify(mockInterpolator).interpolate(values[2], FACTOR_Y);
		verify(mockInterpolator).interpolate(values[3], FACTOR_Y);
		verify(mockInterpolator).interpolate(interpolatedValues, FACTOR_X);
	}

}