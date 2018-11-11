package jfws.maps.sketch.elevation;

import jfws.util.math.random.RandomNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

class ElevationGeneratorWithNoiseTest extends ElevationGeneratorTest {

	private RandomNumberGenerator randomNumberGenerator;

	@BeforeEach
	void setup() {
		randomNumberGenerator = Mockito.mock(RandomNumberGenerator.class);
		generator = new ElevationGeneratorWithNoise(randomNumberGenerator);
	}

	@Test
	void testPrepare() {
		generator.prepare();

		verify(randomNumberGenerator).restart();
		verify(randomNumberGenerator, never()).restart(anyLong());
		verify(randomNumberGenerator, never()).getGaussian();
	}

	@Test
	void testGenerate() {
		when(randomNumberGenerator.getGaussian()).thenReturn(-0.5);

		assertThat(generator.generate(type1), is(equalTo(-40.0)));

		verify(randomNumberGenerator, never()).restart();
		verify(randomNumberGenerator, never()).restart(anyLong());
		verify(randomNumberGenerator).getGaussian();
	}

}