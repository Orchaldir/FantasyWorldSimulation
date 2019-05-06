package jfws.util.math.geometry.distribution;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.Rectangle;
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
public class PoissonDiscDistribution extends AbstractPointDistribution {

	private static final int MAY_ATTEMPTS = 20;

	protected final RandomNumberGenerator generator;

	@Override
	public List<Point2d> distributePoints(Point2d size, int maxPoints) {
		checkForInvalidSize(size);

		log.info("distributePoints(): size={} maxPoints={}", size, maxPoints);
		generator.restart();

		List<Point2d> points = new ArrayList<>();
		List<Integer> activeIndices = new ArrayList<>();

		points.add(new Point2d(size.getX()/ 2.0, size.getY()/ 2.0));
		activeIndices.add(0);

		double minDistance = 20;
		Rectangle rectangle = Rectangle.fromSize(size);

		while(generateValidPoint(points, activeIndices, rectangle, minDistance));

		log.info("distributePoints(): points={}", points.size());

		return points;
	}

	protected boolean generateValidPoint(List<Point2d> points, List<Integer> activeIndices, Rectangle rectangle, double minDistance) {
		while(!activeIndices.isEmpty()) {
			Integer activeIndex = activeIndices.get(0);
			Point2d activePoint = points.get(activeIndex);

			for (int i = 0; i < MAY_ATTEMPTS; i++) {
				Point2d candidate = generateCandidate(activePoint, minDistance);

				if(isCandidateValid(points, candidate, rectangle, minDistance)) {
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
		double distance = minDistance * 1.1;

		return activePoint.fromPolar(angle, distance);
	}

	protected boolean isCandidateValid(List<Point2d> points, Point2d candidate, Rectangle rectangle, double minDistance) {
		if(!rectangle.isInside(candidate)) {
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
}
