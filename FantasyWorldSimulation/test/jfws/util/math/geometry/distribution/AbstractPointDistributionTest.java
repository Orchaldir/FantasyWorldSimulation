package jfws.util.math.geometry.distribution;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.random.GeneratorWithRandom;
import jfws.util.math.random.RandomNumberGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public abstract class AbstractPointDistributionTest {
	protected RandomNumberGenerator generator;
	protected PointDistribution distribution;
	protected List<Point2d> points;
	protected InOrder inOrder;

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

	protected void createRandomNumberGenerator() {
		generator = new GeneratorWithRandom(0);
	}

	protected void createMockedRandomNumberGenerator() {
		generator = mock(RandomNumberGenerator.class);
		inOrder = inOrder(generator);

		when(generator.getDoubleBetweenZeroAndOne()).thenReturn(0.1, 0.2, 0.3, 0.4);
	}

	protected void createRandomNumberGeneratorSpy() {
		generator = Mockito.spy(new GeneratorWithRandom(0));
		inOrder = inOrder(generator);
	}

	protected abstract void createRandomPointDistribution();

	protected void assertGeneratorWasReset(int N) {
		inOrder.verify(generator, times(N)).restart();
	}

	protected void assertGeneratorWasCalled(int desiredNumberOfPoints) {
		inOrder.verify(generator, times(desiredNumberOfPoints*2)).getDoubleBetweenZeroAndOne();
	}

	protected void assertNumberOfPoints(int desiredNumberOfPoints) {
		assertThat(points, hasSize(desiredNumberOfPoints));
	}

	protected void assertInsideRectangle(Point2d point, double minX, double maxX, double minY, double maxY) {
		assertThat(point.getX(), is(greaterThanOrEqualTo(minX)));
		assertThat(point.getX(), is(lessThanOrEqualTo(maxX)));

		assertThat(point.getY(), is(greaterThanOrEqualTo(minY)));
		assertThat(point.getY(), is(lessThanOrEqualTo(maxY)));
	}
}
