package jfws.util.math.geometry.distribution;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.random.RandomNumberGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class GridWithNoiseDistribution extends AbstractPointDistribution {

	public static final double MIN_DISTANCE = 0.2;
	protected RandomNumberGenerator generator;

	@Override
	public List<Point2d> distributePoints(Point2d size, int maxPoints) {
		checkForInvalidSize(size);

		int rows = calculateRows(size, maxPoints);
		int columns = calculateColumns(maxPoints, rows);
		double length = Math.min(size.getX() / columns, size.getY() / rows);
		int numberOfPoints = columns * rows;

		log.info("distributePoints(): maxPoints={} size={} {}*{}={} length={}", maxPoints, size, columns, rows, numberOfPoints, length);

		generator.restart();

		List<Point2d> points = new ArrayList<>(numberOfPoints);
		double radius = length / 2.0;
		double maxDistanceFromCenter = radius * 0.8;

		for (int column = 0; column < columns; column++) {
			double centerX = (column + 0.5) * length;

			for (int row = 0; row < rows; row++) {
				double centerY = (row + 0.5) * length;
				Point2d center = new Point2d(centerX, centerY);

				points.add(generatePolarPoint(center, maxDistanceFromCenter));
			}
		}

		return points;
	}

	private int calculateRows(Point2d size, int maxPoints) {
		double sizeFactor = size.getX() / size.getY();
		return (int) Math.ceil(Math.sqrt(maxPoints / sizeFactor));
	}

	public int calculateColumns(int maxPoints, int rows) {
		return (int) Math.ceil(maxPoints / (double)rows);
	}

	protected Point2d generatePolarPoint(Point2d point, double maxDistance) {
		double angle = generator.getDoubleBetweenZeroAndOne() * 2.0 * Math.PI;
		double distance = (MIN_DISTANCE + generator.getDoubleBetweenZeroAndOne() * (1.0 - MIN_DISTANCE)) * maxDistance;

		return point.fromPolar(angle, distance);
	}
}
