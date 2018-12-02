package jfws.util.math.noise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

class ScalableNoiseTest {

	public static final double RESOLUTION0 = 10.5;
	public static final double RESOLUTION1 = 2.3;
	public static final double X = 1.0;
	public static final double Y = 2.0;
	public static final double RESULT = 5.0;

	private Noise noise;
	private ScalableNoise scalableNoise;

	@BeforeEach
	public void setUp() {
		noise = mock(Noise.class);
		scalableNoise = new ScalableNoise(noise, RESOLUTION0);
	}

	@Test
	public void testGetResolution() {
		assertThat(scalableNoise.getResolution(), is(equalTo(RESOLUTION0)));
	}

	@Test
	public void testSetResolution() {
		scalableNoise.setResolution(RESOLUTION1);
		assertThat(scalableNoise.getResolution(), is(equalTo(RESOLUTION1)));
	}

	@Test
	public void testCalculateNoise() {
		when(noise.calculateNoise(X/ RESOLUTION0, Y/ RESOLUTION0)).thenReturn(RESULT);

		assertThat(scalableNoise.calculateNoise(X, Y), is(equalTo(RESULT)));

		verify(noise, times(1)).calculateNoise(X/ RESOLUTION0, Y/ RESOLUTION0);
	}
}