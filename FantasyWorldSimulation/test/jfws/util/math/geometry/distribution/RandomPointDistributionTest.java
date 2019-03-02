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

	@Test
	public void testDistributePoints() {
		int desiredNumberOfPoints = 2;

		given_a_mocked_random_number_generator();
		given_a_random_point_distribution();

		points = distribution.distributePoints(SIZE, 1.5);

		verify_generator_was_reset(1);
		verify_generator_was_called(desiredNumberOfPoints);
		verify_points_size(desiredNumberOfPoints);
		verify_points();
	}

	@Test
	public void testMoreDistributePoints() {
		given_a_random_number_generator();
		given_a_random_point_distribution();

		points = distribution.distributePoints(SIZE, 0.5);

		verify_points_size(18);
	}

	@Test
	public void testTooSmallSizeX() {
		given_a_random_number_generator();
		given_a_random_point_distribution();

		assertThrows(IllegalArgumentException.class, () ->  distribution.distributePoints(new Point2d(-1.0, 1.0), 2));
	}

	@Test
	public void testTooSmallSizeY() {
		given_a_random_number_generator();
		given_a_random_point_distribution();

		assertThrows(IllegalArgumentException.class, () ->  distribution.distributePoints(new Point2d(1.0, -1.0), 2));
	}

	// members

	private RandomNumberGenerator generator;
	private InOrder inOrder;

	private RandomPointDistribution distribution;

	private List<Point2d> points;

	private static final Point2d SIZE = new Point2d(6.0, 3.0);

	// given

	private void given_a_random_number_generator() {
		generator = new GeneratorWithRandom(0);
	}

	private void given_a_mocked_random_number_generator() {
		generator = mock(RandomNumberGenerator.class);
		inOrder = inOrder(generator);

		when(generator.getDoubleBetweenZeroAndOne()).thenReturn(0.1, 0.2, 0.3, 0.4);
	}

	private void given_a_random_point_distribution() {
		distribution =  new RandomPointDistribution(generator);
	}

	// verify

	private void verify_generator_was_reset(int N) {
		inOrder.verify(generator, times(N)).restart();
	}

	private void verify_generator_was_called(int desiredNumberOfPoints) {
		inOrder.verify(generator, times(desiredNumberOfPoints*2)).getDoubleBetweenZeroAndOne();
	}

	private void verify_points_size(int desiredNumberOfPoints) {
		assertThat(points, hasSize(desiredNumberOfPoints));
	}

	private void verify_points() {
		assertThat(points.get(0).getX(), is(closeTo(0.6, ERROR)));
		assertThat(points.get(0).getY(), is(closeTo(0.6, ERROR)));

		assertThat(points.get(1).getX(), is(closeTo(1.8, ERROR)));
		assertThat(points.get(1).getY(), is(closeTo(1.2, ERROR)));
	}

}