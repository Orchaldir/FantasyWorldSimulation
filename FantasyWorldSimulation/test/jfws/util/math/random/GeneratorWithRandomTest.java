package jfws.util.math.random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

class GeneratorWithRandomTest {

	private static final long SEED = 42;
	private static final long DIFFERENT_SEED = 101;

	private static final int FIRST_INT = -1170105035;
	private static final int SECOND_INT = 234785527;
	private static final int THIRD_INT = -1360544799;

	private static final int DIFFERENT_FIRST_INT = -1194344215;
	private static final int DIFFERENT_SECOND_INT = 15869780;
	private static final int DIFFERENT_THIRD_INT = 1565603387;

	public static final double ERROR = 0.01;

	private RandomNumberGenerator generator;

	@BeforeEach
	public void setUp() {
		generator = new GeneratorWithRandom(SEED);
	}

	@Test
	public void testGetSeed(){
		assertThat(generator.getSeed(), is(equalTo(SEED)));
	}

	@Test
	public void testRestart(){
		assertThat(generator.getInteger(), is(equalTo(FIRST_INT)));
		assertThat(generator.getInteger(), is(equalTo(SECOND_INT)));
		assertThat(generator.getInteger(), is(equalTo(THIRD_INT)));

		generator.restart();

		assertThat(generator.getInteger(), is(equalTo(FIRST_INT)));
		assertThat(generator.getInteger(), is(equalTo(SECOND_INT)));
		assertThat(generator.getInteger(), is(equalTo(THIRD_INT)));
	}

	@Test
	public void testRestartSameSeed(){
		assertThat(generator.getInteger(), is(equalTo(FIRST_INT)));
		assertThat(generator.getInteger(), is(equalTo(SECOND_INT)));
		assertThat(generator.getInteger(), is(equalTo(THIRD_INT)));

		generator.restart(SEED);

		assertThat(generator.getInteger(), is(equalTo(FIRST_INT)));
		assertThat(generator.getInteger(), is(equalTo(SECOND_INT)));
		assertThat(generator.getInteger(), is(equalTo(THIRD_INT)));
	}

	@Test
	public void testRestartWithDifferentSeed(){
		assertThat(generator.getInteger(), is(equalTo(FIRST_INT)));
		assertThat(generator.getInteger(), is(equalTo(SECOND_INT)));
		assertThat(generator.getInteger(), is(equalTo(THIRD_INT)));

		generator.restart(DIFFERENT_SEED);

		assertThat(generator.getInteger(), is(equalTo(DIFFERENT_FIRST_INT)));
		assertThat(generator.getInteger(), is(equalTo(DIFFERENT_SECOND_INT)));
		assertThat(generator.getInteger(), is(equalTo(DIFFERENT_THIRD_INT)));
	}

	// getInteger()

	@Test
	public void testMinOfGetIntegerWithMax(){
		final int desiredMaxValue = 5;
		int min = 10000;

		for(int i = 0; i < 1000; i++) {
			int value = generator.getInteger(desiredMaxValue);
			min = Math.min(min, value);
		}

		assertThat(min, is(equalTo(0)));
	}

	@Test
	public void testMaxOfGetIntegerWithMax(){
		final int desiredMaxValue = 5;
		int max = -1000;

		for(int i = 0; i < 1000; i++) {
			int value = generator.getInteger(desiredMaxValue);
			max = Math.max(max, value);
		}

		assertThat(max, is(equalTo(desiredMaxValue)));
	}

	// getDoubleBetweenZeroAndOne()

	@Test
	public void testMinOfGetDoubleBetweenZeroAndOne(){
		double min = 10000;

		for(int i = 0; i < 1000; i++) {
			double value = generator.getDoubleBetweenZeroAndOne();
			min = Math.min(min, value);
		}

		assertThat(min, is(closeTo(0.0, ERROR)));
	}

	@Test
	public void testMaxOfGetDoubleBetweenZeroAndOne(){
		double max = -10000;

		for(int i = 0; i < 1000; i++) {
			double value = generator.getDoubleBetweenZeroAndOne();
			max = Math.max(max, value);
		}

		assertThat(max, is(closeTo(1.0, ERROR)));
	}

	// getGaussian()

	@Test
	public void testGaussianMeanIsZero(){
		int number = 10000;
		double mean = 0;

		for(int i = 0; i <number; i++) {
			mean += generator.getGaussian();
		}

		mean /= number;

		assertThat(mean, is(closeTo(0, ERROR)));
	}
}