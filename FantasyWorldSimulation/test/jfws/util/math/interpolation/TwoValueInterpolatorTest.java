package jfws.util.math.interpolation;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.number.IsCloseTo.closeTo;

public abstract class TwoValueInterpolatorTest {

	public static final double START = 4.0;
	public static final double MIDDLE = 5.0;
	public static final double END = 6.0;
	public static final double DELTA = 0.001;

	protected TwoValueInterpolator interpolator;
	protected double[] values = new double[Interpolator1d.ARRAY_SIZE];

	protected void testInterpolation(double factor, double result)  {
		assertThat(interpolator.interpolate(START, END, factor), is(closeTo(result, DELTA)));

		fillValues();

		testArrayInterpolation(factor, result);
	}

	protected void testArrayInterpolation(double factor, double result)  {
		assertThat(interpolator.interpolate(values, factor), is(closeTo(result, DELTA)));
	}

	protected void fillValues(double first, double last) {
		values[0] = first;
		values[1] = START;
		values[2] = END;
		values[3] = last;
	}

	protected void fillValues() {
		fillValues(0, 0);
	}

	@Test
	void testGetStart() {
		testInterpolation(0.0, START);
	}

	@Test
	void testGetEnd() {
		testInterpolation(1.0, END);
	}

	@Test
	void testFirstValueHasNoEffect() {
		fillValues(-1, 0);
		testArrayInterpolation(0.5, MIDDLE);

		fillValues(+1, 0);
		testArrayInterpolation(0.5, MIDDLE);
	}

	@Test
	void testLastValueHasNoEffect() {
		fillValues(0, -1);
		testArrayInterpolation(0.5, MIDDLE);

		fillValues(0, +1);
		testArrayInterpolation(0.5, MIDDLE);
	}
}
