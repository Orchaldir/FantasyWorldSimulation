package jfws.util.math.geometry.distribution;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.random.RandomNumberGenerator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

// See "Fast Poisson Disk Sampling in Arbitrary Dimensions" by Robert Bridson

@Getter
@Slf4j
public class FastPoissonDiscDistribution extends RandomPointDistribution {

	private final static int MAY_ATTEMPTS = 20;
	private final double radius;

	public FastPoissonDiscDistribution(RandomNumberGenerator generator, double radius) {
		super(generator);

		this.radius = radius;
	}

	@Override
	public List<Point2d> distributePoints(Point2d size, int N) {
		generator.restart();

		List<Point2d> points = new ArrayList<>(N);
		List<Integer> activeIndices = new ArrayList<>();

		points.add(new Point2d(size.getX()/ 2.0, size.getY()/ 2.0));
		activeIndices.add(0);

		for (int i = 0; i < N; i++) {
			if(!generateValidPoint(points, activeIndices, size)) {
				return points;
			}
		}

		return points;
	}

	protected boolean generateValidPoint(List<Point2d> points, List<Integer> activeIndices, Point2d size) {
		while(!activeIndices.isEmpty()) {
			Integer activeIndex = activeIndices.get(0);
			Point2d activePoint = points.get(activeIndex);

			for (int i = 0; i < MAY_ATTEMPTS; i++) {
				Point2d candidate = generateCandidate(activePoint);

				if(isCandidateValid(points, candidate, size)) {
					activeIndices.add(points.size());
					points.add(candidate);
					return true;
				}
			}

			activeIndices.remove(activeIndex);
		}

		return false;
	}

	protected Point2d generateCandidate(Point2d activePoint) {
		double angle = generator.getDoubleBetweenZeroAndOne() * 2.0 * Math.PI;
		double distance = radius * 1.1;// + generator.getDoubleBetweenZeroAndOne() * radius;

		return activePoint.fromPolar(angle, distance);
	}

	protected boolean isCandidateValid(List<Point2d> points, Point2d candidate, Point2d size) {
		if(isOutside(candidate, size)) {
			return false;
		}

		for (Point2d point : points) {
			double distance = point.getDistanceTo(candidate);

			if(distance < radius) {
				return false;
			}
		}

		return true;
	}

	private boolean isOutside(Point2d candidate, Point2d size) {
		return  candidate.getX() < radius ||
				candidate.getY() < radius ||
				candidate.getX() > size.getX() - radius ||
				candidate.getY() > size.getY() - radius;
	}
}
