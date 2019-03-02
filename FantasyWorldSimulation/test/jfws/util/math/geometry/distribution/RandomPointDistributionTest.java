package jfws.util.math.geometry.distribution;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.random.GeneratorWithRandom;
import jfws.util.math.random.RandomNumberGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RandomPointDistributionTest {

	public static final double ERROR = 0.001;
	private static final Point2d SIZE = new Point2d(6.0, 3.0);

	private RandomNumberGenerator generator;
	private InOrder inOrder;
	private RandomPointDistribution distribution;

	private List<Point2d> points;

	@Test
	public void testDistributePoints() {
		int desiredNumberOfPoints = 2;

		createMockedRandomNumberGenerator();
		createRandomPointDistribution();

		points = distribution.distributePoints(SIZE, 1.5);

		assertGeneratorWasReset(1);
		assertGeneratorWasCalled(desiredNumberOfPoints);
		assertPointsSize(desiredNumberOfPoints);
		assertPoints();
	}

	@Test
	public void testMoreDistributePoints() {
		createRandomNumberGenerator();
		createRandomPointDistribution();

		points = distribution.distributePoints(SIZE, 0.5);

		assertPointsSize(18);
	}

	@Test
	public void testTooSmallSizeX() {
		createRandomNumberGenerator();
		createRandomPointDistribution();

		assertThrows(IllegalArgumentException.class, () ->  distribution.distributePoints(new Point2d(-1.0, 1.0), 2));
	}

	@Test
	public void testTooSmallSizeY() {
		createRandomNumberGenerator();
		createRandomPointDistribution();

		assertThrows(IllegalArgumentException.class, () ->  distribution.distributePoints(new Point2d(1.0, -1.0), 2));
	}

	// given

	private void createRandomNumberGenerator() {
		generator = new GeneratorWithRandom(0);
	}

	private void createMockedRandomNumberGenerator() {
		generator = mock(RandomNumberGenerator.class);
		inOrder = inOrder(generator);

		when(generator.getDoubleBetweenZeroAndOne()).thenReturn(0.1, 0.2, 0.3, 0.4);
	}

	private void createRandomPointDistribution() {
		distribution =  new RandomPointDistribution(generator);
	}

	// verify

	private void assertGeneratorWasReset(int N) {
		inOrder.verify(generator, times(N)).restart();
	}

	private void assertGeneratorWasCalled(int desiredNumberOfPoints) {
		inOrder.verify(generator, times(desiredNumberOfPoints*2)).getDoubleBetweenZeroAndOne();
	}

	private void assertPointsSize(int desiredNumberOfPoints) {
		assertThat(points, hasSize(desiredNumberOfPoints));
	}

	private void assertPoints() {
		assertThat(points.get(0).getX(), is(closeTo(0.6, ERROR)));
		assertThat(points.get(0).getY(), is(closeTo(0.6, ERROR)));

		assertThat(points.get(1).getX(), is(closeTo(1.8, ERROR)));
		assertThat(points.get(1).getY(), is(closeTo(1.2, ERROR)));
	}

}