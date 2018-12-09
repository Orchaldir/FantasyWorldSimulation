package jfws.util.math.generator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

class ScaledInputTest {

	public static final double RESOLUTION0 = 10.5;
	public static final double RESOLUTION1 = 2.3;
	public static final double X = 1.0;
	public static final double Y = 2.0;
	public static final double RESULT = 5.0;

	private Generator noise;
	private ScaledInput scaledInput;

	@BeforeEach
	public void setUp() {
		noise = mock(Generator.class);
		scaledInput = new ScaledInput(noise, RESOLUTION0);
	}

	@Test
	public void testGetResolution() {
		assertThat(scaledInput.getResolution(), is(equalTo(RESOLUTION0)));
	}

	@Test
	public void testSetResolution() {
		scaledInput.setResolution(RESOLUTION1);
		assertThat(scaledInput.getResolution(), is(equalTo(RESOLUTION1)));
	}

	@Test
	public void testCalculateNoise() {
		when(noise.generate(X/ RESOLUTION0, Y/ RESOLUTION0)).thenReturn(RESULT);

		assertThat(scaledInput.generate(X, Y), is(equalTo(RESULT)));

		verify(noise, times(1)).generate(X/ RESOLUTION0, Y/ RESOLUTION0);
	}
}