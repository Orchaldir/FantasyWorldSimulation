package jfws.util.math.geometry;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Point2d {

	private final double x;
	private final double y;

	public double getDistanceTo(Point2d point) {
		return Math.hypot(point.x - x, point.y - y);
	}

	public double getDotProduct(Point2d point) {
		return x * point.x + y * point.y;
	}
}
