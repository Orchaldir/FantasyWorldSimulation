package jfws.util.math.interpolation;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static jfws.util.math.interpolation.TwoValueInterpolator.END;
import static jfws.util.math.interpolation.TwoValueInterpolator.START;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BiTwoValueInterpolatorTest {

	public static final double FACTOR_X = 0.4;
	public static final double FACTOR_Y = 0.6;
	public static final double RESULT_0 = 2.0;
	public static final double RESULT_1 = 4.0;
	public static final double RESULT_3 = 6.0;

	@Test
	public void testCreateBiCosineInterpolator() {
		BiTwoValueInterpolator interpolator = BiTwoValueInterpolator.createBiCosineInterpolator();

		assertNotNull(interpolator);
		assertNotNull(interpolator.getInterpolator());
		assertThat(interpolator.getInterpolator(), is(instanceOf(CosineInterpolator.class)));
	}

	@Test
	public void testCreateBilinearInterpolator() {
		BiTwoValueInterpolator interpolator = BiTwoValueInterpolator.createBilinearInterpolator();

		assertNotNull(interpolator);
		assertNotNull(interpolator.getInterpolator());
		assertThat(interpolator.getInterpolator(), is(instanceOf(LinearInterpolator.class)));
	}

	@Test
	public void testInterpolate() {
		double[][] values = {{0,1,2,3}, {4,5,6,7}, {8,9,10,11}, {12,12,14,15}};
		TwoValueInterpolator twoValueInterpolator = Mockito.mock(TwoValueInterpolator.class);

		when(twoValueInterpolator.interpolate(values[START], FACTOR_Y)).thenReturn(RESULT_0);
		when(twoValueInterpolator.interpolate(values[END], FACTOR_Y)).thenReturn(RESULT_1);
		when(twoValueInterpolator.interpolate(RESULT_0, RESULT_1, FACTOR_X)).thenReturn(RESULT_3);

		BiTwoValueInterpolator interpolator = new BiTwoValueInterpolator(twoValueInterpolator);

		assertThat(interpolator.interpolate(values, FACTOR_X, FACTOR_Y), is(closeTo(RESULT_3, 0.001)));

		verify(twoValueInterpolator).interpolate(values[START], FACTOR_Y);
		verify(twoValueInterpolator).interpolate(values[END], FACTOR_Y);
		verify(twoValueInterpolator).interpolate(RESULT_0, RESULT_1, FACTOR_X);
	}
}