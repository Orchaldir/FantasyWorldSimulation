package jfws.util.math.geometry.distribution;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.random.GeneratorWithRandom;
import jfws.util.math.random.RandomNumberGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
class PoissonDiscDistributionTest {

	private static final Point2d SIZE = new Point2d(1.0, 2.0);
	public static final double RADIUS = 0.05;

	private RandomNumberGenerator spy;
	private PoissonDiscDistribution distribution;

	private List<Point2d> points;

	@Test
	public void testPoints() {
		createRandomPointDistribution();

		generatePoints();

		assertGeneratorWasReset(1);
		assertThat(points.size(), is(greaterThan(100)));
		assertPointsAreInside();
		assertPointsAreNotTooClose();
	}

	@Test
	public void testTooSmallSizeX() {
		createRandomPointDistribution();

		assertThrows(IllegalArgumentException.class, () ->  distribution.distributePoints(new Point2d(0.0, 1.0), 2));
		assertThrows(IllegalArgumentException.class, () ->  distribution.distributePoints(new Point2d(-1.0, 1.0), 2));
	}

	@Test
	public void testTooSmallSizeY() {
		createRandomPointDistribution();

		assertThrows(IllegalArgumentException.class, () ->  distribution.distributePoints(new Point2d(1.0, 0.0), 2));
		assertThrows(IllegalArgumentException.class, () ->  distribution.distributePoints(new Point2d(1.0, -1.0), 2));
	}

	//

	private void createRandomPointDistribution() {
		spy = Mockito.spy(new GeneratorWithRandom(0));
		distribution = new PoissonDiscDistribution(spy);
	}

	private void generatePoints() {
		points = distribution.distributePoints(SIZE, RADIUS);
	}

	private void assertGeneratorWasReset(int N) {
		verify(spy, times(N)).restart();
	}

	private void assertPointsAreInside() {
		for (Point2d point : points) {
			assertThat(point.getX(), is(greaterThanOrEqualTo(0.0)));
			assertThat(point.getX(), is(lessThanOrEqualTo(SIZE.getX())));

			assertThat(point.getY(), is(greaterThanOrEqualTo(0.0)));
			assertThat(point.getY(), is(lessThanOrEqualTo(SIZE.getY())));
		}
	}

	private void assertPointsAreNotTooClose() {
		for (int a = 0; a < points.size() - 1; a++) {
			Point2d pointA = points.get(a);

			for (int b = a + 1; b < points.size(); b++) {
				Point2d pointB = points.get(b);
				double distance = pointA.getDistanceTo(pointB);

				assertTrue(distance > RADIUS * 2);
			}
		}
	}

}