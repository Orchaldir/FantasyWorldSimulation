package jfws.util.math.random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

class GeneratorWithRandomTest {

	static final long SEED = 42;
	static final long DIFFERENT_SEED = 101;

	static final int FIRST_INT = -1170105035;
	static final int SECOND_INT = 234785527;
	static final int THIRD_INT = -1360544799;

	static final int DIFFERENT_FIRST_INT = -1194344215;
	static final int DIFFERENT_SECOND_INT = 15869780;
	static final int DIFFERENT_THIRD_INT = 1565603387;

	RandomNumberGenerator generator;

	@BeforeEach
	void setup(){
		generator = new GeneratorWithRandom(SEED);
	}

	@Test
	void testGetSeed(){
		assertThat(generator.getSeed(), is(equalTo(SEED)));
	}

	@Test
	void testRestart(){
		assertThat(generator.getInteger(), is(equalTo(FIRST_INT)));
		assertThat(generator.getInteger(), is(equalTo(SECOND_INT)));
		assertThat(generator.getInteger(), is(equalTo(THIRD_INT)));

		generator.restart();

		assertThat(generator.getInteger(), is(equalTo(FIRST_INT)));
		assertThat(generator.getInteger(), is(equalTo(SECOND_INT)));
		assertThat(generator.getInteger(), is(equalTo(THIRD_INT)));
	}

	@Test
	void testRestartSameSeed(){
		assertThat(generator.getInteger(), is(equalTo(FIRST_INT)));
		assertThat(generator.getInteger(), is(equalTo(SECOND_INT)));
		assertThat(generator.getInteger(), is(equalTo(THIRD_INT)));

		generator.restart(SEED);

		assertThat(generator.getInteger(), is(equalTo(FIRST_INT)));
		assertThat(generator.getInteger(), is(equalTo(SECOND_INT)));
		assertThat(generator.getInteger(), is(equalTo(THIRD_INT)));
	}

	@Test
	void testRestartWithDifferentSeed(){
		assertThat(generator.getInteger(), is(equalTo(FIRST_INT)));
		assertThat(generator.getInteger(), is(equalTo(SECOND_INT)));
		assertThat(generator.getInteger(), is(equalTo(THIRD_INT)));

		generator.restart(DIFFERENT_SEED);

		assertThat(generator.getInteger(), is(equalTo(DIFFERENT_FIRST_INT)));
		assertThat(generator.getInteger(), is(equalTo(DIFFERENT_SECOND_INT)));
		assertThat(generator.getInteger(), is(equalTo(DIFFERENT_THIRD_INT)));
	}

	// getGaussian()

	@Test
	void testGaussianMeanIsZero(){
		int number = 10000;
		double mean = 0;

		for(int i = 0; i <number; i++) {
			mean += generator.getGaussian();
		}

		mean /= number;

		assertThat(mean, is(closeTo(0, 0.1)));
	}
}