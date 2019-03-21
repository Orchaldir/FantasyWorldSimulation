package jfws.util.math.geometry;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public class Point2d {

	private final double x;
	private final double y;

	// distance

	public double getDistanceTo(Point2d point) {
		return getDistanceTo(point.x, point.y);
	}

	public double getDistanceTo(double x, double y) {
		return Math.hypot(this.x - x, this.y - y);
	}

	public double getDotProduct(Point2d point) {
		return getDotProduct(point.x, point.y);
	}

	public double getDotProduct(double x, double y) {
		return x * this.x + y * this.y;
	}

	public double getLength() {
		return getDistanceTo(0, 0);
	}

	public Point2d getNormalized() {
		double length = getLength();

		if(length == 0) {
			return new Point2d(0, 0);
		}

		double factor = 1.0 / length;

		return new Point2d(x * factor, y * factor);
	}

	// angle

	public double getAngleTo(Point2d point2d) {
		double diffX = point2d.x - x;
		double diffY = point2d.y - y;

		return Math.atan2(diffY, diffX);
	}

	// arithmetic

	public Point2d add(Point2d point) {
		return new Point2d(x + point.x, y + point.y);
	}

	public Point2d subtract(Point2d point) {
		return new Point2d(x - point.x, y - point.y);
	}

	public Point2d multiply(double value) {
		return new Point2d(x * value, y * value);
	}

	public Point2d fromPolar(double angle, double distance){
		double newX = x + Math.cos(angle) * distance;
		double newY = y + Math.sin(angle) * distance;

		return new Point2d(newX, newY);
	}

	public static Point2d calculateCentroid(Collection<Point2d> points) {
		double sumX = 0.0;
		double sumY = 0.0;
		int numberOfPoints = points.size();

		if(numberOfPoints == 0) {
			throw new IllegalArgumentException("No Points!");
		}

		for (Point2d point : points) {
			sumX += point.x;
			sumY += point.y;
		}

		return new Point2d(sumX / numberOfPoints, sumY / numberOfPoints);
	}
}
