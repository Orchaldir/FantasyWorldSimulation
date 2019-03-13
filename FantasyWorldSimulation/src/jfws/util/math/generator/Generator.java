package jfws.util.math.generator;

import jfws.util.math.geometry.Point2d;

public interface Generator {

	double generate(double x, double y);

	default double generate(Point2d point) {
		return generate(point.getX(), point.getY());
	}

}
