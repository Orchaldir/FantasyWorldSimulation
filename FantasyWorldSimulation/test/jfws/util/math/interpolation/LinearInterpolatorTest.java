package jfws.util.math.interpolation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinearInterpolatorTest extends TwoValueInterpolatorTest {

	@BeforeEach
	void setUp() {
		interpolator = new LinearInterpolator();
	}

	@Test
	void test() {
		testInterpolation(0.25, 4.5);
		testInterpolation(0.5, MIDDLE);
		testInterpolation(0.75, 5.5);
	}

}