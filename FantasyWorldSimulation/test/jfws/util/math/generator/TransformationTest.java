package jfws.util.math.generator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

class TransformationTest {

	public static final double RESOLUTION0 = 10.5;
	public static final double X = 1.0;
	public static final double Y = 2.0;

	public static final double RESULT = 5.0;

	private Generator noise;
	private Transformation transformation;

	@BeforeEach
	public void setUp() {
		noise = mock(Generator.class);
		transformation = new Transformation(noise, -1.0, 4.0, RESOLUTION0);
	}

	@Test
	public void testGetResolution() {
		assertThat(transformation.getResolution(), is(equalTo(RESOLUTION0)));
	}

	@Test
	public void testCalculateNoise() {
		when(noise.generate(X/ RESOLUTION0, Y/ RESOLUTION0)).thenReturn(RESULT);

		assertThat(transformation.generate(X, Y), is(equalTo(24.0)));

		verify(noise, times(1)).generate(X/ RESOLUTION0, Y/ RESOLUTION0);
	}
}