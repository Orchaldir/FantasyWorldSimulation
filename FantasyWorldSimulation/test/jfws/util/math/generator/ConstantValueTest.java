package jfws.util.math.generator;

import jfws.util.math.geometry.Point2d;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

class ConstantValueTest {

	public static final double VALUE = 66.7;

	@Test
	public void test() {
		ConstantValue constantValue = new ConstantValue(VALUE);
		assertThat(constantValue.generate(0, 0), is(equalTo(VALUE)));
	}

	@Test
	public void testDefaultGenerate() {
		ConstantValue constantValue = new ConstantValue(VALUE);
		assertThat(constantValue.generate(new Point2d(4, 5)), is(equalTo(VALUE)));
	}

}