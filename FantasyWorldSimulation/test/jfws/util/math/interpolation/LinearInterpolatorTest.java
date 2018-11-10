package jfws.util.math.interpolation;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class LinearInterpolatorTest {

	private static final double START = 4.0;
	private static final double END = 6.0;

	private LinearInterpolator interpolator = new LinearInterpolator();
	private double[] values = new double[Interpolator1d.ARRAY_SIZE];

	void testInterpolation(double factor, double result)  {
		assertThat(interpolator.interpolate(START, END, factor), is(equalTo(result)));

		values[0] = 0;
		values[1] = START;
		values[2] = END;
		values[3] = 0;

		assertThat(interpolator.interpolate(values, factor), is(equalTo(result)));
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
	void test() {
		testInterpolation(0.25, 4.5);
		testInterpolation(0.5, 5.0);
		testInterpolation(0.75, 5.5);
	}

}