package jfws.util.math.generator.gradient;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.interpolation.TwoValueInterpolator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

class CircularGradientTest {

	private static final double RADIUS = 10.0;
	private static final double VALUE_AT_CENTER = 2.5;
	private static final double VALUE_AT_RADIUS = 4.5;
	private static final double VALUE_AT_HALF = (VALUE_AT_CENTER + VALUE_AT_RADIUS) / 2.0;

	private static final double X0 = 1.0;
	private static final double Y0 = 2.0;

	private static final double X1 = 4.0;
	private static final double Y1 = 6.0;

	private static final Point2d CENTER = new Point2d(X0, Y0);

	private TwoValueInterpolator interpolator;
	private CircularGradient circularGradient;

	@BeforeEach
	public void setUp() {
		interpolator = mock(TwoValueInterpolator.class);
		circularGradient = new CircularGradient(interpolator, CENTER, RADIUS, VALUE_AT_CENTER, VALUE_AT_RADIUS);
	}

	@Test
	public void testGenerateAtCenter() {
		when(interpolator.interpolate(VALUE_AT_CENTER, VALUE_AT_RADIUS, 0.0)).thenReturn(VALUE_AT_CENTER);

		assertThat(circularGradient.generate(X0, Y0), is(equalTo(VALUE_AT_CENTER)));

		verify(interpolator).interpolate(VALUE_AT_CENTER, VALUE_AT_RADIUS, 0.0);
	}

	@Test
	public void testGenerateWithDiagonal() {
		when(interpolator.interpolate(VALUE_AT_CENTER, VALUE_AT_RADIUS, 0.5)).thenReturn(VALUE_AT_HALF);

		assertThat(circularGradient.generate(X1, Y1), is(equalTo(VALUE_AT_HALF)));

		verify(interpolator).interpolate(VALUE_AT_CENTER, VALUE_AT_RADIUS, 0.5);
	}

	@Test
	public void testGenerateAtRadius() {
		when(interpolator.interpolate(VALUE_AT_CENTER, VALUE_AT_RADIUS, 1.0)).thenReturn(VALUE_AT_RADIUS);

		assertThat(circularGradient.generate(X0 + RADIUS, Y0), is(equalTo(VALUE_AT_RADIUS)));
		assertThat(circularGradient.generate(X0 - RADIUS, Y0), is(equalTo(VALUE_AT_RADIUS)));
		assertThat(circularGradient.generate(X0, Y0 + RADIUS), is(equalTo(VALUE_AT_RADIUS)));
		assertThat(circularGradient.generate(X0, Y0 - RADIUS), is(equalTo(VALUE_AT_RADIUS)));

		verify(interpolator, times(4)).interpolate(VALUE_AT_CENTER, VALUE_AT_RADIUS, 1.0);
	}

	@Test
	public void testGenerateBeyondRadius() {
		assertThat(circularGradient.generate(X0 + RADIUS * 2, Y0), is(equalTo(VALUE_AT_RADIUS)));
		assertThat(circularGradient.generate(X0 - RADIUS * 2, Y0), is(equalTo(VALUE_AT_RADIUS)));
		assertThat(circularGradient.generate(X0, Y0 + RADIUS * 2), is(equalTo(VALUE_AT_RADIUS)));
		assertThat(circularGradient.generate(X0, Y0 - RADIUS * 2), is(equalTo(VALUE_AT_RADIUS)));

		verifyNoMoreInteractions(interpolator);
	}
}