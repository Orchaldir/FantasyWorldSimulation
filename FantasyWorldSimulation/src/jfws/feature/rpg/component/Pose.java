package jfws.feature.rpg.component;

import jfws.util.math.geometry.Point2d;

public class Pose {
	public Point2d position;
	public double orientation; // in radians

	public Pose(double x, double y, double orientation) {
		this.position = new Point2d(x, y);
		this.orientation = orientation;
	}

	@Override
	public String toString() {
		return String.format("Pose(x=%.3f y=%.3f orientation=%.1f deg)",
				position.getX(), position.getY(), Math.toDegrees(orientation));
	}
}
