package jfws.util.math.geometry.distribution;

import jfws.util.math.geometry.Point2d;

public abstract class AbstractPointDistribution implements PointDistribution {

	protected void checkForInvalidSize(Point2d size) {
		if(size.getX() <= 0.0) {
			throw new IllegalArgumentException("X is too small!");
		}
		else if(size.getY() <= 0.0) {
			throw new IllegalArgumentException("Y is too small!");
		}
	}
}
