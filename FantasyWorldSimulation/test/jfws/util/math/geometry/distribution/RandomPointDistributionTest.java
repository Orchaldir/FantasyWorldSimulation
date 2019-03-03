package jfws.util.math.geometry.distribution;

import jfws.util.math.geometry.Point2d;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

class RandomPointDistributionTest extends AbstractPointDistributionTest {

	public static final double ERROR = 0.001;
	public static final Point2d SIZE = new Point2d(6.0, 3.0);

	@Test
	public void testDistributePoints() {
		int desiredNumberOfPoints = 2;

		createMockedRandomNumberGenerator();
		createRandomPointDistribution();

		points = distribution.distributePoints(SIZE, 1.5);

		assertGeneratorWasReset(1);
		assertGeneratorWasCalled(desiredNumberOfPoints);
		assertNumberOfPoints(desiredNumberOfPoints);
		assertPoints();
	}

	@Test
	public void testMoreDistributePoints() {
		createRandomNumberGenerator();
		createRandomPointDistribution();

		points = distribution.distributePoints(SIZE, 0.5);

		assertNumberOfPoints(18);
	}

	// given

	@Override
	protected void createRandomPointDistribution() {
		distribution =  new RandomPointDistribution(generator);
	}

	// verify

	private void assertPoints() {
		assertThat(points.get(0).getX(), is(closeTo(0.6, ERROR)));
		assertThat(points.get(0).getY(), is(closeTo(0.6, ERROR)));

		assertThat(points.get(1).getX(), is(closeTo(1.8, ERROR)));
		assertThat(points.get(1).getY(), is(closeTo(1.2, ERROR)));
	}

}