package jfws.util.math.generator;

import jfws.util.math.generator.noise.Transformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

class TransformationTest {

	public static final double MIN_VALUE = -1.0;
	public static final double MAX_VALUE = 4.0;
	public static final double RESOLUTION0 = 10.5;
	public static final double X = 1.0;
	public static final double Y = 2.0;

	public static final double RESULT = 5.0;
	public static final int OFFSET_X = 10;
	public static final double OFFSET_Y = 20.5;

	private Generator noise;
	private Transformation transformation;

	@BeforeEach
	public void setUp() {
		noise = mock(Generator.class);
		transformation = new Transformation(noise, MIN_VALUE, MAX_VALUE, RESOLUTION0);
	}

	@Test
	public void testGetResolution() {
		assertThat(transformation.getResolution(), is(equalTo(RESOLUTION0)));
	}

	@Test
	public void testCalculateNoise() {
		double newX = X / RESOLUTION0;
		double newY = Y / RESOLUTION0;

		when(noise.generate(newX, newY)).thenReturn(RESULT);

		assertThat(transformation.generate(X, Y), is(equalTo(14.0)));

		verify(noise, times(1)).generate(newX, newY);
	}

	@Test
	public void testCalculateNoiseWithOffset() {
		transformation = new Transformation(noise, MIN_VALUE, MAX_VALUE, OFFSET_X, OFFSET_Y, RESOLUTION0);

		double newX = (X + OFFSET_X) / RESOLUTION0;
		double newY = (Y + OFFSET_Y) / RESOLUTION0;

		when(noise.generate(newX, newY)).thenReturn(RESULT);

		assertThat(transformation.generate(X, Y), is(equalTo(14.0)));

		verify(noise, times(1)).generate(newX, newY);
	}
}