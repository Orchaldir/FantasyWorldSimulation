package jfws.util.math.geometry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Point2d {

	private double x;
	private double y;

	public double getDistanceTo(Point2d point) {
		return Math.hypot(point.x - x, point.y - y);
	}
}
