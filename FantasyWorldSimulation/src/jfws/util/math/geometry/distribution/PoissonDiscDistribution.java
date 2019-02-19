package jfws.util.math.geometry.distribution;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.random.RandomNumberGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

// See "Fast Poisson Disk Sampling in Arbitrary Dimensions" by Robert Bridson

@AllArgsConstructor
@Getter
@Slf4j
public class PoissonDiscDistribution implements PointDistribution {

	private final static int MAY_ATTEMPTS = 20;

	protected RandomNumberGenerator generator;

	@Override
	public List<Point2d> distributePoints(Point2d size, double radius) {
		log.info("distributePoints(): size={} radius={}", size, radius);
		generator.restart();

		List<Point2d> points = new ArrayList<>();
		List<Integer> activeIndices = new ArrayList<>();

		points.add(new Point2d(size.getX()/ 2.0, size.getY()/ 2.0));
		activeIndices.add(0);

		double minDistance = radius * 2.0;

		while(generateValidPoint(points, activeIndices, size, minDistance)) {
		}

		return points;
	}

	protected boolean generateValidPoint(List<Point2d> points, List<Integer> activeIndices, Point2d size, double minDistance) {
		while(!activeIndices.isEmpty()) {
			Integer activeIndex = activeIndices.get(0);
			Point2d activePoint = points.get(activeIndex);

			for (int i = 0; i < MAY_ATTEMPTS; i++) {
				Point2d candidate = generateCandidate(activePoint, minDistance);

				if(isCandidateValid(points, candidate, size, minDistance)) {
					activeIndices.add(points.size());
					points.add(candidate);
					return true;
				}
			}

			activeIndices.remove(activeIndex);
		}

		return false;
	}

	protected Point2d generateCandidate(Point2d activePoint, double minDistance) {
		double angle = generator.getDoubleBetweenZeroAndOne() * 2.0 * Math.PI;
		double distance = minDistance * 1.1;// + generator.getDoubleBetweenZeroAndOne() * radius;

		return activePoint.fromPolar(angle, distance);
	}

	protected boolean isCandidateValid(List<Point2d> points, Point2d candidate, Point2d size, double minDistance) {
		if(isOutside(candidate, size, minDistance)) {
			return false;
		}

		for (Point2d point : points) {
			double distance = point.getDistanceTo(candidate);

			if(distance < minDistance) {
				return false;
			}
		}

		return true;
	}

	private boolean isOutside(Point2d candidate, Point2d size, double radius) {
		return  candidate.getX() < 0 ||
				candidate.getY() < 0 ||
				candidate.getX() > size.getX() ||
				candidate.getY() > size.getY();
	}
}
