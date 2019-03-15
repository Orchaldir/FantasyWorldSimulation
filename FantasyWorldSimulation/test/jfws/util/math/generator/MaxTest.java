package jfws.util.math.generator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

class MaxTest {

	public static final double X = 1.0;
	public static final double Y = 2.0;

	public static final double VALUE0 = -2.0;
	public static final double VALUE1 = 11.3;
	public static final double VALUE2 = 100.9;

	private Generator generator0;
	private Generator generator1;
	private Generator generator2;
	private Max max;

	@BeforeEach
	public void setUp() {
		generator0 = mock(Generator.class);
		generator1 = mock(Generator.class);
		generator2 = mock(Generator.class);

		max = new Max(List.of(generator0, generator1, generator2));
	}

	@Test
	public void testCalculateNoise() {
		when(generator0.generate(X, Y)).thenReturn(VALUE0);
		when(generator1.generate(X, Y)).thenReturn(VALUE1);
		when(generator2.generate(X, Y)).thenReturn(VALUE2);

		assertThat(max.generate(X, Y), is(equalTo(VALUE2)));

		verify(generator0).generate(X, Y);
		verify(generator1).generate(X, Y);
		verify(generator2).generate(X, Y);
	}
}