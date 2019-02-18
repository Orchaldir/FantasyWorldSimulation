package jfws.util.math.geometry;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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

	// arithmetic

	public Point2d add(Point2d point) {
		return new Point2d(x + point.x, y + point.y);
	}

	public Point2d sub(Point2d point) {
		return new Point2d(x - point.x, y - point.y);
	}
}
