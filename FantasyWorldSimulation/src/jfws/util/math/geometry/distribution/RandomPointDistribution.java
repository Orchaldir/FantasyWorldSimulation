package jfws.util.math.geometry.distribution;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.random.RandomNumberGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class RandomPointDistribution implements PointDistribution {

	protected RandomNumberGenerator generator;

	@Override
	public List<Point2d> distributePoints(Point2d size, double radius) {
		if(size.getX() <= 0.0) {
			throw new IllegalArgumentException("X is too small!");
		}
		else if(size.getY() <= 0.0) {
			throw new IllegalArgumentException("Y is too small!");
		}

		int N = calculateNumberOfPoints(size, radius);

		generator.restart();

		List<Point2d> points = new ArrayList<>(N);

		for (int i = 0; i < N; i++) {
			points.add(generatePoint(size));
		}

		return points;
	}

	private int calculateNumberOfPoints(Point2d size, double radius) {
		double length = radius * 2.0;
		double columns = Math.ceil(size.getX() / length);
		double rows = Math.ceil(size.getY() / length);
		int N = (int) (columns * rows);

		log.info("calculateNumberOfPoints(): radius={} size={} {}*{}={}", radius, size, columns, rows, N);

		return N;
	}

	protected Point2d generatePoint(Point2d size) {
		double x = generator.getDoubleBetweenZeroAndOne() * size.getX();
		double y = generator.getDoubleBetweenZeroAndOne() * size.getY();

		return  new Point2d(x, y);
	}
}
