package jfws.util.math.generator.gradient;

import jfws.util.math.interpolation.TwoValueInterpolator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

class CircularGradientTest extends GradientTest {

	private CircularGradient circularGradient;

	@BeforeEach
	public void setUp() {
		interpolator = mock(TwoValueInterpolator.class);
		circularGradient = new CircularGradient(interpolator, CENTER, MAX_DISTANCE, VALUE_AT_CENTER, VALUE_AT_MAX);
	}

	@Test
	public void testGenerateAtCenter() {
		when(interpolator.interpolate(VALUE_AT_CENTER, VALUE_AT_MAX, 0.0)).thenReturn(VALUE_AT_CENTER);

		assertThat(circularGradient.generate(CENTER_X, CENTER_Y), is(equalTo(VALUE_AT_CENTER)));

		verify(interpolator).interpolate(VALUE_AT_CENTER, VALUE_AT_MAX, 0.0);
	}

	@Test
	public void testGenerateWithDiagonal() {
		when(interpolator.interpolate(VALUE_AT_CENTER, VALUE_AT_MAX, 0.5)).thenReturn(VALUE_AT_HALF);

		assertThat(circularGradient.generate(DIAG_HALF_X, DIAG_HALF_Y), is(equalTo(VALUE_AT_HALF)));

		verify(interpolator).interpolate(VALUE_AT_CENTER, VALUE_AT_MAX, 0.5);
	}

	@Test
	public void testGenerateAtRadius() {
		when(interpolator.interpolate(VALUE_AT_CENTER, VALUE_AT_MAX, 1.0)).thenReturn(VALUE_AT_MAX);

		assertThat(circularGradient.generate(CENTER_X + MAX_DISTANCE, CENTER_Y), is(equalTo(VALUE_AT_MAX)));
		assertThat(circularGradient.generate(CENTER_X - MAX_DISTANCE, CENTER_Y), is(equalTo(VALUE_AT_MAX)));
		assertThat(circularGradient.generate(CENTER_X, CENTER_Y + MAX_DISTANCE), is(equalTo(VALUE_AT_MAX)));
		assertThat(circularGradient.generate(CENTER_X, CENTER_Y - MAX_DISTANCE), is(equalTo(VALUE_AT_MAX)));

		verifyZeroInteractions(interpolator);
	}

	@Test
	public void testGenerateBeyondRadius() {
		assertThat(circularGradient.generate(CENTER_X + MAX_DISTANCE * 2, CENTER_Y), is(equalTo(VALUE_AT_MAX)));
		assertThat(circularGradient.generate(CENTER_X - MAX_DISTANCE * 2, CENTER_Y), is(equalTo(VALUE_AT_MAX)));
		assertThat(circularGradient.generate(CENTER_X, CENTER_Y + MAX_DISTANCE * 2), is(equalTo(VALUE_AT_MAX)));
		assertThat(circularGradient.generate(CENTER_X, CENTER_Y - MAX_DISTANCE * 2), is(equalTo(VALUE_AT_MAX)));

		verifyNoMoreInteractions(interpolator);
	}
}