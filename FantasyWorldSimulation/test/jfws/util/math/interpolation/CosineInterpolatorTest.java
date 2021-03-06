package jfws.util.math.interpolation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CosineInterpolatorTest extends TwoValueInterpolatorTest {

	@BeforeEach
	public void setUp() {
		interpolator = new CosineInterpolator();
	}

	@Test
	public void test() {
		testInterpolation(0.25, 4.2929);
		testInterpolation(0.5, MIDDLE);
		testInterpolation(0.75, 5.7071);
	}

}