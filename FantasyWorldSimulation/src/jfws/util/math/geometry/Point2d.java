package jfws.util.math.geometry;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Point2d {

	private final double x;
	private final double y;

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
}
