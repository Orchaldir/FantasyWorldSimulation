package jfws.util.math.geometry.distribution;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.random.RandomNumberGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class RandomPointDistribution extends AbstractPointDistribution {

	protected RandomNumberGenerator generator;

	@Override
	public List<Point2d> distributePoints(Point2d size, double radius) {
		checkForInvalidSize(size);

		int numberOfPoints = calculateNumberOfPoints(size, radius);

		generator.restart();

		List<Point2d> points = new ArrayList<>(numberOfPoints);

		for (int i = 0; i < numberOfPoints; i++) {
			points.add(generatePoint(size));
		}

		return points;
	}

	private int calculateNumberOfPoints(Point2d size, double radius) {
		double length = radius * 2.0;
		double columns = Math.ceil(size.getX() / length);
		double rows = Math.ceil(size.getY() / length);
		int numberOfPoints = (int) (columns * rows);

		log.info("calculateNumberOfPoints(): radius={} size={} {}*{}={}", radius, size, columns, rows, numberOfPoints);

		return numberOfPoints;
	}

	protected Point2d generatePoint(Point2d size) {
		double x = generator.getDoubleBetweenZeroAndOne() * size.getX();
		double y = generator.getDoubleBetweenZeroAndOne() * size.getY();

		return  new Point2d(x, y);
	}
}
