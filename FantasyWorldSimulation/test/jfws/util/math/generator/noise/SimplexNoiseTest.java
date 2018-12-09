package jfws.util.math.generator.noise;

import jfws.util.math.generator.noise.SimplexNoise;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.core.AllOf;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
class SimplexNoiseTest {

	private static final double RANGE = 100.0;
	public static final double MIN_VALUE = -1.0;
	public static final double MAX_VALUE = 1.0;
	public static final double ERROR = 0.01;

	private SimplexNoise noise = new SimplexNoise();

	// floorFast()

	@Test
	public void testFloorFastWithInteger() {
		for(int value = -10; value < 10; value++) {
			assertThat(SimplexNoise.floorFast(value), is(equalTo(value)));
		}
	}

	@Test
	public void testFloorFastWithPositiveValue() {
		int integer = 365;
		double step = 0.01;

		for(double value = 0.0; value < 1.0; value += step) {
			assertThat(SimplexNoise.floorFast(integer + value), is(equalTo(integer)));
		}
	}

	@Test
	public void testFloorFastWithNegativeValue() {
		int integer = -8;
		double step = 0.01;

		for(double value = 0.0; value < 1.0; value += step) {
			assertThat(SimplexNoise.floorFast(integer + value), is(equalTo(integer)));
		}
	}

	// dot()

	@Test
	public void testDot() {
		SimplexNoise.Grad grad = new SimplexNoise.Grad(1.0, 2.0, 1000.0);

		assertThat(SimplexNoise.dot(grad, 3.0, 4.0), is(equalTo(11.0)));
	}

	// generate()

	@Test
	public void testCalculateNoiseInInterval() {
		for(double x = -RANGE; x < RANGE; x++) {
			for(double y = -RANGE; y < RANGE; y++) {
				double value = noise.generate(x, y);
				assertThat(value, AllOf.allOf(greaterThan(MIN_VALUE), lessThan(MAX_VALUE)));
			}
		}
	}

	@Test
	public void testCalculateNoiseMinMax() {
		double min = MAX_VALUE;
		double max = MIN_VALUE;

		for(double x = -RANGE; x < RANGE; x++) {
			for(double y = -RANGE; y < RANGE; y++) {
				double value = noise.generate(x, y);
				min = Math.min(min, value);
				max = Math.max(max, value);
			}
		}

		assertThat(min, is(closeTo(MIN_VALUE, ERROR)));
		assertThat(max, is(closeTo(MAX_VALUE, ERROR)));
	}

	@Test
	public void testCalculateNoiseAverage() {
		double average = 0.0;
		int numbers = 0;

		for(double x = -RANGE; x < RANGE; x++) {
			for(double y = -RANGE; y < RANGE; y++) {
				average += noise.generate(x, y);
				numbers++;
			}
		}

		average /= numbers;

		assertThat(average, is(closeTo(0, ERROR)));
	}

	@Test
	public void testCalculateNoise() {
		assertThat(noise.generate(1.1, 2.5), is(closeTo(-0.0703, ERROR)));
	}

}