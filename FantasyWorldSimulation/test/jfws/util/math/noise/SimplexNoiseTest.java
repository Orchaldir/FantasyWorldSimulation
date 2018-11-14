package jfws.util.math.noise;

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

	@Test
	public void testCalculateNoiseInInterval() {
		for(double x = -RANGE; x < RANGE; x++) {
			for(double y = -RANGE; y < RANGE; y++) {
				double value = noise.calculateNoise(x, y);
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
				double value = noise.calculateNoise(x, y);
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
				average += noise.calculateNoise(x, y);
				numbers++;
			}
		}

		average /= numbers;

		assertThat(average, is(closeTo(0, ERROR)));
	}

}