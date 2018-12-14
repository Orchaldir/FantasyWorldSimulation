package jfws.util.math.generator.gradient;

import jfws.util.math.interpolation.TwoValueInterpolator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.AdditionalMatchers.eq;
import static org.mockito.Mockito.*;


class AbsoluteLinearGradientTest extends GradientTest {

	public static final double ERROR = 0.001;
	private AbsoluteLinearGradient gradient;

	@BeforeEach
	public void setUp() {
		interpolator = mock(TwoValueInterpolator.class);
		gradient = new AbsoluteLinearGradient(interpolator, CENTER, DIRECTION, MAX_DISTANCE, VALUE_AT_CENTER, VALUE_AT_MAX);
	}

	@Test
	public void testGenerateAtBaseLine() {
		when(interpolator.interpolate(eq(VALUE_AT_CENTER, ERROR), eq(VALUE_AT_MAX, ERROR), eq(0.0, ERROR))).
				thenReturn(VALUE_AT_CENTER);

		assertThat(gradient.generate(CENTER_X, CENTER_Y), is(equalTo(VALUE_AT_CENTER)));
		assertThat(gradient.generate(BASE_LEFT_X, BASE_LEFT_Y), is(equalTo(VALUE_AT_CENTER)));
		assertThat(gradient.generate(BASE_RIGHT_X, BASE_RIGHT_Y), is(equalTo(VALUE_AT_CENTER)));

		verify(interpolator, times(3)).
				interpolate(eq(VALUE_AT_CENTER, ERROR), eq(VALUE_AT_MAX, ERROR), eq(0.0, ERROR));
	}

	@Nested
	class TestGenerateInFront {

		@Test
		public void testAtHalf() {
			when(interpolator.interpolate(eq(VALUE_AT_CENTER, ERROR), eq(VALUE_AT_MAX, ERROR), eq(0.5, ERROR))).
					thenReturn(VALUE_AT_HALF);

			assertThat(gradient.generate(DIAG_HALF_X, DIAG_HALF_Y), is(equalTo(VALUE_AT_HALF)));

			verify(interpolator).interpolate(eq(VALUE_AT_CENTER, ERROR), eq(VALUE_AT_MAX, ERROR), eq(0.5, ERROR));
		}

		@Test
		public void testAtMax() {
			assertThat(gradient.generate(DIAG_MAX_X, DIAG_MAX_Y), is(equalTo(VALUE_AT_MAX)));

			verifyZeroInteractions(interpolator);
		}

		@Test
		public void testBeyondMax() {
			assertThat(gradient.generate(DIAG_BEYOND_X, DIAG_BEYOND_Y), is(equalTo(VALUE_AT_MAX)));

			verifyZeroInteractions(interpolator);
		}
	}

	@Nested
	class TestGenerateBehind {

		@Test
		public void testBehindAtHalf() {
			when(interpolator.interpolate(eq(VALUE_AT_CENTER, ERROR), eq(VALUE_AT_MAX, ERROR), eq(0.5, ERROR))).
					thenReturn(VALUE_AT_HALF);

			assertThat(gradient.generate(BEHIND_HALF_X, BEHIND_HALF_Y), is(equalTo(VALUE_AT_HALF)));

			verify(interpolator).interpolate(eq(VALUE_AT_CENTER, ERROR), eq(VALUE_AT_MAX, ERROR), eq(0.5, ERROR));
		}

		@Test
		public void testBehindAtMax() {
			assertThat(gradient.generate(BEHIND_MAX_X, BEHIND_MAX_Y), is(equalTo(VALUE_AT_MAX)));

			verifyZeroInteractions(interpolator);
		}

		@Test
		public void testBehindBeyondMax() {
			assertThat(gradient.generate(BEHIND_BEYOND_X, BEHIND_BEYOND_Y), is(equalTo(VALUE_AT_MAX)));

			verifyZeroInteractions(interpolator);
		}
	}
}