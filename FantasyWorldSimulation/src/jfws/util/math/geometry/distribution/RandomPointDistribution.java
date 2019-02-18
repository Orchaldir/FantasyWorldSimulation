package jfws.util.math.geometry.distribution;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.random.RandomNumberGenerator;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class RandomPointDistribution implements PointDistribution {

	protected RandomNumberGenerator generator;

	@Override
	public List<Point2d> distributePoints(Point2d size, int N) {
		if(size.getX() <= 0.0) {
			throw new IllegalArgumentException("X is too small!");
		}
		else if(size.getY() <= 0.0) {
			throw new IllegalArgumentException("Y is too small!");
		}

		generator.restart();

		List<Point2d> points = new ArrayList<>(N);

		for (int i = 0; i < N; i++) {
			points.add(generatePoint(size));
		}

		return points;
	}

	protected Point2d generatePoint(Point2d size) {
		double x = generator.getDoubleBetweenZeroAndOne() * size.getX();
		double y = generator.getDoubleBetweenZeroAndOne() * size.getY();

		return  new Point2d(x, y);
	}
}
