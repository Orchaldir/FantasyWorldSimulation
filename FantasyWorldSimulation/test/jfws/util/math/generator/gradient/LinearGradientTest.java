package jfws.util.math.generator.gradient;

import jfws.util.math.interpolation.TwoValueInterpolator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.AdditionalMatchers.eq;
import static org.mockito.Mockito.*;


class LinearGradientTest extends GradientTest {

	public static final double ERROR = 0.001;
	private LinearGradient linearGradient;

	@BeforeEach
	public void setUp() {
		interpolator = mock(TwoValueInterpolator.class);
		linearGradient = new LinearGradient(interpolator, CENTER, DIRECTION, MAX_DISTANCE, VALUE_AT_CENTER, VALUE_AT_MAX);
	}

	@Test
	public void testGenerateAtBaseLine() {
		assertThat(linearGradient.generate(CENTER_X, CENTER_Y), is(equalTo(VALUE_AT_CENTER)));
		assertThat(linearGradient.generate(BASE_LEFT_X, BASE_LEFT_Y), is(equalTo(VALUE_AT_CENTER)));
		assertThat(linearGradient.generate(BASE_RIGHT_X, BASE_RIGHT_Y), is(equalTo(VALUE_AT_CENTER)));

		verifyZeroInteractions(interpolator);
	}

	@Test
	public void testGenerateAtHalf() {
		when(interpolator.interpolate(eq(VALUE_AT_CENTER, ERROR), eq(VALUE_AT_MAX, ERROR), eq(0.5, ERROR))).
				thenReturn(VALUE_AT_HALF);

		assertThat(linearGradient.generate(DIAG_HALF_X, DIAG_HALF_Y), is(equalTo(VALUE_AT_HALF)));

		verify(interpolator).interpolate(eq(VALUE_AT_CENTER, ERROR), eq(VALUE_AT_MAX, ERROR), eq(0.5, ERROR));
	}

	@Test
	public void testGenerateBehind() {
		assertThat(linearGradient.generate(BEHIND_HALF_X, BEHIND_HALF_Y), is(equalTo(VALUE_AT_CENTER)));

		verifyZeroInteractions(interpolator);
	}

	@Test
	public void testGenerateAtMax() {
		assertThat(linearGradient.generate(DIAG_MAX_X, DIAG_MAX_Y), is(equalTo(VALUE_AT_MAX)));

		verifyZeroInteractions(interpolator);
	}

	@Test
	public void testGenerateBeyondMax() {
		assertThat(linearGradient.generate(DIAG_BEYOND_X, DIAG_BEYOND_Y), is(equalTo(VALUE_AT_MAX)));

		verifyZeroInteractions(interpolator);
	}
}