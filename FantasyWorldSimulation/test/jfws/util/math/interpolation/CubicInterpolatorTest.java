package jfws.util.math.interpolation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

class CubicInterpolatorTest {

	private CubicInterpolator cubicInterpolator;

	@BeforeEach
	public void setUp() {
		cubicInterpolator = new CubicInterpolator();
	}

	@Test
	public void testInterpolateZeros() {
		double[] p = { 0.0, 0.0, 0.0, 0.0};

		for(double x = 0.0; x < 1.0; x += 0.1) {
			assertThat(cubicInterpolator.interpolate(p, x), is(equalTo(0.0)));
		}
	}

	@Test
	public void testInterpolateOnes() {
		double[] p = { 1.0, 1.0, 1.0, 1.0};

		for(double x = 0.0; x < 1.0; x += 0.1) {
			assertThat(cubicInterpolator.interpolate(p, x), is(equalTo(1.0)));
		}
	}
}