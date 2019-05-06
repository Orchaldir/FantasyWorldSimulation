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
	public List<Point2d> distributePoints(Point2d size, int maxPoints) {
		checkForInvalidSize(size);

		generator.restart();

		List<Point2d> points = new ArrayList<>(maxPoints);

		for (int i = 0; i < maxPoints; i++) {
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
