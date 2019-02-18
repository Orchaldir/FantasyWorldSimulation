package jfws.util.math.geometry.distribution;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.random.RandomNumberGenerator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Slf4j
public class PoissonDiscDistribution extends RandomPointDistribution {

	private final double radius;

	public PoissonDiscDistribution(RandomNumberGenerator generator, double radius) {
		super(generator);

		this.radius = radius;
	}

	@Override
	public List<Point2d> distributePoints(Point2d size, int N) {
		generator.restart();

		List<Point2d> points = new ArrayList<>(N);

		for (int i = 0; i < N; i++) {
			Point2d point = generateValidPoint(points, size);

			points.add(point);
		}

		return points;
	}

	protected Point2d generateValidPoint(List<Point2d> points, Point2d size) {

		while(true) {
			boolean isValid = true;
			Point2d candidate = generatePoint(size);

			for (Point2d point : points) {
				double distance = candidate.getDistanceTo(point);

				if(distance < radius) {
					isValid = false;
					break;
				}
			}

			if(isValid) {
				return candidate;
			}
		}
	}
}
