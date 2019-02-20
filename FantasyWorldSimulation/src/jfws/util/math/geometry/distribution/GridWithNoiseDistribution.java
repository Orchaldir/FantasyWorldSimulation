package jfws.util.math.geometry.distribution;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.random.RandomNumberGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class GridWithNoiseDistribution implements PointDistribution {

	protected RandomNumberGenerator generator;

	@Override
	public List<Point2d> distributePoints(Point2d size, double radius) {
		if(size.getX() <= 0.0) {
			throw new IllegalArgumentException("X is too small!");
		}
		else if(size.getY() <= 0.0) {
			throw new IllegalArgumentException("Y is too small!");
		}

		double length = radius * 2.0;
		double columns = Math.ceil(size.getX() / length);
		double rows = Math.ceil(size.getY() / length);
		int N = (int) (columns * rows);

		log.info("distributePoints(): radius={} size={} {}*{}={}", radius, size, columns, rows, N);

		generator.restart();

		List<Point2d> points = new ArrayList<>(N);
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

	protected Point2d generatePolarPoint(Point2d point, double maxDistance) {
		double angle = generator.getDoubleBetweenZeroAndOne() * 2.0 * Math.PI;
		double distance = (0.1 + generator.getDoubleBetweenZeroAndOne() * 0.9) * maxDistance;

		return point.fromPolar(angle, distance);
	}
}
