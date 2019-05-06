package jfws.util.math.geometry.distribution;

import jfws.util.math.geometry.Point2d;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class GridWithNoiseDistributionTest extends AbstractPointDistributionTest {

	private static final Point2d SIZE = new Point2d(1.0, 2.0);
	public static final double RADIUS = 0.05;
	public static final int MAX_POINTS = 200;

	@Test
	public void testPoints() {
		createRandomNumberGeneratorSpy();
		createRandomPointDistribution();

		points = distribution.distributePoints(SIZE, MAX_POINTS);

		assertGeneratorWasReset(1);
		assertNumberOfPoints(MAX_POINTS);
		assertPointsAreInsideGridCells(10, 20);
	}

	private void assertPointsAreInsideGridCells(int columns, int rows) {
		double cellSize = RADIUS * 2.0;
		double startX = 0.0;

		for (int x = 0; x < columns; x++) {
			double startY = 0.0;

			for (int y = 0; y < rows; y++) {
				int index = x * rows + y;
				Point2d point = points.get(index);

				assertInsideRectangle(point, startX, startX + cellSize, startY, startY + cellSize);

				startY += cellSize;
			}

			startX += cellSize;
		}
	}

	//

	@Override
	protected void createRandomPointDistribution() {
		distribution = new GridWithNoiseDistribution(generator);
	}
}