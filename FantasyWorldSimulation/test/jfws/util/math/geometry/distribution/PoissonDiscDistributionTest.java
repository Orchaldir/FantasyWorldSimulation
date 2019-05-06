package jfws.util.math.geometry.distribution;

import jfws.util.math.geometry.Point2d;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class PoissonDiscDistributionTest extends AbstractPointDistributionTest{

	private static final Point2d SIZE = new Point2d(1.0, 2.0);
	public static final double RADIUS = 0.05;

	@Test
	public void testPoints() {
		createRandomNumberGeneratorSpy();
		createRandomPointDistribution();

		points = distribution.distributePoints(SIZE, 1000);

		assertGeneratorWasReset(1);
		assertThat(points.size(), is(greaterThan(100)));
		assertPointsAreInside();
		assertPointsAreNotTooClose();
	}

	//

	@Override
	protected void createRandomPointDistribution() {
		distribution = new PoissonDiscDistribution(generator, RADIUS);
	}

	private void assertPointsAreInside() {
		for (Point2d point : points) {
			assertInsideRectangle(point, 0.0, SIZE.getX(), 0.0, SIZE.getY());
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